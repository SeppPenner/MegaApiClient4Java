package megaapi.megaapiclient4java.JsonSerialization;

public class LoginResponse {

    @JsonProperty("csid")
    private String sessionId;

    public String getSessionId() {
        return sessionId;
    }

    @JsonProperty("tsid")
    private String temporarySessionId;

    public String getTemporarySessionId() {
        return temporarySessionId;
    }

    @JsonProperty("privk")
    private String privateKey;

    public String getPrivateKey() {
        return privateKey;
    }

    @JsonProperty("k")
    private String masterKey;

    public String getMasterKey() {
        return masterKey;
    }
}
