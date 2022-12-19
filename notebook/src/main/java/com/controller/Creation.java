package com.controller;
import java.sql.*;


public class Creation {
	
	static public Connection conn=null;
	
	public Creation() {
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
	public void update(String tag , String note) {
		
		try {
			PreparedStatement ps =conn.prepareStatement("INSERT INTO Notes(tag,note) VALUES(?,?)");
			ps.setString(1,tag);
			ps.setString(2,note);
			ps.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
	 }

}

