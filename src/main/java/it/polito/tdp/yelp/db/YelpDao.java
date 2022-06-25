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

	public List<String> loadCitta() {
	String sql="SELECT distinct b.city AS citta "
			+ "FROM business b "
			+ "ORDER BY citta";
	List<String> result= new ArrayList<String>();
	
	Connection conn = DBConnect.getConnection();

	try {
		PreparedStatement st = conn.prepareStatement(sql);
		ResultSet res = st.executeQuery();
		while (res.next()) {

			String s= res.getString("citta");
			
			result.add(s);
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
	

	public List<Locale> getVertici (String citta) {
		String sql="SELECT b.business_id AS id, b.business_name AS name, b.latitude AS lati, b.longitude AS longi "
				+ "FROM business b "
				+ "WHERE b.city=?";
		List<Locale> result= new ArrayList<Locale>();
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, citta);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Locale loc = new Locale(res.getString("id"), res.getString("name"), res.getDouble("lati"), res.getDouble("longi"));
				result.add(loc);
			
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
	
	
	public double getPeso (Locale l1, Locale l2) {
		String sql="SELECT b1.latitude AS lat1, b1.longitude AS long1, b2.latitude AS lat2, b2.longitude AS long2 "
				+ "FROM business b1, business b2 "
				+ "WHERE b1.business_id=? AND b2.business_id=?";
		double peso=0;
		Connection conn= DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, l1.getBusiness_id());
			st.setString(2, l2.getBusiness_id());
			ResultSet res=st.executeQuery();
			if (res.next()) {
				LatLng la1=new LatLng(res.getDouble("lat1"), res.getDouble("long1"));
				LatLng la2=new LatLng(res.getDouble("lat2"), res.getDouble("long2"));
				peso= LatLngTool.distance(la1, la2, LengthUnit.KILOMETER);
				
			
			}
			conn.close();
			return peso;
		}catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}	
	
	
	public List<Locale> getLocaliRecensioni(double x){
		String sql="SELECT b.business_id AS id, b.business_name AS name, b.latitude AS lati, b.longitude AS longi "
				+ "FROM business b "
				+ "WHERE b.stars>=?";
List<Locale> result= new ArrayList<Locale>();
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1,x);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Locale loc = new Locale(res.getString("id"), res.getString("name"), res.getDouble("lati"), res.getDouble("longi"));
				result.add(loc);
			
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

	
	
}
