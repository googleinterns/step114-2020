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
    System.out.println(receiptData);
    JsonParser parser = new JsonParser();
    JsonObject inputjson = parser.parse(receiptData).getAsJsonObject();
    System.out.println(inputjson);
    JsonObject data = parser.parse(inputjson.get("data").getAsString()).getAsJsonObject();
    System.out.println(data);
    JsonArray items = data.get("items").getAsJsonArray();
    System.out.println(items);
    
    DealItem bestItem = null;
    List<DealItem> deals = new ArrayList<DealItem>();
    for (int i = 0; i < items.size(); i++) {
      bestItem = null;
      JsonObject item = items.get(i).getAsJsonObject();
      String itemName = item.get("name").getAsString();
      try {
        GroceryNameProcessor processor = new GroceryNameProcessor();
        itemName = processor.process(itemName);
      } catch (Exception e) {
        System.out.println("error");
      }

      GroceryDataReader groceryReader = new GroceryDataReader();
      DealItem cheapestItem = groceryReader.readFile(itemName.toLowerCase());

      if (cheapestItem != null) {
        Gson gson = new Gson();
        String responseJson = gson.toJson(deals);
        System.out.println(responseJson);
        response.setContentType("application/json");
        response.getWriter().println(responseJson);
      }
    }
  }
}
