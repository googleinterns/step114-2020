package com.google.edith;

/** Represents items of the same type from
  * different stores in order to compare them. */
public final class DealItem {
  private String store;
  private Double price;
  private Double weight;
  private String comment;
  private double value;

  public void setStore(String store) {
    this.store = store;
  }

  public void setPrice(String price) {
    if (price.equals("N/A") || price.equals("") || price.equals("?")) {
      this.price = new Double(0);
    }
    else if (price.charAt(0)=='$') {
      String result = price.substring(1, price.length());
      this.price = Double.parseDouble(result);
    }
  }

  public void setWeight(String weight) {
    if (weight.equals("")) {
      this.weight = new Double(0);
    }
    String num = "";
    String measure = "";
    for (int i=0; i<weight.length(); i++) {
      if (weight.charAt(i)==' ') {
        num = weight.substring(0, i);
        break;
      }
    }
    this.weight = Double.parseDouble(num);
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public double getValue() {
    if (weight == 0 || price == 0) {
      this.value = 0;
    }
    else {
      this.value = this.price/this.weight;
    }
    return value;
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
}
