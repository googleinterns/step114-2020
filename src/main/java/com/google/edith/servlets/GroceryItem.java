package com.google.sps.servlets;

import java.util.Date;

/** Represents an item on a receipt */
public final class GroceryItem {
  
  private final String itemName;
  private final Double itemPrice;
  private final Integer itemQuantity;
  private final Date date;

  public GroceryItem(String itemName, Double itemPrice, Integer itemQuantity) {
    this.itemName = itemName;
    this.itemPrice = itemPrice;
    this.itemQuantity = itemQuantity;
    this.date = new Date();
  }

  public String getName() {
    return itemName;
  }

  public Double getPrice() {
    return itemPrice;
  }

  public Integer getQuantity() {
    return itemQuantity;
  }

  public Date getDate() {
    return date;
  }
}
