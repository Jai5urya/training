package com.servelets;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.controller.Fetch;

@WebServlet("/Search")
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		boolean dataStatus = true;
		JSONArray dataarr = new JSONArray();
		JSONObject json = new JSONObject();
		String noteDetails = request.getReader().lines().collect(Collectors.joining());
		try {
			JSONObject tagData = new JSONObject(noteDetails);
			String tag = tagData.getString("tag");

			Fetch fetch = new Fetch();
			List<JSONObject> list = fetch.searchByTag(tag);
//			System.out.println("inside convert");
			if (list != null) {
				for (JSONObject temp : list)
					dataarr.put(temp);
			} else
				dataStatus = false;

		} catch (Exception e) {
			System.out.println(e);
			dataStatus = false;
			e.printStackTrace();
		}

		try {
			if (dataStatus == true) {
				json.put("data", dataarr);
				json.put("status", dataStatus);
			} else {
				json.put("status", dataStatus);
			}
		} catch (JSONException e) {
		}

		System.out.println(json.toString());
		response.getWriter().write(json.toString());

	}

}