package com.google.edith;

/** 
  * Represents items of the same type from
  * different stores in order to compare them. 
  */

public final class DealItem {
  private String store;
  private double price;
  private double weight;
  private String comment;
  private double unitPrice;
  private String expiration;

  public void setStore(String store) {
    this.store = store;
  }
  
  /**
    * Price values from grocerydata.csv come in two main types:
    * 'N/A', '', and '?' are invalid type, and a dollar value to
    * two decimal points with a $ sign in front of it is valid type.
    * When invalid input is detected, the default price is 0.0.
    * 
    * ex: Input price "?" -> this.price = 0.0
    * ex: input price "$5.67" -> this.price = 5.67
    */
  public void setPrice(String price) {
    if (price.equals("N/A") || price.isEmpty() || price.equals("?")) {
      this.price = 0.0;
      return;
    }
    String result = price.substring(1, price.length());
    try {
      this.price = (double) Double.parseDouble(result);
    } catch (NumberFormatException e) {
      this.price = 0.0;
    }
  }

  /**
    * Weight values from grocerydata.csv come in three main types:
    * '' is invalid type, 'dozen' 'per' and 'head' are salvageable
    * types, as their meaning can be interpreted, and any input that
    * starts with a number and ends with a weight unit is valid type.
    *
    * ex: Inputs of 'per' or 'head' indicate that the price given is
    *     for one unit of that item. Therefore, setting the weight to
    *     one would let the value algorithm still work.
    *     'per lb' -> this.weight = 1.0
    * ex: Input weight '64 fl oz' -> this.weight = 64.0
    */
  public void setWeight(String weight) {
    if (weight.isEmpty()) {
      this.weight = 0.0;
    }
    else if (weight.equals("dozen")) {
      this.weight = 12.0;
    }
    else if (weight.length() == 2) {
      this.weight = 0.0;
    }
    else if (weight.substring(0, 3).equals("per")) {
      this.weight = 1.0;
    }
    else if (weight.equals("head")) {
      this.weight = 1.0;
    }
    else {
      String num = "";
      for (int i = 0; i < weight.length(); i++) {
        if (weight.charAt(i) == ' ') {
          num = weight.substring(0, i);
          break;
        }
      }
      try {
        this.weight = (double) Double.parseDouble(num);
      } catch (NumberFormatException e) {
        this.weight = 0.0;
      }
    }
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public void setExpiration(String expiration) {
    if (expiration.equals("no shelf life data found")) {
      this.expiration = expiration;
      return;
    }

    String[] expirationPieces = expiration.split(" ");
    List<Double> range = new ArrayList<Double>();
    String timeMeasurement = "";

    for (String expirationPiece: expirationPieces) {
      try {
        range.add(Double.parseDouble(expirationPiece));
      } catch (NumberFormatException e) {
        timeMeasurement = expirationPiece;
      }
    }

    Double min = new Double(0);
    if (range.size() >= 1){
      min = range.get(0);
    }
    for (Double time: range) {
      if (time < min) {
        min = time;
      }
    }
    this.expiration = min + " " + timeMeasurement;
  }

  public double getValue() {

  /**
    * Grocery items are compared by determining unit
    * value, which is price divided by weight. This is
    * because the items at each different store are the
    * same type, but differ by weight and price. Using the
    * unit price allows us to compare them more equally.
    * Since invalid input for weight leads to a default weight
    * of 0.0, we need to prevent divide by 0 values as well.
    */
  public double getUnitPrice() {
>>>>>>> LivDev5
    if (weight == 0 || price == 0) {
      this.unitPrice = 0.0;
    }
    else {
      this.unitPrice = this.price/this.weight;
    }
    return unitPrice;
  }

  public String getStore() {
    return store;
  }

  public Double getPrice() {
    return price;
  }

  public Double getWeight() {
    return weight;
  }

  public String getComment() {
    return comment;
  }

  public String getExpiration() {
    return expiration;
  }
}
