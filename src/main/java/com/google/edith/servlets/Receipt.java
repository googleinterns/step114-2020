package com.google.sps.servlets;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/** Represents a customer's receipt. */
public final class Receipt {
  
  public static List<GroceryItem> groceryList = new ArrayList<GroceryItem>();
  double total;
  Date date;

  public Receipt(List<GroceryItem> items) {
    this.groceryList = items;
    total = sumReceipt();
    date = new Date();
  }

  public double getTotal() {
    return total;
  }

  public Date getDate() {
    return date;
  }

  public List<GroceryItem> getItems() {
    return groceryList;
  }

  // Calculate total spending on shopping trip.
  private Double sumReceipt() {
    double total = 0;
    for (GroceryItem item: groceryList) {
      total += item.getPrice()*item.getQuantity();
    }
    return total;
  }
}
