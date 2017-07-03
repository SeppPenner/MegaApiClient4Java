package megaapi.megaapiclient4java.JsonSerialization;

public class UploadUrlRequest extends RequestBase{

    public UploadUrlRequest(long fileSize){
        super("u");
this.size = fileSize;
    }

    @JsonProperty("s")
    private final long size;

    public long getSize(){
        return size;
    }
}
