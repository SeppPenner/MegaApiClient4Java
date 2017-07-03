package megaapi.megaapiclient4java.Exceptions;

public class UploadException extends Exception {

    @Override
    public String getMessage() {
        return "Upload error: " + error;
    }

    private UploadException(String error) {
        this.error = error;
    }

    private final String error;
}
