package com.google.edith;

import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/** 
  * Processes data file of product prices and sizes at different
  * stores and returns the best value item. 
  */
public final class GroceryDataReader {

    private static final String ALDI = "Aldi";
    private static final String KROGER = "Kroger";
    private static final String TRADERJOES = "Trader Joe's";
    private static final String PUBLIX = "Publix";
    private static final String WALMART = "Walmart";
    
  /**
    * Finds the specified product in the file and puts the
    * data into DealItem objects to be handled. 
    */
  public DealItem readFile(String itemName) throws IOException {
    URL csvResource = getClass().getClassLoader().getResource("grocerydata.csv");
    File groceryDataFile = new File(csvResource.getFile());

    CSVReader reader = new CSVReader(new FileReader(groceryDataFile), ',');
    DealItem cheapestItem = null;

    String[] record = null;
    record = reader.readNext();
    record = reader.readNext();

	while ((record = reader.readNext()) != null) {
      if (record[0].equals(itemName)) {
        List<DealItem> dealItems = new ArrayList<DealItem>();

        DealItem item1 = new DealItem();
        item1.setStore(ALDI);
        item1.setPrice(record[1]);
        item1.setWeight(record[2]);
        item1.setComment(record[3]);
        dealItems.add(item1);

        DealItem item2 = new DealItem();
        item2.setStore(KROGER);
        item2.setPrice(record[4]);
        item2.setWeight(record[5]);
        item2.setComment(record[6]);
        dealItems.add(item2);

        DealItem item3 = new DealItem();
        item3.setStore(TRADERJOES);
        item3.setPrice(record[7]);
        item3.setWeight(record[8]);
        item3.setComment(record[9]);
        dealItems.add(item3);

        DealItem item4 = new DealItem();
        item4.setStore(PUBLIX);
        item4.setPrice(record[10]);
        item4.setWeight(record[11]);
        item4.setComment(record[12]);
        dealItems.add(item4);

        DealItem item5 = new DealItem();
        item5.setStore(WALMART);
        item5.setPrice(record[13]);
        item5.setWeight(record[14]);
        item5.setComment(record[15]);
        dealItems.add(item5);

        cheapestItem = getCheapestItemPerUnit(dealItems);
      }
    }
		
	reader.close();
    return cheapestItem;
  }

  /** 
    * Gets the $/unit value of each item and
    * returns the item that is cheapest per unit. 
    * dealItems will never be empty because this
    * function is only ever called when a product match
    * is found.
    */
  private DealItem getCheapestItemPerUnit(List<DealItem> dealItems) {
    double cheapestValue = 10;
    DealItem cheapestItem = dealItems.get(0);

    for (DealItem item: dealItems) {
      if (item.getUnitPrice() < cheapestValue && item.getUnitPrice() != 0) {
        cheapestValue = item.getUnitPrice();
        cheapestItem = item;
      }
    }

    return cheapestItem;
  }
}
