package FA;

public class FaTokenException extends Exception{
    public FaTokenException(String message) {
        super(message + " does not match definition!");
    }
}
