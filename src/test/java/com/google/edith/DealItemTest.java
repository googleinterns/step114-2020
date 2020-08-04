package com.google.edith;

import com.google.edith.DealItem.Store;
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
  public void instantiate_dealItem_isSuccessful() {
    Assert.assertTrue(dealItem instanceof DealItem);
  }

  @Test
  public void setStore_setsStoreValueToArgument() {
    dealItem.setStore(Store.WALMART);
    Assert.assertEquals("Walmart", dealItem.getStore());
  }

  @Test
  public void setPrice_validStringInput_setsPriceToDecimalValue() {
    dealItem.setPrice("$25.5");
    Assert.assertEquals(25.5, dealItem.getPrice(), .01);
  } 

  @Test
  public void setPrice_emptyStringInput_setsPriceToZero() {
    dealItem.setPrice("");
    Assert.assertEquals(0.0, dealItem.getPrice(), .01);
  }

  @Test
  public void setPrice_randomString_setsPriceToZero() {
    // Make sure unexpected data doesn't cause an error.
    dealItem.setPrice("bad data");
    Assert.assertEquals(0.0, dealItem.getPrice(), .01);
  }

  @Test
  public void setWeight_validStringInput_setsWeightToDecimalValue() {
    dealItem.setWeight("15 fl oz");
    Assert.assertEquals(15.0, dealItem.getWeight(), .01);
  }

  @Test
  public void setWeight_unitsOnly_setsWeightToOne() {
    dealItem.setWeight("per fl oz");
    Assert.assertEquals(1.0, dealItem.getWeight(), .01);
  }

  @Test
  public void setWeight_randomString_setsWeightToZero() {
    // Make sure unexpected data doesn't cause an error.
    dealItem.setWeight("bad data");
    Assert.assertEquals(0.0, dealItem.getWeight(), .01);
  }

  @Test
  public void setComment_stringInput_storesCommentValue() {
    dealItem.setComment("good deal");
    Assert.assertEquals("good deal", dealItem.getComment());
  }

  @Test
  public void getUnitPrice_validPriceandWeight_calculatesUnitPrice() {
    dealItem.setPrice("$15.0");
    dealItem.setWeight("3 oz");
    Assert.assertEquals(5.0, dealItem.getUnitPrice(), .01);
  }

  @Test
  public void getUnitPrice_nonDecimalWeight_getsUnitValueNoDivisionByZero() {
    // Make sure there are no division by 0 errors.
    dealItem.setPrice("$15.0");
    dealItem.setWeight("bad data");
    Assert.assertEquals(0, dealItem.getUnitPrice(), .01);
  }

  @Test
  public void setExpiration_stringWithShelfLifeData_setsExpirationCorrectly() {
    dealItem.setExpirationTime("1.0 2.0 Weeks");
    Assert.assertEquals("1.0 Weeks", dealItem.getExpirationTime());
  }

  @Test
  public void setExpiration_randomString_setsExpirationNotFound() {
    dealItem.setExpirationTime("NO_EXPIRATION");
    Assert.assertEquals("NO_EXPIRATION", dealItem.getExpirationTime());
  }
}
