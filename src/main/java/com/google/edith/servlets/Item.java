package com.google.edith.servlets;

import com.google.auto.value.AutoValue;

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
  }

  @Override
  public boolean equals(Object item) {
    Item itemObject = (Item) item;
    if (itemObject.name().equals(this.name()) &&
        itemObject.userId().equals(this.userId())) {
      return true;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return this.userId().hashCode() * 11;
  }
}
