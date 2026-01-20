public class SyntaxException extends Exception {
    public SyntaxException(String message) {
        super("Syntax error: "+message);
    }
}
