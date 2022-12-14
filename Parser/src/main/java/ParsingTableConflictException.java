public class ParsingTableConflictException extends Exception {
    public ParsingTableConflictException(String message) {
        super("Grammar is NOT LR(0): " + message);
    }
}
