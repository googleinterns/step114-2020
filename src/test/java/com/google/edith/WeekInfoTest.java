package com.google.edith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.google.edith.servlets.WeekInfo;
import org.junit.Test;

public final class WeekInfoTest {
  public static final WeekInfo WEEK_INFO_1 = new WeekInfo("2020-08-01", "5.00");
  public static final WeekInfo WEEK_INFO_2 = new WeekInfo("2020-08-01", "5.00");
  public static final WeekInfo WEEK_INFO_3 = new WeekInfo("2020-08-02", "5.00");
  public static final WeekInfo WEEK_INFO_4 = new WeekInfo("2020-08-01", "6.00");
  
  @Test
  public void equals_sameValues_returnsTrue() {
    assertTrue(WEEK_INFO_1.equals(WEEK_INFO_2));
  }

  @Test
  public void equals_differentDates_returnsFalse() {
    assertFalse(WEEK_INFO_1.equals(WEEK_INFO_3));
  }

  @Test
  public void equals_differentTotals_returnsFalse() {
    assertFalse(WEEK_INFO_1.equals(WEEK_INFO_4));
  }
}
