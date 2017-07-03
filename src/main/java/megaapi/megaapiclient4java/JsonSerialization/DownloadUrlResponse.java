package megaapi.megaapiclient4java.JsonSerialization;

public class DownloadUrlResponse {

    @JsonProperty("g")
    private String url;

    public String getUrl() {
        return url;
    }

    @JsonProperty("s")
    private String size;

    public String getSize() {
        return size;
    }

    @JsonProperty("at")
    private String serializedAttributes;

    public String getSerializedAttributes() {
        return serializedAttributes;
    }
}
