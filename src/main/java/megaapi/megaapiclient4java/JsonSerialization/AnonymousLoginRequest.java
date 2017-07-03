package megaapi.megaapiclient4java.JsonSerialization;

public class AnonymousLoginRequest implements RequestBase {

    public AnonymousLoginRequest(String masterKey, String temporarySession) 
        : base("up")
    {
      this.masterKey = masterKey;
        this.temporarySession = temporarySession;
    }

    [

    JsonProperty(
    "k")]
    private String masterKey;

    public String getMasterKey() {
        return masterKey;
    }

    [

    JsonProperty(
    "ts")]
    private String temporarySession;

    public String getTemporarySession() {
        return temporarySession;
    }
}
