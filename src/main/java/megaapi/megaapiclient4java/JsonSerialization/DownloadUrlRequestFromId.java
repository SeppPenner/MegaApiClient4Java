package megaapi.megaapiclient4java.JsonSerialization;

public class DownloadUrlRequestFromId extends RequestBase {

    public DownloadUrlRequestFromId(String id){
        super("g");
      this.id = id;
    }

    public int getG() {
        return 1;
    }

    @JsonProperty("p")
    private final String id;

    public String getId() {
        return id;
    }
}
