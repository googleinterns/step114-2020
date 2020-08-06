package com.google.edith;

import com.google.edith.servlets.Item;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ItemTest {
  private Item item1;
  private Item item2;
  private Item item3;

  @Before
  public void setUp() {
    item1 =
        Item.builder()
            .setUserId("185804764220139124118")
            .setName("Apple Juice")
            .setPrice((float) 5.99)
            .setQuantity(1)
            .setDate("date")
            .setCategory("unknown category")
            .setExpiration("unknown date")
            .build();
    item2 =
        Item.builder()
            .setUserId("185804764220139124118")
            .setName("Apple Juice")
            .setPrice((float) 5.99)
            .setQuantity(1)
            .setDate("date")
            .setCategory("unknown category")
            .setExpiration("unknown date")
            .build();
    item3 =
        Item.builder()
            .setUserId("185804764220139124118")
            .setName("Peanut Butter")
            .setPrice((float) 5.99)
            .setQuantity(1)
            .setDate("date")
            .setCategory("unknown category")
            .setExpiration("unknown date")
            .build();
  }

  @Test
  public void item_isInstanceOf_itemClass() {
    Assert.assertTrue(item1 instanceof Item);
  }

  @Test
  public void equals_itemsAreEqual_returnsTrue() {
    Assert.assertTrue(item1.equals(item2));
  }

  @Test
  public void equals_itemsAreUnequal_returnsFalse() {
    Assert.assertFalse(item1.equals(item3));
  }

  @Test
  public void hashCode_forItem_isDeterminedCorrectly() {
    int hashCode = item1.userId().hashCode() * item1.name().hashCode();
    Assert.assertEquals(hashCode, item1.hashCode());
  }

  @Test
  public void hashCode_forEqualItems_isEqual() {
    Assert.assertEquals(item1.hashCode(), item2.hashCode());
  }

  @Test
  public void hashCode_forUnequalItems_isUnequal() {
    Assert.assertNotEquals(item1.hashCode(), item3.hashCode());
  }
}
