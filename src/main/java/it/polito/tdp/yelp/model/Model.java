package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {

	YelpDao dao;
	//Graph<String, DefaultWeightedEdge> grafo;
	//Map<String, String> idMap;
	Graph<Locale, DefaultWeightedEdge> grafo;
	Map<String, Locale> idMap;
	
	public Model () {
		dao= new YelpDao();
	//idMap= new HashMap<String, String>();
		idMap= new HashMap<String, Locale>();
		
	}
	
	public List<String> getCitta() {
		
		List<String> list= dao.getAllCitta();
		return list;
	}
	
	public void creaGrafo (String citta) {
		/*this.grafo= new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		List<String> loc= dao.getLocali(citta, idMap);
		Graphs.addAllVertices(this.grafo, idMap.values());
		for (CoppiaLocali cl: dao.getCoppiaLocali(citta, idMap)) {
			Graphs.addEdgeWithVertices(this.grafo, cl.getBusiness_id1(), cl.getBusiness_id2(), cl.getPeso());
		}*/
		this.grafo= new SimpleWeightedGraph<Locale, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		List<Locale> loc= dao.getLocali(citta, idMap);
		Graphs.addAllVertices(this.grafo, loc);
		for (CoppiaLocali cl: dao.getCoppiaLocali(citta, idMap)) {
			Graphs.addEdgeWithVertices(this.grafo, cl.getL1(), cl.getL2(), cl.getPeso());
		}
		
		
	}
	public int nVertici() {
		return this.grafo.vertexSet().size();
		
	}

	public int nArchi() {
		return this.grafo.edgeSet().size();
	
	}

	public List<Locale> getLocaliGrafo() {
		List<Locale> vertici=new ArrayList<Locale>(this.grafo.vertexSet());
		
		return vertici;
	}
	
	
	public double pesoGrafo (Locale l1, Locale l2) {
		return dao.getPeso(l1, l2);
	}
	

		public List<Locale> getAdiacenza(Locale l) {
			List<Locale> vicini= new LinkedList<Locale>(Graphs.neighborListOf(this.grafo, l));
		return null;	
		}

}
