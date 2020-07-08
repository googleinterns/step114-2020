package com.google.edith.servlets;

/** Represents items of the same type from
  * different stores in order to compare them. */
public final class DealItem {
  private static String store;
  private static Double price;
  private static Integer weight;
  private static String comment;
  private static double value;

  public void setStore(String store) {
    this.store = store;
  }

  public void setPrice(String price) {
    String result = price;
    if (price.charAt(0)=='$') {
      result = price.substring(1, price.length());
    }
    this.price = Double.parseDouble(result);
  }

  public void setWeight(String weight) {
    String num = "";
    String measure = "";
    for (int i=0; i<weight.length(); i++) {
      if (weight.charAt(i)==' ') {
        num = weight.substring(0, i);
        break;
      }
    }
    this.weight = Integer.parseInt(num);
    findValue();
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  private void findValue() {
    this.value = this.price/this.weight;
  }

  public String getStore() {
    return store;
  }

  public Double getPrice() {
    return price;
  }

  public Integer getWeight() {
    return weight;
  }

  public String getComment() {
    return comment;
  }

  public double getValue() {
    return value;
  }
}
