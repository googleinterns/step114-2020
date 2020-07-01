package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* Servlet that returns some example content.
*/
@WebServlet("/deals")
public class DealsServlet extends HttpServlet {

  private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  private static final String apiKey = "BKFuTzor";
  private static final String apiSite = "https://api.discount.com/v2/deals?api-key=%s";
  private static Map<String, Double> receiptMap = new HashMap<String, Double>();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    sumReceipt();
    storeReceipt();
    getDeals();
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String itemName = String.parseString(request.getParameter("itemName"));
    Double itemPrice = Double.parseDouble(request.getParameter("itemPrice"));
    receiptMap.put(itemName, itemPrice);
  }

  // Store a receipt in Datastore.
  private void storeReceipt() {
    Entity receiptEntity = new Entity("Receipt");
    receiptEntity.setProperty()
    for (String item: receiptMap.keySet()) {
      //also pass receipt key when we figure out how to generate
      storeItem(item);
    }
    datastore.put(receiptEntity);
  }

  // Store a grocery item in Datastore.
  private void storeItem(String item) {
    Entity itemEntity = new Entity("Item");
    itemEntity.setProperty("name", item);
    itemEntity.setProperty("price", receiptMap.get(item));
    datastore.put(itemEntity);
  }

  // Fetch deals from Discount API.
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
  }

  // Calculate total spending on shopping trip.
  private void sumReceipt() {
    
  }
}
