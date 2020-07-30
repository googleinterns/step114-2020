package com.google.edith;

import com.google.common.collect.ImmutableList;
import com.google.edith.DealItem.Store;
import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Processes data file of product prices and sizes at different stores and returns the best value
 * item.
 */
public final class GroceryDataReader {

  private static final Store ALDI = Store.ALDI;
  private static final Store KROGER = Store.KROGER;
  private static final Store TRADER_JOES = Store.TRADER_JOES;
  private static final Store PUBLIX = Store.PUBLIX;
  private static final Store WALMART = Store.WALMART;
  private static final Store NO_STORE = Store.NO_STORE;
  private static final Store[] STORES = {ALDI, KROGER, TRADER_JOES, PUBLIX, WALMART};

  /**
   * Finds the specified product in the file and puts the data into DealItem objects to be handled.
   */
  public DealItem readFile(String itemName) throws IOException {
    URL csvResource = getClass().getClassLoader().getResource("grocerydata.csv");
    File groceryDataFile = new File(csvResource.getFile());

    CSVReader reader = new CSVReader(new FileReader(groceryDataFile), ',');
    DealItem cheapestItem = null;

    // Should never be null with file grocerydatareader.csv.
    String[] record = null;
    record = reader.readNext();
    record = reader.readNext();

    while ((record = reader.readNext()) != null) {
      if (record[0].equals(itemName)) {
        List<DealItem> dealItems = new ArrayList<DealItem>();

        for (int i = 0; i < STORES.length; i++) {
          /**
           * Each store has 3 columns of data, so if i is the store number, the starting index of
           * the data is i*3.
           */
          int storeDataStartColumn = i * 3;
          DealItem item = new DealItem();
          item.setStore(STORES[i]);
          item.setPrice(record[storeDataStartColumn + 1]);
          item.setWeight(record[storeDataStartColumn + 2]);
          item.setComment(record[storeDataStartColumn + 3]);
          dealItems.add(item);
        }

        cheapestItem = getCheapestItemPerUnit(dealItems);
      }
    }
    reader.close();
    if (cheapestItem == null) {
      cheapestItem = new DealItem();
      cheapestItem.setStore(NO_STORE);
    }
    return cheapestItem;
  }

  /**
   * Gets the $/unit value of each item and returns the item that is cheapest per unit. dealItems
   * will never be empty because this function is only ever called when a product match is found.
   */
  private DealItem getCheapestItemPerUnit(List<DealItem> dealItems) {
    double cheapestValue = 10;
    DealItem cheapestItem = dealItems.get(0);

    for (DealItem item : dealItems) {
      if (item.getUnitPrice() < cheapestValue && item.getUnitPrice() != 0) {
        cheapestValue = item.getUnitPrice();
        cheapestItem = item;
      }
    }
    return cheapestItem;
  }
}
