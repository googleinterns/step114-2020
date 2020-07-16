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
  private DealItem bestItem;

  @Before
  public void setUp() {
    groceryReader = new GroceryDataReader();
    bestItem = new DealItem();
  }

  @Test
  public void dealItemInstantiates() {
    Assert.assertTrue(groceryReader instanceof GroceryDataReader);
  }

  @Test
  public void canGetDealIfExists() throws IOException {
    bestItem = groceryReader.readFile("Apple Juice");
    Assert.assertEquals("Kroger", bestItem.getStore());
  }

  @Test
  public void nullReturnedIfNoDeal() throws IOException {
    bestItem = groceryReader.readFile("bread");
    Assert.assertEquals(null, bestItem);
  }

  @Test
  public void getsDealDespiteQuestionableData() throws IOException {
    bestItem = groceryReader.readFile("Coconut Milk");
    Assert.assertEquals("Trader Joe's", bestItem.getStore());
  }
}
