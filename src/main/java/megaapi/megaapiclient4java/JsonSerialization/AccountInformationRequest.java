package megaapi.megaapiclient4java.JsonSerialization;

public class AccountInformationRequest extends RequestBase {

    public AccountInformationRequest() {
        super("uq");
    }

    @JsonProperty("strg")
    private int storage;

    public int getStorage() {
        return 1;
    }

    @JsonProperty("xfer")
    private int transfer;

    public int getTransfer() {
        return 0;
    }

    @JsonProperty("pro")
    private int accountType;

    public int getAccountType() {
        return 0;
    }
}
