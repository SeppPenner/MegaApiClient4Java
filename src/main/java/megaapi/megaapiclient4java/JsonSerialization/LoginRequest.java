package megaapi.megaapiclient4java.JsonSerialization;

public class LoginRequest extends RequestBase {

    public LoginRequest(String userHandle, String passwordHash){
        super("us");
      this.userHandle = userHandle;
        this.passwordHash = passwordHash;
    }

    @JsonProperty("user")
    private final String userHandle;

    public String getUserHandle() {
        return userHandle;
    }

    @JsonProperty("uh")
    private final String passwordHash;

    public String getPasswordHash() {
        return passwordHash;
    }
}
