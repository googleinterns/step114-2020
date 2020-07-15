package com.google.edith.servlets;

import com.google.edith.DealItem;
import com.google.edith.GroceryDataReader;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that takes the user input from the receipt
  * form and uses it to retrieve the best deal. */
@WebServlet("/receipt-deals")
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
    JsonObject json = (JsonObject) parser.parse(receiptData);
    String itemName = json.get("itemName").getAsString();

    GroceryDataReader groceryReader = new GroceryDataReader();
    DealItem bestItem = groceryReader.readFile(itemName);

    if (bestItem == null) {
      response.setContentType("text/plain");
      response.getWriter().println("no deal found");
      System.out.println("no deal found");
    }
    else {
      Gson gson = new Gson();
      String responseJson = gson.toJson(bestItem);
      response.setContentType("application/json");
      response.getWriter().println(responseJson);
      System.out.println(responseJson);
    }
  }
}
