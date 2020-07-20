package com.google.edith;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.Exception;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** 
  * Servlet that takes the user input from the receipt
  * form and uses it to retrieve the best deal. 
  */
@WebServlet("/receipt-data")
public class DealsServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    StringBuilder stringBuilder = new StringBuilder();
    try (BufferedReader reader = request.getReader()) {
      String line;
      while ((line = reader.readLine()) != null) {
        stringBuilder.append(line).append('\n');
      }
    }
    String receiptData = stringBuilder.toString();
    JsonParser parser = new JsonParser();
    JsonObject inputjson = parser.parse(receiptData).getAsJsonObject();
    JsonObject data = parser.parse(inputjson.get("data").getAsString()).getAsJsonObject();
    JsonArray items = data.get("items").getAsJsonArray();
    
    DealItem cheapestItem = null;
    List<DealItem> deals = new ArrayList<DealItem>();
    for (int i = 0; i < items.size(); i++) {
      cheapestItem = null;
      JsonObject item = items.get(i).getAsJsonObject();
      String itemName = item.get("name").getAsString();
      try {
        GroceryNameProcessor processor = new GroceryNameProcessor();
        itemName = processor.process(itemName);
      } catch (Exception e) {
        System.out.println("error");
      }

      GroceryDataReader groceryReader = new GroceryDataReader();
      cheapestItem = groceryReader.readFile(itemName.toLowerCase());

      if (cheapestItem != null) {
        deals.add(cheapestItem);
        //Gson gson = new Gson();
        //String responseJson = gson.toJson(cheapestItem);
        //System.out.println(responseJson);
        //response.setContentType("application/json");
       // response.getWriter().println(responseJson);
      }
    }
    Gson gson = new Gson();
    String dealItems = gson.toJson(deals);
    System.out.println(dealItems);
    response.setContentType("application/json");
    response.getWriter().println(dealItems);
  }
}
