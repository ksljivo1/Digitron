package ba.unsa.etf.rpr;

public class Digitron {
    public static void main(String[] args) throws Exception {
        DigitronLexer digitronLexer = new DigitronLexer();
        digitronLexer.tokenize("2 * 2 + (3*3 +2 * ( 1.432 - 1))");
        //DigitronParser digitronParser = new DigitronParser(digitronLexer.getTokens());
        System.out.println(digitronLexer.getTokens());
    }
}
