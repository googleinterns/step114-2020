package com.google.sps.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStream;
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
    InputStream params = request.getInputStream();
    JsonParser jsonParser = new JsonParser();
    JsonObject jsonObject = (JsonObject)jsonParser.parse(
      new InputStreamReader(params, "utf-8"));
    String itemName = jsonObject.get("itemName").getAsString();
    System.out.println(itemName);
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
    System.out.println(url);
    HttpURLConnection connection = null;
    try {
      connection = (HttpURLConnection) url.openConnection();
    } catch(IOException e) {
      System.out.println("connection not opened");
      System.out.println(e.getMessage());
    }
    
    try {
      connection.setRequestMethod("GET");
      connection.setRequestProperty("Content-Type", "application/json");
    } catch(ProtocolException e) {
      System.out.println("can't set method of request");
      System.out.println(e.getMessage());
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
      System.out.println("can't read in info from connection");
      System.out.println(e.getMessage());
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
