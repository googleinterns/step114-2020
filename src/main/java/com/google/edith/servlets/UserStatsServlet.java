package com.google.edith.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Servlet that returns some example content.
 */
@WebServlet("/user-stats-servlet")
public class UserStatsServlet extends HttpServlet {

  private final DatastoreService datastore;
  private UserInsightsInterface userInsights;

  public UserStatsServlet() {
    this.datastore = DatastoreServiceFactory.getDatastoreService();
    this.userInsights = new UserInsightsService("userId");
  }

  public UserStatsServlet(DatastoreService datastore, UserInsightsInterface userInsights) {
    this.datastore = datastore;
    this.userInsights = userInsights;
  } 

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/json");   
    response.getWriter().println(userInsights.createJson());
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    StringBuilder sb = new StringBuilder();
    BufferedReader reader = request.getReader();
    try {
      String line;
      while ((line = reader.readLine()) != null) {
        sb.append(line).append("\n");
      }
    } finally {
      reader.close();
    }

    String receiptData = sb.toString();
    JsonParser parser = new JsonParser();
    JsonObject json = (JsonObject) parser.parse(receiptData);

    Entity itemEntity = new Entity("Item");

    itemEntity.setProperty("userId", "userId");
    itemEntity.setProperty("name", json.get("itemName").getAsString());
    itemEntity.setProperty("price", Double.parseDouble(json.get("itemPrice").getAsString()));
    itemEntity.setProperty("quantity", Long.parseLong(json.get("itemQuantity").getAsString()));
    itemEntity.setProperty("date", json.get("itemDate").getAsString());
    
    datastore.put(itemEntity);
    Query itemQuery = new Query("Item");
    List<Key> itemKeys = datastore.prepare(itemQuery)
                            .asList(FetchOptions.Builder
                                                .withLimit(Integer.MAX_VALUE))
                            .stream()
                            .map(entity -> entity.getKey())
                            .collect(Collectors.toList());
    if (!userInsights.retreiveUserStats().isPresent()) {
      userInsights.createUserStats();
    }
    userInsights.updateUserStats(itemKeys);
    response.setContentType("text/html");
    response.getWriter().println("Item posted");
  }
}
