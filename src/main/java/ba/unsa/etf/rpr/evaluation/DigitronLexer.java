package ba.unsa.etf.rpr.evaluation;

import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lexical analysis of calculator input expressions
 *
 * @author ksljivo1
 */

public class DigitronLexer {
    List<Pair<Tokens, String>> tokens;

    public DigitronLexer() {
        tokens = new ArrayList<>();
    }

    /**
     *
     * @param expr
     * @throws IOException
     */
    public void tokenize(String expr) throws IOException {
        int poz = 0;
        while(poz < expr.length()) {
            char current = expr.charAt(poz);
            if(current == '+') tokens.add(new Pair<>(Tokens.PLUS, "+"));
            else if(current == 't' && poz + 2 < expr.length() && expr.charAt(poz + 1) == 'a' && expr.charAt(poz + 2) == 'n') {
                tokens.add(new Pair<>(Tokens.TAN, "tan"));
                poz = poz + 2;
            }
            else if(current == '-') tokens.add(new Pair<>(Tokens.MINUS, "-"));
            else if(current == '*') tokens.add(new Pair<>(Tokens.MULTIPLY, "*"));
            else if(current == '/') tokens.add(new Pair<>(Tokens.DIVIDE, "/"));
            else if(current == '(') {
                if(isUnaryMinus()) {
                    modifyUnary("1");
                    tokens.add(new Pair<>(Tokens.MULTIPLY, "*"));
                }
                tokens.add(new Pair<>(Tokens.LPARENTHESIS, "("));
            }
            else if(current == ')') {
                if(!tokens.isEmpty() && tokens.get(tokens.size() - 1).getValue().equals("("))
                    throw new IOException(getExceptionMessage(poz, current));
                tokens.add(new Pair<>(Tokens.RPARENTHESIS, ")"));
            }
            else if(Character.isDigit(current)) {
                StringBuilder broj = new StringBuilder();
                int poz1 = poz;
                while(poz1 < expr.length() && (Character.isDigit(expr.charAt(poz1)) || expr.charAt(poz1) == '.'))  {
                    broj.append(expr.charAt(poz1));
                    poz1 = poz1 + 1;
                }
                String str = broj.toString();
                try {
                    Double.parseDouble(str);
                }
                catch(NumberFormatException numberFormatException) {
                    throw new IOException(getExceptionMessage(poz, current));
                }

                if(isUnaryMinus()) modifyUnary(str);
                else tokens.add(new Pair<>(Tokens.DOUBLE, str));
                poz = poz1 - 1;
            }
            else if(Character.isWhitespace(current));
            else if(poz + 2 < expr.length() && current == 's' &&
                    expr.charAt(poz + 1) == 'i' && expr.charAt(poz + 2) == 'n') {
                tokens.add(new Pair<>(Tokens.SIN, "sin"));
                poz = poz + 2;
            }
            else if(poz + 2 < expr.length() && current == 'l' &&
                    expr.charAt(poz + 1) == 'o' && expr.charAt(poz + 2) == 'g') {
                tokens.add(new Pair<>(Tokens.LOG, "log"));
                poz = poz + 2;
            }
            else if(poz + 2 < expr.length() && current == 'c' &&
                    expr.charAt(poz + 1) == 'o' && expr.charAt(poz + 2) == 's') {
                tokens.add(new Pair<>(Tokens.COS, "cos"));
                poz = poz + 2;
            }
            else if(poz + 2 < expr.length() && current == 'c' &&
                    expr.charAt(poz + 1) == 'o' && expr.charAt(poz + 2) == 't') {
                tokens.add(new Pair<>(Tokens.COT, "cot"));
                poz = poz + 2;
            }
            else throw new IOException(getExceptionMessage(poz, current));
            poz = poz + 1;
        }
        tokens.add(new Pair<>(Tokens.EOF, "EOF"));
    }

    private String getExceptionMessage(int poz, char current) {
        return "Expected a valid token at position: " + (poz + 1) + ", instead received: " + current;
    }

    private void modifyUnary(String s) {
        tokens.remove(tokens.size() - 1);
        tokens.add(new Pair<>(Tokens.LPARENTHESIS, "("));
        tokens.add(new Pair<>(Tokens.DOUBLE, "0"));
        tokens.add(new Pair<>(Tokens.MINUS, "-"));
        tokens.add(new Pair<>(Tokens.DOUBLE, s));
        tokens.add(new Pair<>(Tokens.RPARENTHESIS, ")"));
    }

    private boolean isUnaryMinus() {
        return (tokens.size() >= 2 && tokens.get(tokens.size() - 1).getValue().equals("-") && tokens.get(tokens.size() - 2).getValue().equals("(")) ||
                (tokens.size() == 1 && tokens.get(0).getValue().equals("-"));
    }

    public List<Pair<Tokens, String>> getTokens() {
        return tokens;
    }
}
