package com.google.edith.servlets;

/**
 * A pairing beteween a String {@code date}, a Sunday, and {@code total} which is a trailing total
 * from the last 6 days (Monday - Sunday).
 */
public final class WeekInfo {
  private String date; // yyyy-mm-dd format
  private String total;

  public WeekInfo(String date, String total) {
    this.date = date;
    this.total = total;
  }

  /**
   * Evaluates if two {@code WeekInfo} objects are equal based on their (@code date} and {@code
   * total} values.
   * @param other another {@code WeekInfo} object
   * @return true if both, their string and total fields are equal
   */
  public boolean equals(WeekInfo other) {
    return this.date.equals(other.date) && this.total.equals(other.total);
  }
}
