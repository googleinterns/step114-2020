package com.google.edith;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ShelfDataReaderTest {
  @Test
  public void readFile_inputStringInJsonFile_returnsShelfLifeString() {
    Assert.assertEquals("1.0 2.0 Weeks", ShelfDataReader.readFile("Buttermilk"));
  }

  @Test
  public void readFile_inputStringNotInFile_returnsNoShelfLifeData() {
    Assert.assertEquals("NO_EXPIRATION", ShelfDataReader.readFile(""));
  }
}
