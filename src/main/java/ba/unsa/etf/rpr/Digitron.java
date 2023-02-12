package ba.unsa.etf.rpr;

import ba.unsa.etf.rpr.evaluation.DigitronLexer;
import ba.unsa.etf.rpr.evaluation.DigitronParser;

import java.io.IOException;
import java.util.EmptyStackException;

public class Digitron {

    public static void main(String[] args) {
        DigitronLexer digitronLexer = new DigitronLexer();
        //digitronLexer.tokenize("(((5 * 2) * ((((5 * 2) + (3 / 4)) - (6 * 3)) + ((7 - 8) * (9 + 10)) / (11 * (12 - (13 / 2)))) + (3 / 4)) - (6 * 3)) + ((7 - 8) * (9 + 10)) / (11 * (12 - (13 / 2)))");
        try {
            digitronLexer.tokenize(" - 1 * ((-2 + 3) * 2)");
            DigitronParser digitronParser = new DigitronParser(digitronLexer.getTokens());
            System.out.println(digitronParser.evaluate());
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }



    }
}
