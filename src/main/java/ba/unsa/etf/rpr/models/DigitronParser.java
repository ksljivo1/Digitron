package ba.unsa.etf.rpr.models;

import ba.unsa.etf.rpr.Tokens;
import javafx.util.Pair;

import java.util.List;
import java.util.Stack;

public class DigitronParser {
    List<Pair<Tokens, String>> tokens;

    public DigitronParser(List<Pair<Tokens, String>> tokens) {
        this.tokens = tokens;
    }

    public Stack<Pair<Tokens, String>> evaluate() throws Exception {
        Stack<Pair<Tokens, String>> numberTokens = new Stack<>();
        Stack<Pair<Tokens, String>> operationTokens = new Stack<>();
        Double rez = 0.;
        int i = 0;
        while(!isEOFToken(tokens.get(i))) {
            Pair<Tokens, String> current = tokens.get(i);
            if(numberTokens.size() >= 2 && !operationTokens.empty() && hasHigherPrecedence(operationTokens.peek())) {
                double op2 = Double.parseDouble(numberTokens.pop().getValue());
                double op1 = Double.parseDouble(numberTokens.pop().getValue());
                String op = operationTokens.pop().getValue();
                rez = evaluateBinary(op1, op2, op);
                if(rez.isNaN() || rez.isInfinite()) throw new Exception("ERROR: Division by zero");
                numberTokens.push(new Pair<>(Tokens.DOUBLE, String.valueOf(rez)));
                i--;
            }
            else if(numberTokens.size() >= 2 && !operationTokens.empty() && !hasHigherPrecedence(operationTokens.peek())) {
                int t = i;
                if(t + 1 < tokens.size() && hasHigherPrecedence(tokens.get(t))) {
                    numberTokens.push(tokens.get(t + 1));
                    operationTokens.push(tokens.get(t));
                    i = t + 1;
                }
                else {
                    double op2 = Double.parseDouble(numberTokens.pop().getValue());
                    double op1 = Double.parseDouble(numberTokens.pop().getValue());
                    String op = operationTokens.pop().getValue();
                    rez = evaluateBinary(op1, op2, op);
                    numberTokens.push(new Pair<>(Tokens.DOUBLE, String.valueOf(rez)));
                    i--;
                }
            }
            else if(isOperationToken(current)) operationTokens.push(current);
            else if(isNumberToken(current)) numberTokens.push(current);
            else;
            i++;
        }
        while(!operationTokens.empty()) {
            double op2 = Double.parseDouble(numberTokens.pop().getValue());
            double op1 = Double.parseDouble(numberTokens.pop().getValue());
            String op = operationTokens.pop().getValue();
            rez = evaluateBinary(op1, op2, op);
            numberTokens.push(new Pair<>(Tokens.DOUBLE, String.valueOf(rez)));
        }
        return numberTokens;
    }

    private static boolean isNumberToken(Pair<Tokens, String> token) {
        return token.getKey() == Tokens.DOUBLE;
    }

    private static boolean isEOFToken(Pair<Tokens, String> token) {
        return token.getKey() == Tokens.EOF;
    }

    private static boolean isOperationToken(Pair<Tokens, String> token) {
        return !isNumberToken(token) && !isEOFToken(token);
    }

    private static boolean hasHigherPrecedence(Pair<Tokens, String> token) {
        return token.getKey() == Tokens.DIVIDE || token.getKey() == Tokens.MULTIPLY;
    }

    private static double evaluateBinary(double a, double b, String op) {
        if(op.equals("+")) return a + b;
        else if(op.equals("-")) return a - b;
        else if(op.equals("*")) return a * b;
        else return a / b;
    }
}