package com.google.sps.servlets;

 /** Used to hold the properties of an item Entity in datastore. */
public final class Item {
  double price; 
  long quantity;  
//   String Name; 
//   String userId;
  String date; 
//   String category; 
  
  public Item(double price, long quantity, String date) {
    this.price = price;
    this.quantity = quantity;
    this.date = date;
  }
}