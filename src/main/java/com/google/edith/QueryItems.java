package com.google.edith;

import com.google.gson.Gson;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Finds the items on past receipts that have expired by the time
 * of the user's next shopping trip.
 */
public class QueryItems {
  public String findExpiredItems(Receipt pastReceipt) {
    Date currentDate = new Date();
    String receiptDateString = pastReceipt.getDate();
    Date receiptDate = new SimpleDateFormat("dd/MM/yyyy").parse(receiptDateString); 
    long timePassedSinceReceipt = currentDate.getTime() - receiptDate.getTime();
    int daysPassedSinceReceipt = (int) (timePassedSinceReceipt / 86400000 );
    List<Item> itemsToBuy = new ArrayList<Item>();

    for (Receipt receipt: receipts) {
      Items[] items = receipt.getItems();
      for (Item item: items) {
        String expiration = item.getExpiration();

        if (expiration.equals("no shelf life data found")) {
          continue;
        }

        String[] expirationNumberAndUnit = expiration.split(" ");
        double number = 0;
        String unit = "";

        for (String expirationPiece: expirationNumberAndUnit) {
          try {
            number = (double) Double.parseDouble(expirationPiece);
          } catch(NumberFormatException e) {
            unit = expirationPiece;
          }
        }

        if (unit.toLowerCase().equals("weeks")) {
          number = number * 7;
        }
        else if (unit.toLowerCase().equals("months")) {
          number = number * 30;
        }

        if (number > daysPassedSinceReceipt) {
          itemsToBuy.add(item);
        }
      }
    }

    Gson gson = new Gson();
    return gson.toJson(itemsToBuy);
  }
}