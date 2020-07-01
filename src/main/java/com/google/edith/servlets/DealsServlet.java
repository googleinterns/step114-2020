package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that fetches deals from the Discount API */
@WebServlet("/deals")
public class DealsServlet extends HttpServlet {

  private static final String apiKey = "BKFuTzor";
  private static final String apiSite = "https://api.discount.com/v2/deals?api-key=%s";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

  }

  // Fetch deals from Discount API.
  /**
  private void getDeals() {
    String dealsApi = String.format(apiSite, apiKey);
    // sending get request
    URL url = new URL(dealsApi);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("get");
    connection.setRequestProperty("Content-Type", "application/json");
    Map<String, String> parameters = new HashMap<String, String>();
    // here's where we need the food category
    // receiving deal info
    StringBuilder content;
    try(BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
      String line;
      content = new StringBuilder();
      while((line = input.readLine()) != null) {
        content.append(line);
        content.append(System.lineSeparator());
      }
    } finally {
      connection.disconnect();
    }
    Gson gson = new Gson();
    String json = gson.toJson(content);
  }*/

}
