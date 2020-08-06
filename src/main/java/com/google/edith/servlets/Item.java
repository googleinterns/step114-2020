package com.google.edith.servlets;

import com.google.auto.value.AutoValue;

<<<<<<< HEAD
/** Encapsulate User info and logout url. */

public class Item {
  public String userId;
  public String name;
  public float price;
  public int quantity;
  public String category;
  public String expireDate;

  public Item(String userId, String name, float price, int quantity, String category, String expireDate) {
    this.userId = userId;
    this.name = name;
    this.price = price;
    this.quantity = quantity;
    this.category = category;
    this.expireDate = expireDate;
  }

  @Override
  public boolean equals(Object object) {
    Item item = (Item) object;
    if (this.price == item.getPrice() &&
        this.name.equals(item.getName()) &&
        this.expireDate.equals(item.getExpireDate())) {
          return true;
    }
    return false;
  }
  
  @Override
  public int hashCode() {
    int hashcode = 0;
    hashcode = (int) price*20;
    hashcode += this.name.hashCode();
    return hashcode;
  }

  public String getUserId() {
    return this.userId;
  }

  public String getName() {
    return this.name;
  }

  public float getPrice() {
    return this.price;
=======
/** An Item is anything that is purchased at a grocery store. */
@AutoValue
public abstract class Item {
  public abstract String name();

  public abstract String userId();

  public abstract String category();

  public abstract double price();

  public abstract long quantity();

  /** yyyy-mm-dd format. */
  public abstract String date();

  public abstract String expiration();

  /**
   * Creates an AutoValue Builder for this class.
   *
   * @return AutoValue Builder
   */
  public static Builder builder() {
    return new AutoValue_Item.Builder();
  }

  /** Builder used to set the fields of this class. */
  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setName(String value);

    public abstract Builder setUserId(String value);

    public abstract Builder setCategory(String value);

    public abstract Builder setPrice(double value);

    public abstract Builder setQuantity(long value);

    public abstract Builder setDate(String value);

    public abstract Builder setExpiration(String value);

    public abstract Item build();
>>>>>>> edc7eeb167e91ef7647bcc8fd9533ef56c3a615f
  }

  public int getQuantity() {
    return this.quantity;
  }

  public String getCategory() {
    return this.category;
  }

  public String getExpireDate() {
    return this.expireDate;
  }

  public String toString() {
    return (this.userId + " " + this.name + " " + this.price + " " + this.quantity + " " + this.category);
  }

}
