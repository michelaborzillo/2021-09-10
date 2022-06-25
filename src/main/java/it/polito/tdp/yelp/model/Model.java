package it.polito.tdp.yelp.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	YelpDao dao;
	Graph<Locale, DefaultWeightedEdge> grafo;
	Map<String, Locale> idMap;
	List<Locale> best;
	public Model () {
		dao= new YelpDao();
		idMap= new HashMap<String, Locale>();
	}

	
	public void creaGrafo (String citta) {
		this.grafo = new SimpleWeightedGraph<Locale, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		List<Locale> locale = dao.getVertici(citta);
		Graphs.addAllVertices(this.grafo, locale);
		for (Locale l1: locale) {
			for (Locale l2: locale) {
				if (!l1.equals(l2)&&l1.getBusiness_id().compareTo(l2.getBusiness_id())<0 ) {
					double peso= dao.getPeso(l1, l2);
					if (peso>0) {
						Graphs.addEdge(this.grafo, l1,l2, peso);
					}
				}
			}
		}
	}
	public List<String> getCitta() {
		return this.dao.loadCitta();
	}

	public int nVertici() {
		return this.grafo.vertexSet().size();
		
	}

	public int nArchi() {
		return this.grafo.edgeSet().size();
	
	}
	
	public Set<Locale> addVertici(){
		return this.grafo.vertexSet();
	}
	
/*	public List<LocalePiuDistante> getLocalePiuDistante(Locale l) {
		int max=0;
		List<LocalePiuDistante> distante = new ArrayList<LocalePiuDistante>();
		for (DefaultWeightedEdge e: this.grafo.edgesOf(l)) {
			if (this.grafo.getEdgeWeight(e)>max) {
				max=(int) this.grafo.getEdgeWeight(e);
			}
		}
		
		List<Locale> result= new ArrayList<Locale>();
		for (DefaultWeightedEdge e: this.grafo.edgesOf(l)) {
			if (this.grafo.getEdgeWeight(e)==max) {
				Locale l2= Graphs.getOppositeVertex(this.grafo, e, l);
				result.add(l2);
		}
	}
		for (Locale ll: result) {
			distante.add(new LocalePiuDistante(ll, this.grafo.getEdgeWeight(this.grafo.getEdge(l,ll))));
			
		}
		
		return distante;
		
		
		
	}*/
	/*public LocalePiuDistante getLocalePiuDistante() {
		if (this.grafo==null)
			return null;
		Locale best=null;
		double max=Integer.MIN_VALUE;
		for (Locale p: this.grafo.vertexSet()) {
			double pesoUscente=0;
			for (DefaultWeightedEdge edge: this.grafo.outgoingEdgesOf(p)) {
				pesoUscente+=this.grafo.getEdgeWeight(edge);
				
			}
			double pesoEntrante=0;
			for (DefaultWeightedEdge edge: this.grafo.incomingEdgesOf(p)) {
				pesoEntrante+=this.grafo.getEdgeWeight(edge);
			}
			double delta=pesoUscente-pesoEntrante;
			if (delta>max) {
				best=p;
				max=delta;
			}
		}
		return new LocalePiuDistante(best, max);
	}
	*/
	public LocalePiuDistante getLocalePiuDistante(Locale p) {
		if (this.grafo==null)
			return null;
		Locale best = null;
		double max=0.0;
		LocalePiuDistante lpd=null;
			
			for (DefaultWeightedEdge edge: this.grafo.edgesOf(p)) {
				if (this.grafo.getEdgeWeight(edge)>max) {
					max=(int) this.grafo.getEdgeWeight(edge);
					best = Graphs.getOppositeVertex(this.grafo,edge, p);
					lpd= new LocalePiuDistante(best, max);
				
			}
			}
		
			return lpd;
	}
	
	
	public List<Locale> getRecesioni (double x) {
		return dao.getLocaliRecensioni(x);
	}
	
	
	public List<Locale> calcolaPercorso (Locale partenza, Locale destinazione, double soglia) {
		best= new LinkedList<Locale>();
		List<Locale> parziale = new LinkedList<Locale>();
		parziale.add(partenza);
		cerca(parziale, destinazione, soglia);
		return best;
	}
	
	
	
	private void cerca (List<Locale> parziale, Locale destinazione, double soglia) {
		if (parziale.get(parziale.size()-1).equals(destinazione)) {
			if (parziale.size()>best.size()) {
				best= new LinkedList<> (parziale);
			}
			return; 
		
	}
		for (Locale l: Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1))) {
			if (this.getLocaliConRece(soglia).contains(l) && !parziale.contains(l)) {
				parziale.add(l);
				this.cerca(parziale, destinazione, soglia);
				parziale.remove(parziale.size()-1);
			}
		}
		
		
	}
	
	private List<Locale> getLocaliConRece(double x){
		List<Locale> result= new ArrayList<Locale>();
		for (Locale l: this.getRecesioni(x)) {
			result.add(l);
		}
		return result;
	}
	
}
