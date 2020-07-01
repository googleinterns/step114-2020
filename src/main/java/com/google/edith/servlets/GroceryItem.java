package com.google.sps.servlets;

/** Represents an item on a receipt */
public final class GroceryItem {
  
  private final String itemName;
  private final double itemPrice;
  private final int itemQuantity;
  private final Date date;

  public GroceryItem(String itemName, double itemPrice) {
    this.itemName = itemName;
    this.itemPrice = itemPrice;
    this.itemQuantity = itemQuantity;
    this.date = new Date();
  }

  public String getName() {
    return itemName;
  }

  public double getPrice() {
    return itemPrice;
  }

  public double getQuantity() {
    return itemQuantity;
  }

  public Date getDate() {
    return date;
  }

}
