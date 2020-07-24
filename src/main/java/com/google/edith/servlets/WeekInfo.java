package com.google.edith.servlets;

public class WeekInfo {
    private String date;
    private String total;

    public WeekInfo(String date, String total) {
      this.date = date;
      this.total = total;
    }

    public boolean equals(WeekInfo other) {
      return this.date.equals(other.date) && this.total.equals(other.total);
    }
}