package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/** Handles storing receipts and items in datastore. */
public final class ReceiptDataService {

  private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  // Store a receipt in Datastore.
  public void storeReceipt(List<GroceryItem> groceryList) {
    Receipt receipt = new Receipt(groceryList);
    Entity receiptEntity = new Entity("Receipt");
    Key receiptKey = receiptEntity.getKey();
    receiptEntity.setProperty("totalPrice", receipt.getTotal());
    receiptEntity.setProperty("date", receipt.getDate());

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
}
