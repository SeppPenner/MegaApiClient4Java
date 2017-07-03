package megaapi.megaapiclient4java.Exceptions;

public class ArgumentNullException extends Exception {
    
    private final String message;
    
    public ArgumentNullException(String message) {
        this.message = message;
    }
}