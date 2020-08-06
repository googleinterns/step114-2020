package com.google.edith;

import com.google.cloud.language.v1.LanguageServiceClient;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class GroceryNameProcessorTest {
  private GroceryNameProcessor processor;
  private String result;

  @Mock LanguageServiceClient languageServiceClient;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    result = "";
    processor = Mockito.mock(GroceryNameProcessor.class);
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
