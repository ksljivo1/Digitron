package ba.unsa.etf.rpr.evaluation;

import javafx.util.Pair;

import java.io.IOException;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

/**
 * Parser of calculator input expressions
 *
 * @author ksljivo1
 */

public class TestParser {
    private List<Pair<Tokens, String>> tokens;
    private Stack<Pair<Tokens, String>> operandTokens;
    private Stack<Pair<Tokens, String>> operationTokens;

    public TestParser(String expr) throws IOException {
        TestLexer digitronLexer = new TestLexer();
        try {
            digitronLexer.tokenize(expr);
        } catch (IOException e) {
            throw new IOException("Syntax error");
        }
        tokens = digitronLexer.getTokens();
        //System.out.println(tokens);
        operandTokens = new Stack<>();
        operationTokens = new Stack<>();
    }

    public void parse() throws IOException {
        for(int i = 0; !isEOFToken(tokens.get(i)); i = i + 1) {
            System.out.println(operandTokens);
            System.out.println(operationTokens + "\n");
            Pair<Tokens, String> current = tokens.get(i);
            if(isLPARENTHESISToken(current) || isOperandToken(current)) operandTokens.push(current);
            else if(isOperationToken(current)) operationTokens.push(current);
            else if(isRPARENTHESISToken(current)) {
                boolean loop = true;
                while(loop) {
                    var b = operandTokens.pop();
                    var a = operandTokens.pop();
                    var operator = operationTokens.pop();
                    if(isLPARENTHESISToken(operandTokens.peek())) {
                        operandTokens.pop();
                        loop = false;
                    }
                    if(!operationTokens.empty() && hasHigherPrecedence(operationTokens.peek()) && loop) calculateWithPrecedence(a, b, operator);
                    else operandTokens.push(new Pair<>(Tokens.DOUBLE, String.valueOf(
                            evaluateBinary(Double.parseDouble(a.getValue()), Double.parseDouble(b.getValue()), operator.getValue()))));
                }
            }
        }
    }

    public double evaluate() throws IOException {
        while(operandTokens.size() != 1 || !operationTokens.empty()) {
            var b = operandTokens.pop();
            var a = operandTokens.pop();
            var operator = operationTokens.pop();
            if(!operationTokens.empty() && hasHigherPrecedence(operationTokens.peek())) calculateWithPrecedence(a, b, operator);
            else operandTokens.push(new Pair<>(Tokens.DOUBLE, String.valueOf(
                    evaluateBinary(Double.parseDouble(a.getValue()), Double.parseDouble(b.getValue()), operator.getValue()))));
        }
        return Double.parseDouble(operandTokens.pop().getValue());
    }

    private void calculateWithPrecedence(Pair<Tokens, String> a, Pair<Tokens, String> b, Pair<Tokens, String> operator) throws IOException {
        var c = operandTokens.pop();
        var multiplyOrDivide = operationTokens.pop();
        operandTokens.push(new Pair<>(Tokens.DOUBLE,
                String.valueOf(evaluateBinary(Double.parseDouble(c.getValue()), Double.parseDouble(a.getValue()), multiplyOrDivide.getValue()))));
        operandTokens.push(b);
        operationTokens.push(operator);
    }

    public Stack<Pair<Tokens, String>> getOperandTokens() {
        return operandTokens;
    }

    public Stack<Pair<Tokens, String>> getOperationTokens() {
        return operationTokens;
    }

    private static boolean isInsideParentheses(boolean lparenthesisToken, boolean lparenthesisToken2) {
        return lparenthesisToken || lparenthesisToken2;
    }

    public static boolean isOperandToken(Pair<Tokens, String> token) {
        return token.getKey() == Tokens.DOUBLE;
    }

    public static boolean isEOFToken(Pair<Tokens, String> token) {
        return token.getKey() == Tokens.EOF;
    }

    private static boolean isTANToken(Pair<Tokens, String> token) {
        return token.getKey() == Tokens.TAN;
    }

    private static boolean isSINToken(Pair<Tokens, String> token) {
        return token.getKey() == Tokens.SIN;
    }

    private static boolean isCOSToken(Pair<Tokens, String> token) {
        return token.getKey() == Tokens.COS;
    }

    private static boolean isCOTToken(Pair<Tokens, String> token) {
        return token.getKey() == Tokens.COT;
    }

    private static boolean isOperationToken(Pair<Tokens, String> token) {
        return token.getKey() == Tokens.PLUS || token.getKey() == Tokens.MINUS || token.getKey() == Tokens.MULTIPLY || token.getKey() == Tokens.DIVIDE;
    }

    public static boolean isLPARENTHESISToken(Pair<Tokens, String> token) {
        return token.getKey() == Tokens.LPARENTHESIS;
    }

    public static boolean isMinusToken(Pair<Tokens, String> token) {
        return token.getKey() == Tokens.MINUS;
    }

    public static boolean isRPARENTHESISToken(Pair<Tokens, String> token) {
        return token.getKey() == Tokens.RPARENTHESIS;
    }

    private static boolean hasHigherPrecedence(Pair<Tokens, String> token) {
        return token.getKey() == Tokens.DIVIDE || token.getKey() == Tokens.MULTIPLY;
    }

    private double evaluateBinary(double a, double b, String op) throws IOException {
        if(op.equals("+")) return a + b;
        else if(op.equals("-")) return a - b;
        else if(op.equals("*")) return a * b;
        else {
            Double rez = a / b;
            if(rez.isNaN() || rez.isInfinite()) throw new IOException("ERROR: Division by zero");
            return rez;
        }
    }
}

