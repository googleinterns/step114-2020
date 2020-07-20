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
  private String result;

  @Before
  public void setUp() {
    result = "";
    processor = new GroceryNameProcessor();
  }

  @Test
  public void canGetDealIfExists() {
    try {
      result = processor.process("Coleman Farms chicken breast");
    } catch(Exception e) {
      System.out.println(e);
    }
    Assert.assertEquals(result, "chicken breast");
  }

  @Test
  public void confusingInput() {
    try {
      result = processor.process("SPCIS RED PEPPER <+");
    } catch(Exception e) {
      System.out.println(e);
    }
    Assert.assertEquals(result, "red pepper");
  }

  @Test
  public void canMatch() {
    try {
      result = processor.process("Kirkland Farms pasture-raised eggs");
    } catch(Exception e) {
      System.out.println(e);
    }
    Assert.assertEquals(result, "eggs");
  }
}
