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
  private static List<GroceryItem> groceryList = new ArrayList<GroceryItem>();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    storeReceipt();
    getDeals();
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String itemName = String.parseString(request.getParameter("itemName"));
    Double itemPrice = Double.parseDouble(request.getParameter("itemPrice"));
    Integer itemQuantity = Integer.parseInt(request.getParameter("itemQuantity"));
    GroceryItem item = new GroceryItem(itemName, itemPrice, itemQuantity);
    groceryList.add(item);
  }

  // Store a receipt in Datastore.
  private void storeReceipt() {
    Double total = sumReceipt();
    Entity receiptEntity = new Entity("Receipt");
    Key receiptKey = receiptEntity.getKey();
    receiptEntity.setProperty("totalPrice", total);
    receiptEntity.setProperty("date", new Date());
    for (GroceryItem item: groceryList) {
      storeItem(item, receiptKey);
    }
    datastore.put(receiptEntity);
  }

  // Store a grocery item in Datastore.
  private void storeItem(GroceryItem item, Key receiptKey) {
    Entity itemEntity = new Entity("Item");
    itemEntity.setProperty("name", item.getName());
    itemEntity.setProperty("price", item.getPrice());
    itemEntity.setProperty("date", item.getDate());
    itemEntity.setProperty("receiptId", receiptKey);
    datastore.put(itemEntity);
  }

  // Calculate total spending on shopping trip.
  private Double sumReceipt() {
    double total = 0;
    for (GroceryItem item: groceryList) {
      total += item.getPrice();
    }
    return total;
  }
}