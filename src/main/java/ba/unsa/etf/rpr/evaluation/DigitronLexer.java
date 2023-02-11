package ba.unsa.etf.rpr.evaluation;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class DigitronLexer {
    List<Pair<Tokens, String>> tokens;

    public DigitronLexer() {
        tokens = new ArrayList<>();
    }

    public void tokenize(String expr) throws Exception {
        int poz = 0;
        while(poz < expr.length()) {
            char current = expr.charAt(poz);
            if(current == '+') tokens.add(new Pair(Tokens.PLUS, "+"));
            else if(current == '-') tokens.add(new Pair(Tokens.MINUS, "-"));
            else if(current == '*') tokens.add(new Pair(Tokens.MULTIPLY, "*"));
            else if(current == '/') tokens.add(new Pair(Tokens.DIVIDE, "/"));
            else if(current == '(') tokens.add(new Pair(Tokens.LPARENTHESIS, "("));
            else if(current == ')') tokens.add(new Pair(Tokens.RPARENTHESIS, ")"));
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
                catch (NumberFormatException numberFormatException) {
                    throw new Exception("Expected a valid token at position: " + poz + " instead recieved: " + current);
                }
                tokens.add(new Pair(Tokens.DOUBLE, str));
                poz = poz1 - 1;
            }
            else if(current == ' ') ;
            else throw new Exception("Expected a valid token at position: " + poz + " instead recieved: " + current);
            poz = poz + 1;
        }
        tokens.add(new Pair(Tokens.EOF, "EOF"));
    }

    public List<Pair<Tokens, String>>  getTokens() {
        return tokens;
    }
}
