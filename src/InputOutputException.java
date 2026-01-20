public class InputOutputException extends Exception {
    public InputOutputException(String message) {
        super("Lexical error: "+message);
    }
}
