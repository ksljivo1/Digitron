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

public class TestLexer {
    List<Pair<Tokens, String>> tokens;

    public TestLexer() {
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
            else if(current == '-') tokens.add(new Pair<>(Tokens.MINUS, "-"));
            else if(current == '*') tokens.add(new Pair<>(Tokens.MULTIPLY, "*"));
            else if(current == '/') tokens.add(new Pair<>(Tokens.DIVIDE, "/"));
            else if(current == '(') tokens.add(new Pair<>(Tokens.LPARENTHESIS, "("));
            else if(current == ')') tokens.add(new Pair<>(Tokens.RPARENTHESIS, ")"));
            else if(Character.isDigit(current)) {
                String minus = "";
                int currentSize = tokens.size();
                if(currentSize == 1 && TestParser.isMinusToken(tokens.get(0)) ||
                        (currentSize >= 2 && TestParser.isMinusToken(tokens.get(currentSize - 1)) &&
                                TestParser.isLPARENTHESISToken(tokens.get(currentSize - 2)))) minus = "-";

                tokens.add(new Pair<>(Tokens.DOUBLE, minus + current));
            }
            else if(Character.isWhitespace(current));
            else throw new IOException(getExceptionMessage(poz, current));
            poz = poz + 1;
        }
        tokens.add(new Pair<>(Tokens.EOF, "EOF"));
    }

    /*public void handleUnaryMinus() {
        int poz = 0;
        while(!TestParser.isEOFToken(tokens.get(poz))) {
            if(poz == 0 && TestParser.isMinusToken(tokens.get(poz))) {
                tokens.add(poz, new Pair<>(Tokens.DOUBLE, "0"));
                tokens.add(poz + 2, new Pair<>(Tokens.DOUBLE, "1"));
                tokens.add(poz + 3, new Pair<>(Tokens.MULTIPLY, "*"));
                poz = poz + 3;
            }
            else if(poz != 0) {
                Pair<Tokens, String> previousToken = tokens.get(poz - 1);
                if (TestParser.isMinusToken(tokens.get(poz)) &&
                        TestParser.isLPARENTHESISToken(previousToken)) {
                    tokens.add(poz, new Pair<>(Tokens.DOUBLE, "0"));
                    tokens.add(poz + 2, new Pair<>(Tokens.DOUBLE, "1"));
                    tokens.add(poz + 3, new Pair<>(Tokens.MULTIPLY, "*"));
                    poz = poz + 3;
                }
            }
            else;
            poz = poz + 1;
        }
    }*/

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
