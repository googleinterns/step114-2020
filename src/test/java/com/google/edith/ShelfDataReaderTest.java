package com.google.edith;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ShelfDataReaderTest {
<<<<<<< HEAD

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
=======
  @Test
  public void readFile_inputStringInJsonFile_returnsShelfLifeString() {
    Assert.assertEquals("1.0 2.0 Weeks", ShelfDataReader.readFile("Buttermilk"));
>>>>>>> edc7eeb167e91ef7647bcc8fd9533ef56c3a615f
  }

  @Test
  public void readFile_inputStringNotInFile_returnsNoShelfLifeData() {
    Assert.assertEquals("NO_EXPIRATION", ShelfDataReader.readFile("nothing"));
  }

  @Test
  public void handlesDifferentCapitalization() {
<<<<<<< HEAD
    Assert.assertEquals("1.0 2.0 Weeks", reader.readFile("buttermilk"));
=======
    Assert.assertEquals("1.0 2.0 Weeks", ShelfDataReader.readFile("buttermilk"));
>>>>>>> edc7eeb167e91ef7647bcc8fd9533ef56c3a615f
  }
}
