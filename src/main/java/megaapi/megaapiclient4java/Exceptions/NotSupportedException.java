package megaapi.megaapiclient4java.Exceptions;

public class NotSupportedException extends Exception {
    
    private final String message;
    
    public NotSupportedException(String message) {
        this.message = message;
    }
}