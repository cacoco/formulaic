package io.angstrom;

import io.angstrom.util.Cell;
import io.angstrom.util.Operator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;

public class SpreadsheetTest {

    public static Deque<String> getDequeArray(final String expression) {
        Deque<String> deque = new ArrayDeque<String>();
        final StringTokenizer tokenizer = new StringTokenizer(expression);
        while (tokenizer.hasMoreTokens()) {
            deque.push(tokenizer.nextToken());
        }
        return deque;
    }

    public static void log(final Operator operator, final String expression, final Float value) {
        System.out.println(String.format("Operator=%s, Expression='%s' => computed value=%.5f",  operator, expression, value));
    }

    @Test
    public void testEvaluate() throws Exception {
        final Spreadsheet spreadsheet = new Spreadsheet(true, null);
        final String s = "4 5";
        final Operator op = Operator.ADDITION;
        Deque<String> expression = getDequeArray(s);
        Float value = spreadsheet.evaluate(new HashSet<String>(), op, expression);
        log(op, s, value);
        Assert.assertEquals(value, 9f);
    }

    @Test
    public void testEvaluate1() throws Exception {
        final Spreadsheet spreadsheet = new Spreadsheet(true, null);
        final String s = "100 5";
        final Operator op = Operator.DIVISION;
        Deque<String> expression = getDequeArray(s);
        Float value = spreadsheet.evaluate(new HashSet<String>(), op, expression);
        log(op, s, value);
        Assert.assertEquals(value, 20f);
    }

    @Test
    public void testEvaluate2() throws Exception {
        final Spreadsheet spreadsheet = new Spreadsheet(true, null);
        final String s = "9 10";
        final Operator op = Operator.SUBTRACTION;
        Deque<String> expression = getDequeArray(s);
        Float value = spreadsheet.evaluate(new HashSet<String>(), op, expression);
        log(op, s, value);
        Assert.assertEquals(value, -1f);
    }

    @Test
    public void testEvaluate3() throws Exception {
        final Spreadsheet spreadsheet = new Spreadsheet(true, null);
        final String s = "10 9 1";
        final Operator op = Operator.SUBTRACTION;
        Deque<String> expression = getDequeArray(s);
        Float value = spreadsheet.evaluate(new HashSet<String>(), op, expression);
        log(op, s, value);
        Assert.assertEquals(value, 0f);
    }

    @Test
    public void testEvaluate4() throws Exception {
        final Spreadsheet spreadsheet = new Spreadsheet(true, null);
        final String s = "4 5";
        final Operator op = Operator.MULTIPLICATION;
        Deque<String> expression = getDequeArray(s);
        Float value = spreadsheet.evaluate(new HashSet<String>(), op, expression);
        log(op, s, value);
        Assert.assertEquals(value, 20f);
    }

    @Test
    public void testEvaluate5() throws Exception {
        final Spreadsheet spreadsheet = new Spreadsheet(true, null);
        final String s = "2 4 5";
        final Operator op = Operator.MULTIPLICATION;
        Deque<String> expression = getDequeArray(s);
        Float value = spreadsheet.evaluate(new HashSet<String>(), op, expression);
        log(op, s, value);
        Assert.assertEquals(value, 40f);
    }

    @Test
    public void testEvaluate6() throws Exception {
        final Spreadsheet spreadsheet = new Spreadsheet(true, null);
        final String s = "4++ 5 6";
        final Operator op = Operator.ADDITION;
        Deque<String> expression = getDequeArray(s);
        Float value = spreadsheet.evaluate(new HashSet<String>(), op, expression);
        log(op, s, value);
        Assert.assertEquals(value, 16f);
    }

    @Test
    public void testEvaluate7() throws Exception {
        final Spreadsheet spreadsheet = new Spreadsheet(true, null);
        final String s = "4++ 5 6";
        final Operator op = Operator.MULTIPLICATION;
        Deque<String> expression = getDequeArray(s);
        Float value = spreadsheet.evaluate(new HashSet<String>(), op, expression);
        log(op, s, value);
        Assert.assertEquals(value, 150f);
    }

    @Test
    public void testEvaluate8() throws Exception {
        final Spreadsheet spreadsheet = new Spreadsheet(true, null);
        final String s = "4 5-- 6";
        final Operator op = Operator.ADDITION;
        Deque<String> expression = getDequeArray(s);
        Float value = spreadsheet.evaluate(new HashSet<String>(), op, expression);
        log(op, s, value);
        Assert.assertEquals(value, 14f);
    }

    @Test
    public void testEvaluate9() throws Exception {
        final Spreadsheet spreadsheet = new Spreadsheet(true, null);
        final String s = "4 5-- 6";
        final Operator op = Operator.MULTIPLICATION;
        Deque<String> expression = getDequeArray(s);
        Float value = spreadsheet.evaluate(new HashSet<String>(), op, expression);
        log(op, s, value);
        Assert.assertEquals(value, 96f);
    }

    @Test
    public void testGetNodeValue() throws Exception {
        final Cell A1 = new Cell("A2");
        final Cell A2 = new Cell("4 5 *");

        final Map<String, Cell> rows = new TreeMap<String, Cell>();
        rows.put("A1", A1);
        rows.put("A2", A2);

        final Spreadsheet spreadsheet = new Spreadsheet(true, rows);
        Float value = spreadsheet.getNodeValue("A1", null);
        Assert.assertEquals(value, 20f);
    }

    @Test
    public void testGetNodeValue1() throws Exception {
        final Cell A1 = new Cell("A2");
        final Cell A2 = new Cell("4 5 *");

        final Map<String, Cell> rows = new TreeMap<String, Cell>();
        rows.put("A1", A1);
        rows.put("A2", A2);

        final Spreadsheet spreadsheet = new Spreadsheet(true, rows);
        Float value = spreadsheet.getNodeValue("A2", null);
        Assert.assertEquals(value, 20f);
    }

    @Test
    public void testGetNodeValue2() throws Exception {
        final Cell A1 = new Cell("A2--");
        final Cell A2 = new Cell("4 5 *");

        final Map<String, Cell> rows = new TreeMap<String, Cell>();
        rows.put("A1", A1);
        rows.put("A2", A2);

        final Spreadsheet spreadsheet = new Spreadsheet(true, rows);
        Float value = spreadsheet.getNodeValue("A1", null);
        Assert.assertEquals(value, 19f);
    }

    @Test
    public void testGetNodeValue3() throws Exception {
        final Cell A1 = new Cell("A2++");
        final Cell A2 = new Cell("4 5 *");

        final Map<String, Cell> rows = new TreeMap<String, Cell>();
        rows.put("A1", A1);
        rows.put("A2", A2);

        final Spreadsheet spreadsheet = new Spreadsheet(true, rows);
        Float value = spreadsheet.getNodeValue("A1", null);
        Assert.assertEquals(value, 21f);
    }

    @Test
    public void testGetNodeValue4() throws Exception {
        final Cell A1 = new Cell("A2");
        final Cell A2 = new Cell("A1");

        final Map<String, Cell> rows = new TreeMap<String, Cell>();
        rows.put("A1", A1);
        rows.put("A2", A2);
        final Spreadsheet spreadsheet = new Spreadsheet(true, rows);

        try {
            Float value = spreadsheet.getNodeValue("A1", null);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertNotNull(e);
        }
    }
}
