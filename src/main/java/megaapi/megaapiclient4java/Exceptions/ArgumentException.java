package megaapi.megaapiclient4java.Exceptions;

public class ArgumentException extends Exception {
    
    private final String message;
    
    public ArgumentException(String message) {
        this.message = message;
    }
}