package ba.unsa.etf.rpr;

import ba.unsa.etf.rpr.evaluation.DigitronLexer;
import ba.unsa.etf.rpr.evaluation.DigitronParser;

import java.io.IOException;

public class Digitron {

    public static void main(String[] args) {
        DigitronLexer digitronLexer = new DigitronLexer();
        try {
            digitronLexer.tokenize("2-(-1)");
            DigitronParser digitronParser = new DigitronParser(digitronLexer.getTokens());
            System.out.println(digitronParser.evaluate());
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
