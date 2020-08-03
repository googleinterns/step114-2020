package com.google.edith.servlets;

import com.google.auto.value.AutoValue;

 /** Used to hold the properties of an item Entity in datastore. */
@AutoValue
public abstract class Item {
  public abstract String name();
  public abstract String userId();
  public abstract String category();
  public abstract double price(); 
  public abstract long quantity();  
  public abstract String date(); 
  public abstract String receiptId();
  public abstract String expiration();
  
  /**
   * Creates an AutoValue Builder for this class.
   * @return AutoValue Builder
   */
  public static Builder builder() {
    return new AutoValue_Item.Builder();
  }

  /**
   * Builder used to set the fields of this class.
   */
  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setName(String value);
    public abstract Builder setUserId(String value);
    public abstract Builder setCategory(String value);
    public abstract Builder setPrice(double value); 
    public abstract Builder setQuantity(long value);  
    public abstract Builder setDate(String value); 
    public abstract Builder setReceiptId(String value);
    public abstract Builder setExpiration(String value);
    public abstract Item build();
  }
}