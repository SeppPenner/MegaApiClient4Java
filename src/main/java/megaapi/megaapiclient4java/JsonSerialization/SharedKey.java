package megaapi.megaapiclient4java.JsonSerialization;

public class SharedKey {

    public SharedKey(String id, String key) {
        this.id = id;
        this.key = key;
    }

    @JsonProperty("h")
    private final String id;

    public String getId() {
        return id;
    }

    @JsonProperty("k")
    private final String key;

    public String getKey() {
        return id;
    }
}
