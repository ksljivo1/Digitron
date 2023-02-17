package ba.unsa.etf.rpr;

import ba.unsa.etf.rpr.evaluation.DigitronLexer;
import ba.unsa.etf.rpr.evaluation.DigitronParser;

import java.io.IOException;

public class Digitron {

    public static void main(String[] args) {
        try {
            DigitronLexer digitronLexer = new DigitronLexer();
            DigitronParser digitronParser = new DigitronParser("tan(60) + cot(1)");
            digitronLexer.tokenize("tan(60) + 2 * cot(1)");
            System.out.println(digitronParser.evaluate());
            //System.out.println(digitronLexer.getTokens());
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
