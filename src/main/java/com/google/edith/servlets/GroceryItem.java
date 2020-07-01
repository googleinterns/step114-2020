package com.google.sps.servlets;

/** Represents an item on a receipt */
public final class GroceryItem {
  
  private final String itemName;
  private final double itemPrice;
  private final Date date;

  public GroceryItem(String itemName, double itemPrice) {
    this.itemName = itemName;
    this.itemPrice = itemPrice;
    this.date = new Date();
  }

  public String getName() {
    return itemName;
  }

  public String getPrice() {
    return itemPrice;
  }

  public Date getDate() {
    return date;
  }
}
