package megaapi.megaapiclient4java.JsonSerialization;

public class UploadUrlResponse{
    
    @JsonProperty("p")
    private String url;
    
    public String getUrl(){
        return url;
    }
}