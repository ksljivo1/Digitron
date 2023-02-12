package ba.unsa.etf.rpr.evaluation;

import javafx.util.Pair;

import java.io.IOException;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

public class DigitronParser {
    List<Pair<Tokens, String>> tokens;

    public DigitronParser(List<Pair<Tokens, String>> tokens) {
        this.tokens = tokens;
    }

    public Stack<Pair<Tokens, String>> evaluate() throws IOException {
        Stack<Pair<Tokens, String>> numberTokens = new Stack<>();
        Stack<Pair<Tokens, String>> operationTokens = new Stack<>();
        try {
            parse(numberTokens, operationTokens, tokens);
            while (!operationTokens.empty()) {
                double op2 = Double.parseDouble(numberTokens.pop().getValue());
                double op1 = Double.parseDouble(numberTokens.pop().getValue());
                String op = operationTokens.pop().getValue();
                Double rez = evaluateBinary(op1, op2, op);
                numberTokens.push(new Pair<>(Tokens.DOUBLE, String.valueOf(rez)));
            }
        }
        catch (EmptyStackException | NumberFormatException | IndexOutOfBoundsException e) {
            throw new IOException("Syntax error");
        }
        if(numberTokens.size() != 1 || !operationTokens.empty()) throw new IOException("Syntax error");
        return numberTokens;
    }

    private static void parse(Stack<Pair<Tokens, String>> numberTokens, Stack<Pair<Tokens, String>> operationTokens, List<Pair<Tokens, String>> tokens) throws EmptyStackException, IOException {
        int i = 0;
        Double rez = 0.;
        while(!isEOFToken(tokens.get(i))) {
            Pair<Tokens, String> current = tokens.get(i);
            if(isLPARENTHESISToken(current)) numberTokens.push(current);
            else if(isRPARENTHESISToken(current)) {
                Pair<Tokens, String> isprazni2 = numberTokens.pop();
                Pair<Tokens, String> isprazni1 = numberTokens.pop();
                while(!isLPARENTHESISToken(isprazni1)) {
                    double op2 = Double.parseDouble(isprazni2.getValue());
                    double op1 = Double.parseDouble(isprazni1.getValue());
                    String op = operationTokens.pop().getValue();
                    rez = evaluateBinary(op1, op2, op);
                    numberTokens.push(new Pair<>(Tokens.DOUBLE, String.valueOf(rez)));
                    isprazni2 = numberTokens.pop();
                    isprazni1 = numberTokens.pop();
                }
                numberTokens.push(isprazni2);
            }
            else if(numberTokens.size() >= 2 && !operationTokens.empty() && hasHigherPrecedence(operationTokens.peek())) {
                Pair<Tokens, String> op2Token = numberTokens.pop();
                Pair<Tokens, String> op1Token = numberTokens.pop();
                if(isLPARENTHESISToken(op2Token) || isLPARENTHESISToken(op1Token)) {
                    numberTokens.push(op1Token);
                    numberTokens.push(op2Token);
                    if(isOperationToken(current)) operationTokens.push(current);
                    else if(isNumberToken(current) || isLPARENTHESISToken(current)) numberTokens.push(current);
                    else ;
                }
                else {
                    double op2 = Double.parseDouble(op2Token.getValue());
                    double op1 = Double.parseDouble(op1Token.getValue());
                    String op = operationTokens.pop().getValue();
                    try {
                        rez = evaluateBinary(op1, op2, op);
                    } catch (RuntimeException runtimeException) {
                        System.out.println(runtimeException.getMessage());
                    }
                    numberTokens.push(new Pair<>(Tokens.DOUBLE, String.valueOf(rez)));
                    i--;
                }
            }
            else if(numberTokens.size() >= 2 && !operationTokens.empty() && !hasHigherPrecedence(operationTokens.peek())) {
                int t = i;
                if(t + 1 < tokens.size() && hasHigherPrecedence(tokens.get(t))) {
                    numberTokens.push(tokens.get(t + 1));
                    operationTokens.push(tokens.get(t));
                    i = t + 1;
                }
                else {
                    Pair<Tokens, String> op2Token = numberTokens.pop();
                    Pair<Tokens, String> op1Token = numberTokens.pop();
                    if(isLPARENTHESISToken(op2Token) || isLPARENTHESISToken(op1Token)) {
                        numberTokens.push(op1Token);
                        numberTokens.push(op2Token);
                        if(isOperationToken(current)) operationTokens.push(current);
                        else if(isNumberToken(current) || isLPARENTHESISToken(current)) numberTokens.push(current);
                        else ;
                    }
                    else {
                        double op2 = Double.parseDouble(op2Token.getValue());
                        double op1 = Double.parseDouble(op1Token.getValue());
                        String op = operationTokens.pop().getValue();
                        rez = evaluateBinary(op1, op2, op);
                        numberTokens.push(new Pair<>(Tokens.DOUBLE, String.valueOf(rez)));
                        i--;
                    }
                }
            }
            else if(isOperationToken(current)) operationTokens.push(current);
            else if(isNumberToken(current)) numberTokens.push(current);
            else;
            i++;
        }
    }

    private static boolean isNumberToken(Pair<Tokens, String> token) {
        return token.getKey() == Tokens.DOUBLE;
    }

    private static boolean isEOFToken(Pair<Tokens, String> token) {
        return token.getKey() == Tokens.EOF;
    }

    private static boolean isOperationToken(Pair<Tokens, String> token) {
        return token.getKey() == Tokens.PLUS || token.getKey() == Tokens.MINUS || token.getKey() == Tokens.MULTIPLY || token.getKey() == Tokens.DIVIDE;
    }

    private static boolean isLPARENTHESISToken(Pair<Tokens, String> token) {
        return token.getKey() == Tokens.LPARENTHESIS;
    }

    private static boolean isRPARENTHESISToken(Pair<Tokens, String> token) {
        return token.getKey() == Tokens.RPARENTHESIS;
    }

    private static boolean hasHigherPrecedence(Pair<Tokens, String> token) {
        return token.getKey() == Tokens.DIVIDE || token.getKey() == Tokens.MULTIPLY;
    }

    private static double evaluateBinary(double a, double b, String op) throws IOException {
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
