package megaapi.megaapiclient4java.JsonSerialization;

public class DownloadUrlRequestFromId extends RequestBase {

    public DownloadUrlRequestFromId(String id) 
        : base("g"){
      this.id = id;
    }

    public int getG() {
        return 1;
    }

    @JsonProperty("p")
    private String id;

    public String getId() {
        return id;
    }
}
