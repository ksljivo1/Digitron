package ba.unsa.etf.rpr;

public class Digitron {
    public static void main(String[] args) throws Exception {
        DigitronLexer digitronLexer = new DigitronLexer();
        digitronLexer.tokenize("23 * 2 -4.5+ 6 +66/0");
        System.out.println(digitronLexer.getTokens());
    }
}
