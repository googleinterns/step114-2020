package com.google.edith.servlets;

import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GroceryDataReader {
  private static final String CSV_FILE = "grocerydata.csv";
    
  public DealItem readFile(String itemName) throws IOException {

    File f = new File(CSV_FILE);
    String path = f.getAbsolutePath();

    CSVReader reader = new CSVReader(new FileReader(path), ',');
    DealItem bestItem = new DealItem();

	// read line by line
    String[] record = null;
    record = reader.readNext();
    record = reader.readNext();

	while ((record = reader.readNext()) != null) {
      if (record[0].equals(itemName)) {
        List<DealItem> dealItems = new ArrayList<>();

	    DealItem item1 = new DealItem();
	    item1.setStore("Aldi");
	    item1.setPrice(record[1]);
	    item1.setWeight(record[2]);
	    item1.setComment(record[3]);
        dealItems.add(item1);

        DealItem item2 = new DealItem();
	    item2.setStore("Kroger");
	    item2.setPrice(record[4]);
	    item2.setWeight(record[5]);
	    item2.setComment(record[6]);
        dealItems.add(item2);

        DealItem item3 = new DealItem();
	    item3.setStore("Trader Joes");
	    item3.setPrice(record[7]);
	    item3.setWeight(record[8]);
	    item3.setComment(record[9]);
        dealItems.add(item3);

        DealItem item4 = new DealItem();
	    item4.setStore("Publix");
	    item4.setPrice(record[10]);
	    item4.setWeight(record[11]);
	    item4.setComment(record[12]);
        dealItems.add(item4);

        DealItem item5 = new DealItem();
	    item5.setStore("Walmart");
	    item5.setPrice(record[13]);
	    item5.setWeight(record[14]);
	    item5.setComment(record[15]);
        dealItems.add(item5);

        bestItem = getBestDeal(dealItems);
      }
	}
		
	reader.close();
    return bestItem;
  }

  private DealItem getBestDeal(List<DealItem> dealItems) {
    double min = dealItems.get(0).getValue();
    DealItem bestItem = dealItems.get(0);
    for (DealItem item: dealItems) {
      if (item.getValue()<min) {
        min = item.getValue();
        bestItem = item;
      }
    }
    return bestItem;
  }
}
