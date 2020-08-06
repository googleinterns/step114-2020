package com.google.edith;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.UserService;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.edith.servlets.Item;
import com.google.edith.servlets.Receipt;
import com.google.gson.Gson;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Finds the items on past receipts that have expired by the time of the user's next shopping trip.
 */
public class QueryItems {

  private static final String SPLIT = " ";
  private static final String WEEKS = "weeks";
  private static final String MONTHS = "months";
  private final DatastoreService datastore;
  private UserService userService;

  public QueryItems(DatastoreService datastore, UserService userService) {
    this.datastore = datastore;
    this.userService = userService;
  }

  public String findExpiredItems() {
    List<Receipt> receipts = queryReceipts();
    Set<Item> itemsToBuy = new HashSet<Item>();

    for (Receipt receipt : receipts) {
      double daysPassedSinceReceiptStored =
          findTimePassedSinceReceiptStored(receipt.getDate());
      List<Item> items = Arrays.asList(receipt.getItems());

      for (Item item : items) {
        String expiration = item.expiration();
        if (expiration.equals("NO_EXPIRATION")) {
          continue;
        }

        List<String> expirationNumberAndUnit = Splitter.on(SPLIT).splitToList(expiration);
        double number = 0;
        String unit = "";

        for (String expirationPiece : expirationNumberAndUnit) {
          try {
            number = (double) Double.parseDouble(expirationPiece);
          } catch (NumberFormatException e) {
            unit = expirationPiece;
          }
        }
        if (unit.toLowerCase().equals(WEEKS)) {
          number = number * 7;
        } else if (unit.toLowerCase().equals(MONTHS)) {
          number = number * 30;
        }

        if (number < daysPassedSinceReceiptStored) {
          itemsToBuy.add(item);
        }
      }
    }

    Gson gson = new Gson();
    return gson.toJson(itemsToBuy);
  }

  /**
   * Determines the number of days between when the receipt was stored in datastore and the current
   * time.
   */
  private int findTimePassedSinceReceiptStored(String receiptDateString) {
    Date currentDate = new Date();
    Date receiptDate;
    try {
      receiptDate = new SimpleDateFormat("yyyy-MM-dd").parse(receiptDateString);
    } catch (ParseException e) {
      receiptDate = new Date();
    }
    long timePassedSinceReceipt = currentDate.getTime() - receiptDate.getTime();
    int daysPassedSinceReceipt = (int) TimeUnit.MILLISECONDS.toDays(timePassedSinceReceipt);
    return daysPassedSinceReceipt;
  }

  private ImmutableList<Receipt> queryReceipts() {
    String id = userService.getCurrentUser().getUserId();
    Query query =
        new Query("Receipt")
            .setFilter(new Query.FilterPredicate("userId", Query.FilterOperator.EQUAL, id));
    PreparedQuery receiptResults = datastore.prepare(query);
    List<Entity> receiptEntities = receiptResults.asList(FetchOptions.Builder.withLimit(3));
    List<Receipt> receipts = new ArrayList<>();

    for (Entity receiptEntity : receiptEntities) {
      Key entityKey = receiptEntity.getKey();
      Query itemQuery = new Query("Item", entityKey);
      PreparedQuery results = datastore.prepare(itemQuery);
      List<Entity> itemEntities = results.asList(FetchOptions.Builder.withLimit(Integer.MAX_VALUE));
      ImmutableList<Item> items = createItemObjects(itemEntities);
      String userId = (String) receiptEntity.getProperty("userId");
      String storeName = (String) receiptEntity.getProperty("storeName");
      String date = (String) receiptEntity.getProperty("date");
      String name = (String) receiptEntity.getProperty("name");
      String fileUrl = (String) receiptEntity.getProperty("fileUrl");
      float totalPrice = (float) ((double) receiptEntity.getProperty("price"));
      Receipt receipt = new Receipt(userId, storeName, date, name, fileUrl, totalPrice, items.toArray(new Item[0]));
      receipts.add(receipt);
    }
    return ImmutableList.copyOf(receipts);
  }

  /**
   * Creates an array of Item objects from entites of kind Item found in the datastore.
   *
   * @param entities - entities of kind Item found in datastore.
   * @return Item[] - array of Item objects.
   */
  public ImmutableList<Item> createItemObjects(List<Entity> entities) {
    List<Item> itemsList = new ArrayList<>();

    for (Entity entity : entities) {
      String userId = (String) entity.getProperty("userId");
      String itemName = (String) entity.getProperty("name");
      float price = (float) ((double) entity.getProperty("price"));
      int quantity = (int) ((long) entity.getProperty("quantity"));
      String category = (String) entity.getProperty("category");
      String expireDate = (String) entity.getProperty("date");

      Item receiptItem =
          Item.builder()
              .setUserId(userId)
              .setName(itemName)
              .setPrice(price)
              .setQuantity(quantity)
              .setDate("date")
              .setCategory(category)
              .setExpiration(expireDate)
              .build();
      itemsList.add(receiptItem);
    }
    return ImmutableList.copyOf(itemsList);
    //return itemsList.toArray(new Item[0]);
  }
}
