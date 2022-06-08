package it.polito.tdp.yelp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.CoppiaLocali;
import it.polito.tdp.yelp.model.Locale;
import it.polito.tdp.yelp.model.Review;
import it.polito.tdp.yelp.model.User;
import javafx.collections.MapChangeListener;

public class YelpDao {
	
	
	public List<Business> getAllBusiness(){
		String sql = "SELECT * FROM Business";
		List<Business> result = new ArrayList<Business>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Business business = new Business(res.getString("business_id"), 
						res.getString("full_address"),
						res.getString("active"),
						res.getString("categories"),
						res.getString("city"),
						res.getInt("review_count"),
						res.getString("business_name"),
						res.getString("neighborhoods"),
						res.getDouble("latitude"),
						res.getDouble("longitude"),
						res.getString("state"),
						res.getDouble("stars"));
				result.add(business);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Review> getAllReviews(){
		String sql = "SELECT * FROM Reviews";
		List<Review> result = new ArrayList<Review>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Review review = new Review(res.getString("review_id"), 
						res.getString("business_id"),
						res.getString("user_id"),
						res.getDouble("stars"),
						res.getDate("review_date").toLocalDate(),
						res.getInt("votes_funny"),
						res.getInt("votes_useful"),
						res.getInt("votes_cool"),
						res.getString("review_text"));
				result.add(review);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<User> getAllUsers(){
		String sql = "SELECT * FROM Users";
		List<User> result = new ArrayList<User>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				User user = new User(res.getString("user_id"),
						res.getInt("votes_funny"),
						res.getInt("votes_useful"),
						res.getInt("votes_cool"),
						res.getString("name"),
						res.getDouble("average_stars"),
						res.getInt("review_count"));
				
				result.add(user);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> getAllCitta () {
		String sql="SELECT DISTINCT city "
				+ "FROM business";
		List<String> result= new ArrayList<String>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				String citta= res.getString("city");
				result.add(citta);
			}
			conn.close();
			return result;
		
			}catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
	}

	/*public List<String> getLocali(String citta, Map<String, String> idMap) {
		String sql="SELECT business_name, business_id "
				+ "FROM business "
				+ "WHERE city=?";
		
		List<String> result= new ArrayList<String>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, citta);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				String string = res.getString("business_id");
				idMap.put(res.getString("business_id"), string);
				result.add(idMap.get(string));
			}
			conn.close();
			return result;
			
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}*/

	/*public List<CoppiaLocali> getCoppiaLocali(String citta, Map<String, String> idMap) {
		String sql= "SELECT b1.business_id AS business_id1, b2.business_id AS business_id2, b1.longitude AS longb1, b1.latitude AS latb1, b2.longitude AS longb2, b2.latitude AS latb2 "
				+ "FROM business b1, business b2 "
				+ "WHERE b1.city=? AND b2.city=? "
				+"GROUP BY b1.business_id, b2.business_id";
		Connection conn = DBConnect.getConnection();
		List<CoppiaLocali> result= new ArrayList<CoppiaLocali>();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, citta);
			st.setString(2, citta);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				String l1= res.getString("business_id1");
				String l2= res.getString("business_id2");
				LatLng peso1= new LatLng(res.getDouble("latb1"), res.getDouble("longb1"));
				LatLng peso2= new LatLng(res.getDouble("latb2"), res.getDouble("longb2"));
				
				double peso=LatLngTool.distance(peso1, peso2, LengthUnit.KILOMETER);
				if (idMap.get(l1)!= null && idMap.get(l2)!=null) {
				result.add(new CoppiaLocali(idMap.get(l1),idMap.get(l2), peso));
				}
			}
			conn.close();
			return result;
		}catch (SQLException e) {
			e.printStackTrace();
			return null;
		}*/
		
		
		public List<Locale> getLocali(String citta, Map<String, Locale>idMap) {
			String sql="SELECT business_id, business_name, latitude, longitude "
					+ "FROM business "
					+ "WHERE city=?";
			List<Locale> result= new ArrayList<Locale>();
			Connection conn= DBConnect.getConnection();
			try {
				PreparedStatement st = conn.prepareStatement(sql);
				st.setString(1, citta);
				ResultSet res = st.executeQuery();
				while (res.next()) {
					Locale loc= new Locale(res.getString("business_id"), res.getString("business_name"), res.getDouble("longitude"), res.getDouble("latitude"));
					idMap.put(loc.getBusiness_id(), loc);
					result.add(loc);
				}
				conn.close();
				return result;
				
			}
			catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}

		public List<CoppiaLocali> getCoppiaLocali(String citta, Map<String, Locale> idMap) {
			String sql= "SELECT b1.business_id AS l1, b2.business_id AS l2, b1.longitude AS lo1, b1.latitude AS la1, b2.longitude AS lo2, b2.latitude AS la2 "
					+ "FROM business b1, business b2 "
					+ "WHERE b1.city=? AND b2.city=?";
				
			Connection conn = DBConnect.getConnection();
			List<CoppiaLocali> result= new ArrayList<CoppiaLocali>();
			try {
				PreparedStatement st = conn.prepareStatement(sql);
				st.setString(1, citta);
				st.setString(2, citta);
				ResultSet res = st.executeQuery();
				while(res.next()) {
					Locale l1= idMap.get(res.getString("l1"));
					Locale l2= idMap.get(res.getString("l2"));
					idMap.put(l1.getBusiness_id(), l1);
					idMap.put(l2.getBusiness_id(), l2);
					
					LatLng peso1= new LatLng(res.getDouble("la1"), res.getDouble("lo1"));
					LatLng peso2= new LatLng(res.getDouble("la2"), res.getDouble("lo2"));
					double peso=LatLngTool.distance(peso1, peso2, LengthUnit.KILOMETER);
					if (l1!=null && l2!=null && idMap.get("l1")!=null && idMap.get("l2")!=null) {
					result.add(new CoppiaLocali(l1, l2, peso));
					}
				}
				conn.close();
				return result;
			}catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
			
		}

		public double getPeso(Locale l1, Locale l2) {
			String sql="SELECT b1.business_id AS l1, b2.business_id AS l2, b1.longitude AS lo1, b1.latitude AS la1, b2.longitude AS lo2, b2.latitude AS la2 "
					+ "FROM business b1, business b2 "
					+ "WHERE b1.business_id=? AND b2.business_id=?";
					
			Connection conn= DBConnect.getConnection();
			
			try {
				PreparedStatement st = conn.prepareStatement(sql);
				st.setString(1, l1.getBusiness_id());
				st.setString(2, l2.getBusiness_id());
				ResultSet res=st.executeQuery();
				double peso=0;
				if (res.next()) {
					
					LatLng peso1= new LatLng(res.getDouble("la1"), res.getDouble("lo1"));
					LatLng peso2= new LatLng(res.getDouble("la2"), res.getDouble("lo2"));
				 peso=LatLngTool.distance(peso1, peso2, LengthUnit.KILOMETER);
				}
				conn.close();
				return peso;
			}catch (SQLException e) {
				e.printStackTrace();
				return 0;
			}
		}
		
	
	
	

	
	
}
