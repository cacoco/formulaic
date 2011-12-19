package io.angstrom.util;

import java.util.Stack;

public enum Operator {
    ADDITION("+"),
    SUBTRACTION("-"),
    MULTIPLICATION("*"),
    DIVISION("/");

    private String symbol;

    Operator(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static boolean isOperator(final String symbol) {
        Operator[] v = Operator.values();
        for (Operator op : v) {
            if (op.getSymbol().equals(symbol)) { return true; }
        }
        return false;
    }

    public static Operator getBySymbol(final String symbol) {
        Operator[] v = Operator.values();
        for (Operator op : v) {
            if (op.getSymbol().equals(symbol)) { return op; }
        }
        throw new IllegalArgumentException("Unrecognized operation=" + symbol);
    }

    public static Float compute(final Operator operator, Stack<Float> args) {
        Float result = 0f;
        switch (operator) {
            case ADDITION:
                while (!args.empty()) {
                    result = result + args.pop();
                }
                break;
            case SUBTRACTION:
                Float subtractor = args.pop();
                while (!args.empty()) {
                    result = subtractor - args.pop();
                    subtractor = result;
                }
                break;
            case MULTIPLICATION:
                Float multiple = args.pop();
                while (!args.empty()) {
                    result = multiple * args.pop();
                    multiple = result;
                }
                break;
            case DIVISION:
                // assumes only 2 args...
                try {
                    Float numerator = args.pop();
                    Float divisor = args.pop();
                    result = numerator/divisor;
                } catch (Exception e) {
                    throw new IllegalArgumentException("Unable to perform division. Expected 2 arguments.");
                }

                break;
            default:
                throw new IllegalArgumentException("Unrecognized operation=" + operator);
        }
        return result;
    }
}
