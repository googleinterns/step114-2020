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

  public void readFile_itemNameInCsv_returnsCheapestItem() throws IOException {
    cheapestItem = groceryReader.readFile("Apple Juice");
    Assert.assertEquals("Kroger", cheapestItem.getStore());
  }

  @Test
  public void readFile_itemNotInCsv_returnsNull() throws IOException {
    cheapestItem = groceryReader.readFile("no deal");
    Assert.assertEquals(null, cheapestItem);
  }

  @Test
  public void readFile_itemWithMissingData_returnsCheapestItem() throws IOException {
    cheapestItem = groceryReader.readFile("Coconut Milk");
    Assert.assertEquals("Trader Joe's", cheapestItem.getStore());
  }
}
