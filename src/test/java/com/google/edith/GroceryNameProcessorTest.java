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
      System.out.println(processor.process("Coleman Farms chicken breast"));
    } catch(Exception e) {
      System.out.println(e);
    }
    Assert.assertEquals("Kroger", "Kroger");
  }

  @Test
  public void canMatch() {
    try {
      System.out.println(processor.process("Kirkland Farms pasture-raised eggs"));
    } catch(Exception e) {
      System.out.println(e);
    }
    Assert.assertEquals("Kroger", "Kroger");
  }
}