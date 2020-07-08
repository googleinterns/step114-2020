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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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

/** Servlet that takes the user input from the receipt
  * form and uses it to retrieve the best deal. */
@WebServlet("/receipt-deals")
public class DealsServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    InputStream params = request.getInputStream();
    JsonParser jsonParser = new JsonParser();
    JsonObject jsonObject = (JsonObject)jsonParser.parse(
      new InputStreamReader(params, "utf-8"));
    String itemName = jsonObject.get("itemName").getAsString();

    GroceryDataReader reader = new GroceryDataReader();
    DealItem bestItem = reader.readFile(itemName);
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
