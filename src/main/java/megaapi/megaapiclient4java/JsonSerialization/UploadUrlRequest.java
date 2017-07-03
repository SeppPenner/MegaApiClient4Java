package megaapi.megaapiclient4java.JsonSerialization;

public class UploadUrlRequest 
: RequestBase{

    public UploadUrlRequest(long fileSize): base("u"){
        this.size = fileSize;
    }

    [JsonProperty("s")]
    private long size;

    public long getSize(){
        return size;
    }
}
