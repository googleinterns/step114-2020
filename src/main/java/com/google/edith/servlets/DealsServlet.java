package com.google.edith;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that takes the user input from the receipt form and uses it to retrieve the best deal.
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
    JsonObject json = (JsonObject) parser.parse(receiptData);
    String itemName = json.get("itemName").getAsString();

    GroceryDataReader groceryReader = new GroceryDataReader();
    DealItem cheapestItem = groceryReader.readFile(itemName);

    if (cheapestItem == null) {
      response.setContentType("text/plain");
      response.getWriter().println("no deal found");
    } else {
      System.out.println(cheapestItem.getStore());
      response.getWriter().println(cheapestItem.getStore());
      response.getWriter().println(cheapestItem.getPrice());
      response.getWriter().println(cheapestItem.getComment());
    }
  }
}
