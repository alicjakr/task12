public class EngineException extends Exception {
    public EngineException(String message) {
        super("Engine error: "+message);
    }
}
