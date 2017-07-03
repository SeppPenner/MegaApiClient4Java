package megaapi.megaapiclient4java.JsonSerialization;

public class AnonymousLoginRequest implements RequestBase {

    public AnonymousLoginRequest(String masterKey, String temporarySession){
        super("up");
      this.masterKey = masterKey;
        this.temporarySession = temporarySession;
    }

    @JsonProperty("k")
    private final String masterKey;

    public String getMasterKey() {
        return masterKey;
    }

    @JsonProperty("ts")
    private final String temporarySession;

    public String getTemporarySession() {
        return temporarySession;
    }
}
