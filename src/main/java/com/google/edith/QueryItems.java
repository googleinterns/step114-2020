package com.google.edith;

public class QueryItems {
  public String findExpiredItems(Receipt receipt) {
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date currentDate = new Date();
    String receiptDateString = receipt.getDate();
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

        if (unit.equals("Weeks")) {
          number = number * 7;
        }
        else if (unit.equals("Months")) {
          number = number * 30;
        }

        if (number > daysPassedSinceReceipt) {
          itemsToBuy(item);
        }
      }
    }
  }
}