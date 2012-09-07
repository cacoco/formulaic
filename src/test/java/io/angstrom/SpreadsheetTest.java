package io.angstrom;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;

public class SpreadsheetTest {

    @Test
    public void testEvaluate() throws Exception {
        final Spreadsheet spreadsheet = new Spreadsheet(true, null);
        final String line = "20 100 / 3 +";
        final Float value = spreadsheet.evaluate("A1", line, new Stack<String>());

        Assert.assertEquals(value, 8f);
    }

    @Test
    public void testEvaluate2() throws Exception {
        final List<String> data = new ArrayList<String>();
        data.add("6 2");
        // A
        data.add("A2");
        data.add("B1++");
        data.add("1");
        data.add("4 5 + 3 *");
        data.add("20 100 / 3 +");
        data.add("1002");
        // B
        data.add("2");
        data.add("A3++ 66 + 8 + 4 /");
        data.add("6 3 17 +");
        data.add("22 7 /");
        data.add("1 4 - 8 *");
        data.add("60 60 60 60 *");
        final Spreadsheet spreadsheet = new Spreadsheet(data);

        spreadsheet.reduce();
    }
}
