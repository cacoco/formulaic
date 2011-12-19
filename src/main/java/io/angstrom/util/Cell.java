package io.angstrom.util;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.StringTokenizer;

public class Cell {
    private Deque<String> expression;
    private Float value;

    public Cell(String expression) {
        final StringTokenizer tokenizer = new StringTokenizer(expression);

        this.expression = new ArrayDeque<String>();
        while (tokenizer.hasMoreTokens()) {
            this.expression.push(tokenizer.nextToken());
        }
        if (Character.isDigit(this.expression.peek().charAt(0))) {
            this.value = Float.parseFloat(this.expression.peek());
        }
    }

    public Deque<String> getExpression() {
        return this.expression;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "expression=" + expression +
                ", value=" + value +
                '}';
    }
}
