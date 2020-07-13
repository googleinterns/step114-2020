package com.google.edith;

import java.io.IOException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ShelfDataReaderTest {
  
  private ShelfDataReader reader;

  @Before
  public void setUp() {
    reader = new ShelfDataReader();
  }
  @Test
  public void doesParse() {
    reader.readFile("Lamb");
    Assert.assertEquals("", "");
  }
}