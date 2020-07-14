package com.google.edith;

import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/** Processes data file of product prices and sizes at different
  * stores and returns the best value item. */
public class GroceryDataReader {
    
  /** Finds the specified product in the file and puts the
    * data into DealItem objects to be handled. */
  public DealItem readFile(String itemName) throws IOException {
    URL csvresource = getClass().getClassLoader().getResource("grocerydata.csv");
    File groceryDataFile = new File(csvresource.getFile());

    CSVReader reader = new CSVReader(new FileReader(groceryDataFile), ',');
    DealItem bestItem = null;

    String[] record = null;
    record = reader.readNext();
    record = reader.readNext();

	while ((record = reader.readNext()) != null) {
      if (record[0].equals(itemName)) {
        ShelfDataReader shelfReader = new ShelfDataReader();
        String expirationTime = shelfReader.readFile(itemName);

        List<DealItem> dealItems = new ArrayList<DealItem>();

	    DealItem item1 = new DealItem();
        item1.setStore("Aldi");
	    item1.setPrice(record[1]);
	    item1.setWeight(record[2]);
	    item1.setComment(record[3]);
        item1.setExpiration(expirationTime);
        dealItems.add(item1);

        DealItem item2 = new DealItem();
        item2.setStore("Kroger");
	    item2.setPrice(record[4]);
	    item2.setWeight(record[5]);
	    item2.setComment(record[6]);
        item2.setExpiration(expirationTime);
        dealItems.add(item2);

        DealItem item3 = new DealItem();
        item3.setStore("Trader Joe's");
	    item3.setPrice(record[7]);
	    item3.setWeight(record[8]);
	    item3.setComment(record[9]);
        item3.setExpiration(expirationTime);
        dealItems.add(item3);

        DealItem item4 = new DealItem();
        item4.setStore("Publix");
	    item4.setPrice(record[10]);
	    item4.setWeight(record[11]);
	    item4.setComment(record[12]);
        item4.setExpiration(expirationTime);
        dealItems.add(item4);

        DealItem item5 = new DealItem();
        item5.setStore("Walmart");
	    item5.setPrice(record[13]);
	    item5.setWeight(record[14]);
	    item5.setComment(record[15]);
        item5.setExpiration(expirationTime);
        dealItems.add(item5);

        bestItem = getBestDeal(dealItems);
      }
	}
		
	reader.close();
    return bestItem;
  }

  /** Gets the $/unit value of each item and
    * returns the best deal. */
  private DealItem getBestDeal(List<DealItem> dealItems) {
    double bestVal = 10;
    DealItem bestItem = dealItems.get(0);

    for (DealItem item: dealItems) {
      if (item.getValue() < bestVal && item.getValue() != 0) {
        bestVal = item.getValue();
        bestItem = item;
      }
    }

    return bestItem;
  }
}
