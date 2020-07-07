package com.google.edith.servlets;

public final class DealItem {
  private static String store;
  private static Double price;
  private static Double weight;
  private static String comment;
  private static double value;
  private static String unit;

  public DealItem() {
    
  }

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
        measure = weight.substring(i+1, weight.length());
      }
    }
    this.weight = Double.parseDouble(num);
    findValue(measure);
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  private void findValue(String measure) {
    this.value = this.price/this.weight;
    this.unit = "$/" + measure;
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

  public double getValue() {
    return value;
  }

  public String getValueStatement() {
    return Double.toString(value) + " " + unit;
  }
}
