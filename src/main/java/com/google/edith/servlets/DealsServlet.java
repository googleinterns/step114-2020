package com.google.edith;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.Exception;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that takes the user input from the receipt
  * form and uses it to retrieve the best deal. */
@WebServlet("/receipt-data")
public class DealsServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    StringBuilder sb = new StringBuilder();
    BufferedReader reader = request.getReader();
    try {
      String line;
      while ((line = reader.readLine()) != null) {
        sb.append(line).append('\n');
      }
    } finally {
      reader.close();
    }

    String receiptData = sb.toString();
    JsonParser parser = new JsonParser();
    JsonObject inputjson = parser.parse(receiptData).getAsJsonObject();
    System.out.println(inputjson);
    JsonObject data = parser.parse(inputjson.get("data").getAsString()).getAsJsonObject();
    System.out.println(data);
    JsonArray items = parser.parse(inputjson.get("items").getAsString()).getAsJsonArray();
    System.out.println(items);
    
    DealItem bestItem = null;
    for (int i = 0; i < items.size(); i++) {
      JsonObject item = items.get(i).getAsJsonObject();
      String itemName = item.get("name").getAsString();
      try {
        GroceryNameProcessor processor = new GroceryNameProcessor();
        itemName = processor.process(itemName);
      } catch (Exception e) {
        System.out.println("error");
      }

      GroceryDataReader groceryReader = new GroceryDataReader();
      bestItem = groceryReader.readFile(itemName.toLowerCase());
    }

    if (bestItem == null) {
      response.setContentType("text/plain");
      response.getWriter().println("no deal found");
    }
    else {
      Gson gson = new Gson();
      String responseJson = gson.toJson(bestItem);
      response.setContentType("application/json");
      response.getWriter().println(responseJson);
    }
  }
}
