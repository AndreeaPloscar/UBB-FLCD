public class LexicalError extends Exception{

    public LexicalError(Integer line, Integer token) {
        super("Lexical error on line " + line + " token " + token);
    }
}
