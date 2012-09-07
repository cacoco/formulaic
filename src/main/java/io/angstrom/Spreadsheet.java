package io.angstrom;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Spreadsheet {
    private Map<String, String> rows = new TreeMap<String, String>();
    private int width, height = 0;

    private Map<String, Float> __memoize = new HashMap<String, Float>();

    protected Spreadsheet(final boolean testOnly, final Map<String, String> rows) {
        this.rows = rows == null ? new TreeMap<String, String>() : rows;
        this.width = 0;
        this.height = 0;
    }

    public Spreadsheet(final List<String> data) throws Exception {
        if (data == null || data.size() < 2) { // SHOULD have at least one row of data past dimension line
            throw new Exception("Invalid data set given.");
        }
        final String dimensions = data.get(0);
        this.width = Integer.parseInt(dimensions.substring(0, dimensions.indexOf(" ")).trim());
        this.height = Integer.parseInt(dimensions.substring(dimensions.indexOf(" ") + 1, dimensions.length()).trim());

        int colCount = 0;
        try {
            // map the cells
            char identifier = 'A'; // assume no more than 26 rows.
            for (int i = 1; i <= (this.width * this.height); i ++) {
                rows.put(String.format("%s%s", identifier, (colCount + 1)), data.get(i));

                colCount++;
                if (colCount == this.width) {
                    // move to next row
                    identifier++;
                    // reset column
                    colCount = 0;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new Exception(String.format("Invalid id data set given, specified height=%s,width=%s", this.height, this.width));
        }
    }

    public void reduce() throws Exception {
        System.out.println(String.format("%s %s", this.width, this.height));
        final Set<String> keys = rows.keySet();
        for (final String key : keys) {
            System.out.println(String.format("%.5f", evaluate(key, rows.get(key), new Stack<String>())));
        }
    }

    protected Float evaluate(final String key, final String line, final Stack<String> visited) throws Exception {
        if (visited.contains(key)) {
            final StringBuilder __sb = new StringBuilder(key);
            for (String s : visited) {
                __sb.append(String.format(" --> %s", s));
            }
            throw new Exception(
                    String.format("Cyclical dependency detected -- we've been here before. Referenced:[%s]", __sb.toString()));
        } else {
            visited.push(key);
        }
        final Stack<Float> __operands = new Stack<Float>();

        final StringTokenizer __tokenizer = new StringTokenizer(line);
        while (__tokenizer.hasMoreTokens()) {
            final String token = __tokenizer.nextToken();
            if ("+".equals(token)) {
                __operands.push(__operands.pop() + __operands.pop());
            } else if ("-".equals(token)) {
                __operands.push(__operands.pop() - __operands.pop());
            } else if ("/".equals(token)) {
                __operands.push(__operands.pop() / __operands.pop());
            } else if ("*".equals(token)) {
                __operands.push(__operands.pop() * __operands.pop());
            } else {
                try {
                    __operands.push(Float.parseFloat(token));
                } catch (NumberFormatException e) {
                    boolean increment = false;
                    boolean decrement = false;
                    String cell = token;
                    // references another cell
                    if (token.endsWith("++")) {
                        increment = true;
                        cell = token.substring(0, token.length() - 2);
                    } else if (token.endsWith("--")) {
                        decrement = true;
                        cell = token.substring(0, token.length() - 2);
                    }
                    // find other cell value, look to see if we've computed it before on a previous run
                    Float value = __memoize.get(cell);
                    if (value == null) {
                        value = evaluate(cell, rows.get(cell), visited);
                        visited.pop(); // we've evaluated the cell visited, pop it off
                        // deal with increment and decrement
                        if (increment) {
                            value = value + 1;
                        } else if (decrement) {
                            value = value - 1;
                        }
                        __memoize.put(cell, value); // remember the value
                    }
                    __operands.push(value);
                }
            }
        }
        return __operands.pop();
    }

    public static void main(String[] args) {
        try {
            final List<String> data = new ArrayList<String>();
            final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String line;
            while ((line = in.readLine()) != null && line.length() != 0) { data.add(line); }

            long start = System.currentTimeMillis();
            final Spreadsheet spreadsheet = new Spreadsheet(data);
            spreadsheet.reduce();
            System.out.println(String.format("time: [%dms]", (System.currentTimeMillis() - start)));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
