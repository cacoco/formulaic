package io.angstrom;

import io.angstrom.util.Cell;
import io.angstrom.util.Operator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Spreadsheet {
    private Map<String, Cell> rows = new TreeMap<String, Cell>();
    private int width, height = 0;

    protected Spreadsheet(final boolean testOnly, final Map<String, Cell> rows) {
        this.rows = rows == null ? new TreeMap<String, Cell>() : rows;
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
                final Cell cell = new Cell(data.get(i));
                rows.put(String.format("%s%s", identifier, (colCount+1)), cell);

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
        for (String key : keys) {
            final Cell cell = rows.get(key);
            if (cell.getValue() == null) {
                cell.setValue(getNodeValue(key, null));
                rows.put(key, cell);
            }
            System.out.println(String.format("%.5f", cell.getValue()));
        }
    }

    protected Float getNodeValue(final String key, Set<String> visited) throws Exception {
        if (visited == null) { visited = new HashSet<String>(); }
        final Cell cell = rows.get(key);
        if (cell == null) { throw new Exception("Invalid key=" + key); }

        if (visited.contains(key) && cell.getValue() == null) {
            System.out.println(String.format("Visited chain: %s", visited));
            throw new Exception(String.format("Cyclical dependency detected -- we've been here before. Referenced:[%s]", key));
        }
        visited.add(key);

        if (cell.getValue() != null) { return cell.getValue(); }
        final Deque<String> expression = cell.getExpression();
        Operator operator = null;
        if (Operator.isOperator(expression.peek())) {
            operator = Operator.getBySymbol(expression.pop());
        }

        return evaluate(visited, operator, expression);
    }

    protected Float evaluate(Set<String> visited, Operator operator, Deque<String> args) throws Exception {
        final Stack<Float> operands = new Stack<Float>();

        String key;
        while ((key = args.poll()) != null) {
            boolean increment = false;
            boolean decrement = false;
            // will only deal with postfix
            if (key.endsWith("--")) {
                key = key.substring(0, (key.length() - 2));
                decrement = true;
            } else if (key.endsWith("++")) {
                key = key.substring(0, (key.length() - 2));
                increment = true;
            }
            if (Operator.isOperator(key)) {
                // only pop the next two args for the operator
                Deque<String> innerOp = new ArrayDeque<String>();
                innerOp.add(args.pop());
                innerOp.add(args.pop());
                operands.add(evaluate(visited, Operator.getBySymbol(key), innerOp));
            } else if (rows.containsKey(key)) {
                Cell referenced = rows.get(key);
                Float value = getNodeValue(key, visited);
                if (referenced.getValue() == null) {
                    referenced.setValue(value);
                    rows.put(key, referenced);
                }
                if (decrement) { value--; }
                if (increment) { value++; }
                operands.add(value);
            } else {
                Float value = Float.parseFloat(key);
                if (decrement) { value--; }
                if (increment) { value++; }
                operands.add(value);
            }
        }
        if (operator == null) {
            return operands.get(0); // should only be one as there is no operator
        }
        return Operator.compute(operator, operands);
    }

    public static void main(String[] args) {
        try {
            final List<String> data = new ArrayList<String>();
            final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String line;
            while ((line = in.readLine()) != null && line.length() != 0) {
                data.add(line);
            }

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
