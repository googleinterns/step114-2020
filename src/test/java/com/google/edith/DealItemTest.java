package com.google.edith;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DealItemTest {

  private DealItem dealItem;

  @Before
  public void setUp() {
    dealItem = new DealItem();
  }

  @Test
  public void dealItemInstantiates() {
    Assert.assertTrue(dealItem instanceof DealItem);
  }

  @Test
  public void canSetStore() {
    dealItem.setStore("Walmart");
    Assert.assertEquals(dealItem.getStore(), "Walmart");
  }

  @Test
  public void priceParsedCorrectly() {
    dealItem.setPrice("$25.5");
    Assert.assertEquals(dealItem.getPrice(), new Double(25.5));
  }

  @Test
  public void badPriceDataHandled() {
    dealItem.setPrice("");
    Assert.assertEquals(dealItem.getPrice(), new Double(0));
  }

  @Test
  public void weightParsedCorrectly() {
    dealItem.setWeight("15 fl oz");
    Assert.assertEquals(dealItem.getWeight(), new Double(15.0));
  }

  @Test
  public void canSetComment() {
    dealItem.setComment("good deal");
    Assert.assertEquals(dealItem.getComment(), "good deal");
  }

  @Test
  public void canFindValue() {
    dealItem.setPrice("$15.0");
    dealItem.setWeight("3 oz");
    Assert.assertEquals(dealItem.getValue(), 5.0, .01);
  }
}
