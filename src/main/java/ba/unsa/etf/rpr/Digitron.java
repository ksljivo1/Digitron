package ba.unsa.etf.rpr;

import ba.unsa.etf.rpr.evaluation.DigitronLexer;
import ba.unsa.etf.rpr.evaluation.DigitronParser;

import java.io.IOException;
import java.util.Scanner;

public class Digitron {

    public static void main(String[] args) throws IOException {
        int i = 0;
            while(i < 100) {
                try {
                    System.out.print(">");
                    Scanner scanner = new Scanner(System.in);
                    /*DigitronLexer digitronLexer = new DigitronLexer();
                    digitronLexer.tokenize(scanner.nextLine());
                    System.out.println(digitronLexer.getTokens());*/
                    DigitronParser digitronParser = new DigitronParser(scanner.nextLine());
                    System.out.println(digitronParser.evaluate().peek().getValue());

                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                i++;
            }

    }
}
