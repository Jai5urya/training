package com.controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.model.Notes;

public class Fetch {
	

	 private Connection conn;
	 
	 public Fetch() throws Exception {
		 start();
	 }
	
	
	 private void start() {
		    String url = "jdbc:mysql://localhost:3306/notebook";
			try {
				 Class.forName("com.mysql.cj.jdbc.Driver");
				  conn = DriverManager.getConnection(url,"root","88888888");
			} catch (Exception e) {
				e.printStackTrace();
			}
		   
	}


	public List<JSONObject> searchByTag(String tag ) throws Exception {
		PreparedStatement ps =  conn.prepareStatement("select * from notes where tag = ? ");
		 ps.setString(1, tag);

//		 System.out.println(tag);
		 ResultSet rs  = ps.executeQuery();
		 
		 if(!rs.next())
			 return null;
//		 rs.previous();
		 
		 List<JSONObject> args = new ArrayList<>();
		 

		 Notes notes = new Notes();
		 notes.setId(rs.getInt("id"));
		 notes.setNote(rs.getString("note"));
		 notes.setTag(rs.getString("tag"));
		 
		 args.add(convert(notes));
		 
		 while(rs.next()) {
			 notes = new Notes();
			 notes.setId(rs.getInt("id"));
			 notes.setNote(rs.getString("note"));
			 notes.setTag(rs.getString("tag"));
			 
			 args.add(convert(notes));
		 }
//		 System.out.println("db class " + args.get(0).toString());
		 return args;
		 
		 
		 
	  }
	 public List<JSONObject> searchByid(int id) throws Exception {
		 PreparedStatement ps =  conn.prepareStatement("select * from notes where id = ? ");
		 ps.setInt(1,id);
		 ResultSet rs  = ps.executeQuery();
		 
		 if(!rs.next())
			 return null;
		 rs.previous();
		 
		 List<JSONObject> args = new ArrayList<>();
		 
		 while(rs.next()) {
			 Notes notes = new Notes();
			 notes.setId(rs.getInt("id"));
			 notes.setNote(rs.getString("note"));
			 notes.setTag(rs.getString("tag"));
			 
			 args.add(convert(notes));
		 }
		 
		 return args;
		 
	  }
	 public List<JSONObject> searchByContent(String content ) throws Exception {
			PreparedStatement ps =  conn.prepareStatement("select * from notes where note like %?% ");
			 ps.setString(1, content);
			 ResultSet rs  = ps.executeQuery();
			 
			 if(!rs.next())
				 return null;
			 rs.previous();
			 
			 List<JSONObject> args = new ArrayList<>();
			 
			 while(rs.next()) {
				 Notes notes = new Notes();
				 notes.setId(rs.getInt("id"));
				 notes.setNote(rs.getString("note"));
				 notes.setTag(rs.getString("tag"));
				 
				 args.add(convert(notes));
			 }
			 
			 return args;
			 
			 
			 
	  }
	 private JSONObject convert(Notes notes) {
		 JSONObject jo = new JSONObject();
		 try {
			jo.put("id", notes.getId());
			jo.put("tag", notes.getTag());
			jo.put("note", notes.getNote());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		 return jo;
	 }
	 
	 
}

