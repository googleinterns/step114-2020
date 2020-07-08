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

  @Before
  public void setUp() {
    groceryReader = new GroceryDataReader();
  }

  @Test
  public void dealItemInstantiates() {
    Assert.assertTrue(groceryReader instanceof GroceryDataReader);
  }

  @Test
  public void canGetDealIfExists() {
    DealItem bestItem = new DealItem();
    try {
      bestItem = groceryReader.readFile("Apple Juice");
    } catch (IOException e) {
      System.err.println();
    }
    Assert.assertEquals(bestItem.getStore(), "Kroger");
  }

  @Test
  public void nullReturnedIfNoDeal() {
    DealItem bestItem = new DealItem();
    try {
      bestItem = groceryReader.readFile("bread");
    } catch (IOException e) {
      System.err.println();
    }
    Assert.assertEquals(bestItem, null);
  }
}
