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
  * Servlet that takes the user input from the receipt
  * form and uses it to retrieve the best deal. 
  */
@WebServlet("/receipt-deals")
public class DealsServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    StringBuilder stringBuilder = new StringBuilder();
    BufferedReader reader = request.getReader();
    try {
      String line;
      while ((line = reader.readLine()) != null) {
        stringBuilder.append(line).append('\n');
      }
    } finally {
      reader.close();
    }

    String receiptData = stringBuilder.toString();
    JsonParser parser = new JsonParser();
    JsonObject json = (JsonObject) parser.parse(receiptData);
    String itemName = json.get("itemName").getAsString();

    GroceryDataReader groceryReader = new GroceryDataReader();
    DealItem bestItem = groceryReader.readFile(itemName);
    response.setContentType("text/plain");
    if (bestItem == null) {
      System.out.println("no deal found");
      response.getWriter().println("no deal found");
    }
    else {
      System.out.println(bestItem.getStore());
      response.getWriter().println(bestItem.getStore());
      response.getWriter().println(bestItem.getPrice());
      response.getWriter().println(bestItem.getComment());
    }
  }
}
