package com.servelets;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// import org.apache.*;
import org.json.JSONException;
import org.json.JSONObject;

import com.controller.Creation;

@WebServlet("/Notes")
public class Note extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
	String noteDetails = request.getReader().lines().collect(Collectors.joining());
	        JSONObject json = new JSONObject();
	        boolean status = true;
		try {
			JSONObject noteData = new JSONObject(noteDetails);
			System.out.println(noteDetails);
			String note = noteData.getString("note");
			String tag = noteData.getString("tag");
			Creation create = new Creation();
 			create.update(tag,note);
 			
		} catch (JSONException e) {
			status = false;
			e.printStackTrace();
		}
		try {
			json.put("status", status);
		} catch (JSONException e) {
			System.out.println("Inside exception");
		}
		response.getWriter().write(json.toString());
		System.out.println(json.toString());
	}

}