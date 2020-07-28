package com.google.edith;

import com.google.edith.servlets.Item;
import com.google.edith.servlets.Receipt;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class QueryItemsTest {

  private Item receiptItem;
  private Item[] items;
  private Receipt receipt;
  private QueryItems query;

  @Test
  public void findExpiredItems_expiredItem_displaysExpiredItem() {
    receiptItem =
        new Item("185804764220139124118",
            "Apple Juice",
            (float) 5.99,
            1,
            "unknown category",
            "6.0 Days");
    items = new Item[1];
    items[0] = receiptItem;
    receipt =
        new Receipt(
            "185804764220139124118",
            "whole Foods",
            "2020-07-13",
            "Receipt",
            "L2dzL2VkaXRoLXJlY2VpcHRzL1NMY1gwX1VZczduVlBJaFBPV3dkY2c",
            0,
            items);
    query = new QueryItems();
    Assert.assertTrue(query.findExpiredItems(receipt).contains("Apple Juice"));
  }

  @Test
  public void findExpiredItems_notExpiredItem_doesntDisplayExpiredItem() {
    Item receiptItem =
        new Item("185804764220139124118",
            "Peanut Butter",
            (float) 5.99,
            1,
            "unknown category",
            "20.0 Days");
    items = new Item[1];
    items[0] = receiptItem;
    receipt =
        new Receipt(
            "185804764220139124118",
            "whole Foods",
            "2020-07-17",
            "Receipt",
            "L2dzL2VkaXRoLXJlY2VpcHRzL1NMY1gwX1VZczduVlBJaFBPV3dkY2c",
            0,
            items);
    query = new QueryItems();
    Assert.assertTrue(query.findExpiredItems(receipt).equals("[]"));
  }

  @Test
  public void findExpiredItems_expiredItemInWeeks_displaysExpiredItem() {
    Item receiptItem =
        new Item("185804764220139124118",
            "Peanut Butter",
            (float) 5.99,
            1,
            "unknown category",
            "1.0 Weeks");
    items = new Item[1];
    items[0] = receiptItem;
    receipt =
        new Receipt(
            "185804764220139124118",
            "whole Foods",
            "2020-07-17",
            "Receipt",
            "L2dzL2VkaXRoLXJlY2VpcHRzL1NMY1gwX1VZczduVlBJaFBPV3dkY2c",
            0,
            items);
    query = new QueryItems();
    Assert.assertTrue(query.findExpiredItems(receipt).contains("Peanut Butter"));
  }
}
