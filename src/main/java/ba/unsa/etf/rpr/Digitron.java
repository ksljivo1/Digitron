package ba.unsa.etf.rpr;

import ba.unsa.etf.rpr.evaluation.DigitronLexer;
import ba.unsa.etf.rpr.evaluation.DigitronParser;

import java.io.IOException;

public class Digitron {

    public static void main(String[] args) {
        try {
            DigitronParser digitronParser = new DigitronParser("1 + 2");
            System.out.println(digitronParser.evaluate());
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
