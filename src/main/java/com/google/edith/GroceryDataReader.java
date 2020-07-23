package com.google.edith;

import com.google.common.collect.ImmutableList;
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

  private static final String ALDI = "Aldi";
  private static final String KROGER = "Kroger";
  private static final String TRADER_JOES = "Trader Joe's";
  private static final String PUBLIX = "Publix";
  private static final String WALMART = "Walmart";
  private static final ImmutableList<String> STORES =
      ImmutableList.of(ALDI, KROGER, TRADER_JOES, PUBLIX, WALMART);

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

        for (int i = 0; i < STORES.size(); i++) {
          DealItem item = new DealItem();
          item.setStore(STORES.get(i));
          item.setPrice(record[i * 3 + 1]);
          item.setWeight(record[i * 3 + 2]);
          item.setComment(record[i * 3 + 3]);
          dealItems.add(item);
        }

        cheapestItem = getCheapestItemPerUnit(dealItems);
      }
    }
    reader.close();
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
