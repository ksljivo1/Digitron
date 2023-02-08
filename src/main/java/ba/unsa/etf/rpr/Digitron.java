package ba.unsa.etf.rpr;

import ba.unsa.etf.rpr.models.DigitronParser;

public class Digitron {
    public static void main(String[] args) throws Exception {
        DigitronLexer digitronLexer = new DigitronLexer();
        digitronLexer.tokenize("1 + 2.2 / 1.1 + 5 - 1 + 3 * 2 * 1 - 1 / 0.5");
        DigitronParser digitronParser = new DigitronParser(digitronLexer.getTokens());
        System.out.println(digitronParser.evaluate());
    }
}
