package com.google.sps.servlets;

/** Represents a customer's receipt. */
public final class Receipt {
  
  public static List<GroceryItem> groceryList = new ArrayList<GroceryItem>();
  int total;
  Date date;

  public Receipt(List<GroceryItem> items) {
    this.groceryList = items;
    total = sumReceipt();
    date = new Date();
  }

  public int getTotal() {
    return total;
  }

  public Date getDate() {
    return date;
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
