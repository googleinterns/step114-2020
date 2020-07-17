package com.google.edith.servlets;

 /** Used to hold the properties of an item Entity in datastore. */
public final class Item {
  // TODO (malachibre): Add name, user, category, and receiptId fields.
  // TODO (malachibre): Create a builder for this class inclduing checks 
  //                    for valid assignments.
  double price; 
  long quantity;  
  String date; 
 
  public Item(double price, long quantity, String date) {
    this.price = price;
    this.quantity = quantity;
    this.date = date;
  }
}
