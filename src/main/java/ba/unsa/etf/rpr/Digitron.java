package ba.unsa.etf.rpr;

public class Digitron {
    public static void main(String[] args) throws Exception {
        DigitronLexer digitronLexer = new DigitronLexer();
        digitronLexer.tokenize("2 + 2 / 2 / 2");
        DigitronParser digitronParser = new DigitronParser(digitronLexer.getTokens());
        System.out.println(digitronParser.evaluate());
    }
}
