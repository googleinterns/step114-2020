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
import com.google.common.collect.ImmutableList;
import com.google.cloud.language.v1.AnalyzeEntitiesRequest;
import com.google.cloud.language.v1.AnalyzeEntitiesResponse;
import com.google.cloud.language.v1.Entity;
import com.google.cloud.language.v1.EntityMention;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import com.google.cloud.language.v1.AnalyzeEntitiesResponse.Builder;

@RunWith(JUnit4.class)
public class GroceryNameProcessorTest {
  private GroceryNameProcessor processor;
  private String result;

  @Before
  public void setUp() throws IOException {
    MockitoAnnotations.initMocks(this);
    GroceryNameProcessor processor = Mockito.mock(GroceryNameProcessor.class);
    result = "";
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
