package megaapi.megaapiclient4java.JsonSerialization;

public class LoginRequest implements RequestBase {

    public LoginRequest(string userHandle, String passwordHash) 
        : base("us")
    {
      this.userHandle = userHandle;
        this.passwordHash = passwordHash;
    }

    @JsonProperty("user")
    private String userHandle;

    public String getUserHandle() {
        return userHandle;
    }

    @JsonProperty("uh")
    private String passwordHash;

    public String getPasswordHash() {
        return passwordHash;
    }
}
