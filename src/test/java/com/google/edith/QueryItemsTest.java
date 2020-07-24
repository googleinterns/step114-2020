 
package com.google.edith;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class QueryItemsTest {

  private Item receiptItem;
  private Item[] items;
  private Receipt receipt;
  private QueryItems query;

  @Before
  public void setUp() {
    receiptItem = Item.builder()
            .setUserId("185804764220139124118")
            .setName("Apple Juice")
            .setPrice((float) 5.99)
            .setQuantity(1)
            .setCategory("unknown category")
            .setExpireDate("6.0 Days")
            .build();
    items = new Item[1];
    items[0] = receiptItem;
    receipt = new Receipt("185804764220139124118", "whole Foods", "16/07/2020", "Receipt", "L2dzL2VkaXRoLXJlY2VpcHRzL1NMY1gwX1VZczduVlBJaFBPV3dkY2c", 0, items);
    query = new QueryItems();
  }

  @Test
  public void findExpiredItems_expiredItem_displaysExpiredItem() {
    Assert.assertTrue(query.findExpiredItems(receipt).contains("Apple Juice"));
  }
}
