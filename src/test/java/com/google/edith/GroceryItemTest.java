package com.google.sps;

import com.google.sps.servlets.GroceryItem;
import java.util.Date;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

public final class GroceryItemTest {

  private final GroceryItem item = new GroceryItem("bread", 5.6, 3);

  @Test
  public void testGetName() {
    Assert.assertEquals(item.getName(), "bread");
  }
  
  @Test
  public void testGetPrice() {
    Assert.assertEquals(item.getPrice(), new Double(5.6));
  }

  @Test
  public void testGetQuantity() {
    Assert.assertEquals(item.getQuantity(), new Integer(3));
  }

  @Test
  public void dateSetOnInstantiation () {
    Assert.assertTrue(item.getDate() instanceof Date);
  }

  @Test
  public void itemInstantiated() {
    Assert.assertTrue(item instanceof GroceryItem && item != null);
  }
}
