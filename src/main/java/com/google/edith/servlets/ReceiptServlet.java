package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* Servlet that returns some example content.
*/
@WebServlet("/receipt")
public class DealsServlet extends HttpServlet {

  private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  private static Map<String, Double> receiptMap = new HashMap<String, Double>();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
    Double total = sumReceipt();
    Entity receiptEntity = new Entity("Receipt");
    Key receiptKey = receiptEntity.getKey();
    receiptEntity.setProperty("totalPrice", total);
    receiptEntity.setProperty("date", new Date());
    for (String item: receiptMap.keySet()) {
      //also pass receipt key when we figure out how to generate
      storeItem(item, receiptKey);
    }
    datastore.put(receiptEntity);
  }

  // Store a grocery item in Datastore.
  private void storeItem(String item, Key receiptKey) {
    Entity itemEntity = new Entity("Item");
    itemEntity.setProperty("name", item);
    itemEntity.setProperty("price", receiptMap.get(item));
    itemEntity.setProperty("date", new Date());
    itemEntity.setProperty("receiptId", receiptKey);
    datastore.put(itemEntity);
  }

  // Calculate total spending on shopping trip.
  private Double sumReceipt() {
    Double total = 0;
    for (String item: receiptMap) {
      total = Double.sum(total, receiptMap.get(item));
    }
    return total;
  }
}