public class MyRuntimeException extends RuntimeException {
    public MyRuntimeException(String message) {
        super("Runtime error: "+message);
    }
}
