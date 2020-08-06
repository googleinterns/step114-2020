package com.google.edith;

import java.io.IOException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class GroceryDataReaderTest {

  private GroceryDataReader groceryReader;
  private DealItem cheapestItem;

  @Before
  public void setUp() {
    groceryReader = new GroceryDataReader();
    cheapestItem = new DealItem();
  }

  @Test
  public void instantiate_groceryReader_isSuccessful() {
    Assert.assertTrue(groceryReader instanceof GroceryDataReader);
  }

  @Test
  public void readFile_itemNameInCsv_returnsCheapestItem() throws IOException {
    cheapestItem = groceryReader.readFile("Apple Juice", "5.6");
    Assert.assertEquals("Kroger", cheapestItem.getStore());
  }

  @Test
  public void readFile_itemNotInCsv_returnsNull() throws IOException {
    cheapestItem = groceryReader.readFile("no deal");
    Assert.assertEquals("NO_STORE", cheapestItem.getStore());
  }

  @Test
  public void readFile_itemWithMissingData_returnsCheapestItem() throws IOException {
    cheapestItem = groceryReader.readFile("Coconut Milk", "6.6");
    Assert.assertEquals("Trader Joe's", cheapestItem.getStore());
  }

  @Test
  public void readFile_itemHasDealNotDate_returnsDeal() throws IOException {
    cheapestItem = groceryReader.readFile("bread crumbs", "5.4");
    Assert.assertEquals("Aldi", cheapestItem.getStore());
  }

  @Test
  public void readFile_itemHasDealNotDate_hasNoExpiration() throws IOException {
    cheapestItem = groceryReader.readFile("bread crumbs", "6.7");
    Assert.assertEquals("no shelf life data found", cheapestItem.getExpiration());
  }

  @Test
  public void readFile_itemHasDateNotDeal_hasNoDeal() throws IOException {
    cheapestItem = groceryReader.readFile("tortillas", "7.7");
    Assert.assertEquals("no deal found", cheapestItem.getStore());
  }

  @Test
  public void readFile_itemHasDateNotDeal_returnsDate() throws IOException {
    cheapestItem = groceryReader.readFile("tortillas", "8.9");
    Assert.assertEquals("3.0 Months", cheapestItem.getExpiration());
  }

  @Test
  public void readFile_itemTooCheapForDeal_returnsNoDeal() throws IOException {
    cheapestItem = groceryReader.readFile("Apple Juice", "0.5");
    Assert.assertEquals("no deal found", cheapestItem.getStore());
  }
}
