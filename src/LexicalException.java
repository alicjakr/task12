public class LexicalException extends Exception {
    public LexicalException(String message) {
        super("Lexical error: "+message);
    }
}