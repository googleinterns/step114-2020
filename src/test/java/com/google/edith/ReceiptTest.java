package com.google.sps;

import com.google.sps.servlets.GroceryItem;
import com.google.sps.servlets.Receipt;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

public final class ReceiptTest {

  private final GroceryItem item1 = new GroceryItem("bread", 5.6, 3);
  private final GroceryItem item2 = new GroceryItem("cheese", 7.7, 2);
  private final GroceryItem item3 = new GroceryItem("milk", 3.4, 1);
  private final List<GroceryItem> list = new ArrayList<GroceryItem>();
  private static Receipt receipt;

  @Before
  public void setUp() {
    list.add(item1);
    list.add(item2);
    list.add(item3);
    receipt = new Receipt(list);
  }

  @Test
  public void testTotalCalculation() {
    Assert.assertEquals(receipt.getTotal(), 35.60, .01);
  }

  @Test
  public void receiptContainsItems() {
    Assert.assertEquals(receipt.getItems(), list);
  }

  @Test
  public void dateSetOnInstantiation () {
    Assert.assertTrue(receipt.getDate() instanceof Date);
  }

  @Test
  public void receiptInstantiated() {
    Assert.assertTrue(receipt instanceof Receipt && receipt != null);
  }
}
