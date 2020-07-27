package com.google.edith;

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
  public void instantiates() {
    Assert.assertTrue(reader instanceof ShelfDataReader);
  }

  @Test
  public void readFile_inputStringInJsonFile_returnsShelfLifeString() {
    Assert.assertEquals("1.0 2.0 Weeks", reader.readFile("Buttermilk"));
  }

  @Test
  public void readFile_inputStringNotInFile_returnsNoShelfLifeData() {
    Assert.assertEquals("no shelf life data found", reader.readFile(""));
  }

  @Test
  public void handlesDifferentCapitalization() {
    Assert.assertEquals("1.0 2.0 Weeks", reader.readFile("buttermilk"));
  }
}
