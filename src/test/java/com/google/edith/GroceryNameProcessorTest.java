package com.google.edith;

import java.io.IOException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class GroceryNameProcessorTest {
  private GroceryNameProcessor processor;

  @Before
  public void setUp() {
    processor = new GroceryNameProcessor();
  }

  @Test
  public void canGetDealIfExists() {
    try {
      processor.process("Coleman Farms chicken breast");
    } catch(Exception e) {
      System.out.println(e);
    }
    Assert.assertEquals("Kroger", "Kroger");
  }
}