package com.google.sps.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException; 
import java.net.URL; 
import java.util.HashMap;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that fetches deals from the Discount API */
@WebServlet("/deals")
public class DealsServlet extends HttpServlet {

  private static final String apiKey = "BKFuTzor";
  private static final String apiSite = "https://api.discount.com/v2/deals?api_key=%s&query=%s";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    System.out.println("running");
    String itemName = request.getParameter("itemName");
    String title = getDeals(itemName);
    response.getWriter().println(title);
    response.sendRedirect("/index.html");
  }

  // Fetch deals from Discount API.
  private String getDeals(String category) {
    String dealsApi = String.format(apiSite, apiKey, category);
    URL url = null;
    try {
      url = new URL(dealsApi);
    } catch(IOException e) {
      System.out.println(dealsApi);
    }
    
    HttpURLConnection connection = null;
    try {
      connection = (HttpURLConnection) url.openConnection();
    } catch(IOException e) {
      System.out.println("not connected");
    }
    
    try {
      connection.setRequestMethod("get");
      connection.setRequestProperty("Content-Type", "application/json");
    } catch(ProtocolException e) {
      System.out.println("not connected");
    }

    StringBuilder content = null;
    try(BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
      String line;
      content = new StringBuilder();
      while((line = input.readLine()) != null) {
        content.append(line);
        content.append(System.lineSeparator());
      }
    } catch(IOException e) {
      System.out.println("not working");
    } finally {
      connection.disconnect();
    }
    Gson gson = new Gson();
    String json = gson.toJson(content);
    return handleResponse(json);
  }

  // Handles the deals info.
  private String handleResponse(String json) {
    JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
    JsonObject deals = jsonObject.getAsJsonObject("deals");
    JsonObject deal = deals.getAsJsonObject("deal");
    String title = deal.get("title").getAsString();
    return title;
  }

}
