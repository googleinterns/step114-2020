package com.google.edith;

import com.google.edith.servlets.Item;
import com.google.edith.servlets.Receipt;
import com.google.gson.Gson;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Finds the items on past receipts that have expired by the time of the user's next shopping trip.
 */
public class QueryItems {
  public String findExpiredItems(Receipt[] pastReceipts) {
    Set<Item> itemsToBuy = new HashSet<Item>();

    for (Receipt receipt : pastReceipts) {
      double daysPassedSinceReceipt = findTimePassed(receipt.getDate());
      Item[] items = receipt.getItems();

      for (Item item : items) {
        String expiration = item.getExpireDate();

        if (expiration.equals("no shelf life data found")) {
          continue;
        }

        String[] expirationNumberAndUnit = expiration.split(" ");
        double number = 0;
        String unit = "";

        for (String expirationPiece : expirationNumberAndUnit) {
          try {
            number = (double) Double.parseDouble(expirationPiece);
          } catch (NumberFormatException e) {
            unit = expirationPiece;
          }
        }
        if (unit.toLowerCase().equals("weeks")) {
          number = number * 7;
        } else if (unit.toLowerCase().equals("months")) {
          number = number * 30;
        }

        if (number < daysPassedSinceReceipt) {
          itemsToBuy.add(item);
        }
      }
    }

    Gson gson = new Gson();
    return gson.toJson(itemsToBuy);
  }

  /**
   * Determines the number of days between when the receipt was stored in datastore
   * and the current time.
   */
  public double findTimePassed(String receiptDateString) {
    Date currentDate = new Date();
    Date receiptDate;
    try {
      receiptDate = new SimpleDateFormat("yyyy-mm-dd").parse(receiptDateString);
    } catch (ParseException e) {
      receiptDate = new Date();
    }
    long timePassedSinceReceipt = currentDate.getTime() - receiptDate.getTime();
    double daysPassedSinceReceipt = (timePassedSinceReceipt / (1000 * 60 * 60 * 24)) / 24;
    return daysPassedSinceReceipt;
  }
}
