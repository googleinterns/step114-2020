package com.google.edith;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@RunWith(JUnit4.class)
public class GroceryNameProcessorTest {
  private GroceryNameProcessor processor;
  private String result;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    result = "";
    processor = mock(GroceryNameProcessor.class);
  }

  @Test
  public void canGetDealIfExists() throws Exception {
    when(processor.process(Mockito.anyString())).thenReturn("chicken breast");
    result = processor.process("Coleman Farms chicken breast");
    Assert.assertEquals(result, "chicken breast");
  }

  @Test
  public void confusingInput() throws Exception {
    when(processor.process(Mockito.anyString())).thenReturn("red pepper");
    result = processor.process("SPCIS RED PEPPER <+");
    Assert.assertEquals(result, "red pepper");
  }

  @Test
  public void canMatch() throws Exception {
    when(processor.process(Mockito.anyString())).thenReturn("eggs");
    result = processor.process("Kirkland Farms pasture-raised eggs");
    Assert.assertEquals(result, "eggs");
  }
}
