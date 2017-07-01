package megaapi.megaapiclient4java.JsonSerialization;

public class AuthInfos{
    
    public AuthInfos(String email, String hash, byte[] passwordAesKey){
        this.email = email;
        this.hash = hash;
        this.passwordAesKey = passwordAesKey;
    }
    
    @JsonProperty
    private final String email;
    
    public String getEmail(){
        return email;
    }
    
    @JsonProperty
    private final String hash;
    
    public String getHash(){
        return hash;
    }
    
    @JsonProperty
    private final byte[] passwordAesKey;
    
    public byte[] getPasswordAesKey(){
        return passwordAesKey;
    }
}