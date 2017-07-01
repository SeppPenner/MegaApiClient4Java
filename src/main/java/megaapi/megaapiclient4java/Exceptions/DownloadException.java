package megaapi.megaapiclient4java.Exceptions;

public class DownloadException extends Exception{

    @Override
    public String getMessage(){
        return "Invalid file checksum";
    }
}