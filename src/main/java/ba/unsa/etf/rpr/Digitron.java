package ba.unsa.etf.rpr;

import ba.unsa.etf.rpr.evaluation.DigitronLexer;
import ba.unsa.etf.rpr.evaluation.DigitronParser;

public class Digitron {
    public static void main(String[] args) throws Exception {
        DigitronLexer digitronLexer = new DigitronLexer();
        digitronLexer.tokenize("(((5 * 2) * ((((5 * 2) + (3 / 4)) - (6 * 3)) + ((7 - 8) * (9 + 10)) / (11 * (12 - (13 / 2)))) + (3 / 4)) - (6 * 3)) + ((7 - 8) * (9 + 10)) / (11 * (12 - (13 / 2)))");
        DigitronParser digitronParser = new DigitronParser(digitronLexer.getTokens());
        System.out.println(digitronParser.evaluate());
    }
}
