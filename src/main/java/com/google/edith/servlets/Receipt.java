package com.google.sps.servlets;

/** Represents a customer's receipt. */
public final class Receipt {
  
  public static List<GroceryItem> groceryList = new ArrayList<GroceryItem>();

  public Receipt(List<GroceryItem> items) {
    this.groceryList = items;
  }
}
