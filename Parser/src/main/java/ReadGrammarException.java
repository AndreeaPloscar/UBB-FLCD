public class ReadGrammarException extends Exception {
    public ReadGrammarException(String message) {
        super("Could not read grammar: " + message);
    }
}
