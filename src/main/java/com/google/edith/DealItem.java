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
    else {
      String result = price.substring(1, price.length());
      try {
        this.price = Double.parseDouble(result);
      } catch (NumberFormatException e) {
        this.price = new Double(0);
      }
    }
  }

  public void setWeight(String weight) {
    if (weight.equals("")) {
      this.weight = new Double(0);
    }
    else if (weight.equals("dozen")) {
      this.weight = new Double(12);
    }
    else if (weight.substring(0, 3).equals("per")) {
      this.weight = new Double(1);
    }
    else if (weight.equals("head")) {
      this.weight = new Double(1);
    }
    else {
      String num = "";
      String measure = "";
      for (int i=0; i<weight.length(); i++) {
        if (weight.charAt(i)==' ') {
          num = weight.substring(0, i);
          break;
        }
      }
      try {
        this.weight = Double.parseDouble(num);
      } catch (NumberFormatException e) {
        this.weight = new Double(0);
      }
    }
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
