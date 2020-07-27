package com.google.edith;

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
  public void canGetDealIfExists() throws Exception {
    result = processor.process("Coleman Farms chicken breast");
    Assert.assertEquals(result, "chicken breast");
  }

  @Test
  public void confusingInput() throws Exception {
    result = processor.process("SPCIS RED PEPPER <+");
    Assert.assertEquals(result, "red pepper");
  }

  @Test
  public void canMatch() throws Exception {
    result = processor.process("Kirkland Farms pasture-raised eggs");
    Assert.assertEquals(result, "eggs");
  }
}
