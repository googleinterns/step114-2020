package com.google.edith.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Retrieves information from webpage and returns User information in JSON format or updates user
 * information.
 */
@WebServlet("/user-stats-servlet")
public class UserStatsServlet extends HttpServlet {

  private final DatastoreService datastore;
  private final UserInsightsInterface userInsights;

  public UserStatsServlet() {
    this.datastore = DatastoreServiceFactory.getDatastoreService();
    this.userInsights = new UserInsightsService();
  }

  public UserStatsServlet(DatastoreService datastore, UserInsightsInterface userInsights) {
    this.datastore = datastore;
    this.userInsights = userInsights;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/json");
    String userId = "userId";
    response.getWriter().println(userInsights.createJson(userId));
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    StringBuilder stringBuilder = new StringBuilder();
    try (BufferedReader reader = request.getReader()) {
      String line;
      while ((line = reader.readLine()) != null) {
        stringBuilder.append(line).append("\n");
      }
    } catch (IOException e) {
      e.printStackTrace(System.out);
    }

    String receiptData = stringBuilder.toString();
    JsonObject json = (JsonObject) JsonParser.parseString(receiptData);
    String userId = "userId";

    Entity itemEntity = new Entity("Item");

    itemEntity.setProperty("name", json.get("itemName").getAsString());
    itemEntity.setProperty("userId", userId);
    itemEntity.setProperty("category", json.get("itemCategory").getAsString());
    itemEntity.setProperty("price", Double.parseDouble(json.get("itemPrice").getAsString()));
    itemEntity.setProperty("quantity", Long.parseLong(json.get("itemQuantity").getAsString()));
    itemEntity.setProperty("date", json.get("itemDate").getAsString());

    datastore.put(itemEntity);
    Query itemQuery = new Query("Item");
    List<Key> itemKeys =
        datastore
            .prepare(itemQuery)
            .asList(FetchOptions.Builder.withLimit(Integer.MAX_VALUE))
            .stream()
            .map(Entity::getKey)
            .collect(Collectors.toList());
    if (!userInsights.retreiveUserStats(userId).isPresent()) {
      userInsights.createUserStats(userId);
    }
    userInsights.updateUserStats(userId, itemKeys);
    response.setContentType("text/html");
    response.getWriter().println("Item posted");
  }
}
