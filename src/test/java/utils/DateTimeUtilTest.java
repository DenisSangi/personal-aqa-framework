package utils;

import org.testng.Assert;
import org.testng.annotations.Test;

public class DateTimeUtilTest {

    @Test
    public void testGetTodayDateByFormatWithLocale() {
        String testDateWithLocale = DateTimeUtil.getTodayDateByFormat(DateTimeUtil.DATE_AND_TIME_FORMAT);
        String dateNowRegex = "\\d{4}-[A-Z][a-z]{2}-\\d{2}_\\d{2}-\\d{2}-\\d{2}";
        Assert.assertTrue(testDateWithLocale.matches(dateNowRegex));
    }

    @Test
    public void testGetTodayDateByFormat() {
        String testDateWithLocale = DateTimeUtil.getTodayDateByFormat(DateTimeUtil.DATE_ONLY_FORMAT);
        String dateNowSimpleRegex = "\\d{4}-\\d{2}-\\d{2}";
        Assert.assertTrue(testDateWithLocale.matches(dateNowSimpleRegex));
    }
}