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
    cheapestItem = groceryReader.readFile("Apple Juice");
    Assert.assertEquals("Kroger", cheapestItem.getStore());
  }

  @Test
  public void readFile_itemNotInCsv_returnsNull() throws IOException {
    cheapestItem = groceryReader.readFile("no deal");
    Assert.assertEquals("no deal found", cheapestItem.getStore());
  }

  @Test
  public void readFile_itemWithMissingData_returnsCheapestItem() throws IOException {
    cheapestItem = groceryReader.readFile("Coconut Milk");
    Assert.assertEquals("Trader Joe's", cheapestItem.getStore());
  }

  @Test
  public void readFile_itemHasDealNotDate_returnsDeal() throws IOException {
    cheapestItem = groceryReader.readFile("bread crumbs");
    Assert.assertEquals("Aldi", cheapestItem.getStore());
  }

  @Test
  public void readFile_itemHasDealNotDate_hasNoExpiration() throws IOException {
    cheapestItem = groceryReader.readFile("bread crumbs");
    Assert.assertEquals("no shelf life data found", cheapestItem.getExpiration());
  }

  @Test
  public void readFile_itemHasDateNotDeal_hasNoDeal() throws IOException {
    cheapestItem = groceryReader.readFile("tortillas");
    Assert.assertEquals("no deal found", cheapestItem.getStore());
  }

  @Test
  public void readFile_itemHasDateNotDeal_returnsDate() throws IOException {
    cheapestItem = groceryReader.readFile("tortillas");
    Assert.assertEquals("3.0 Months", cheapestItem.getExpiration());
  }
}
