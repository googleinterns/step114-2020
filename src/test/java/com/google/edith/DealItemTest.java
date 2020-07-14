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
    Assert.assertEquals("Walmart", dealItem.getStore());
  }

  @Test
  public void priceParsedCorrectly() {
    dealItem.setPrice("$25.5");
    Assert.assertEquals(new Double(25.5), dealItem.getPrice());
  }

  @Test
  public void badPriceDataHandled() {
    dealItem.setPrice("");
    Assert.assertEquals(new Double(0), dealItem.getPrice());
  }

  @Test
  public void parseDoubleErrorHandledPrice() {
    // Make sure unexpected data doesn't cause an error.
    dealItem.setPrice("bad data");
    Assert.assertEquals(new Double(0), dealItem.getPrice());
  }

  @Test
  public void weightParsedCorrectly() {
    dealItem.setWeight("15 fl oz");
    Assert.assertEquals(new Double(15.0), dealItem.getWeight());
  }

  @Test
  public void badWeightHandled() {
    dealItem.setWeight("per fl oz");
    Assert.assertEquals(new Double(1), dealItem.getWeight());
  }

  @Test
  public void parseDoubleErrorHandledWeight() {
    // Make sure unexpected data doesn't cause an error.
    dealItem.setWeight("bad data");
    Assert.assertEquals(new Double(0), dealItem.getWeight());
  }

  @Test
  public void canSetComment() {
    dealItem.setComment("good deal");
    Assert.assertEquals("good deal", dealItem.getComment());
  }

  @Test
  public void canFindValue() {
    dealItem.setPrice("$15.0");
    dealItem.setWeight("3 oz");
    Assert.assertEquals(5.0, dealItem.getValue(), .01);
  }

  @Test
  public void canFindValueWithZeroWeight() {
    // Make sure there are no division by 0 errors.
    dealItem.setPrice("$15.0");
    dealItem.setWeight("bad data");
    Assert.assertEquals(0, dealItem.getValue(), .01);
  }

  @Test
  public void setsExpirationCorrectly() {
    dealItem.setExpiration("1.0 2.0 Weeks ");
    Assert.assertEquals("1.0 Weeks", dealItem.getExpiration());
  }

  @Test
  public void handlesBadExpirationData() {
    dealItem.setExpiration("no shelf life data found");
    Assert.assertEquals("no shelf life data found", dealItem.getExpiration());
  }
}
