package megaapi.megaapiclient4java.JsonSerialization;

import megaapi.megaapiclient4java.Interfaces.INode;

public class RenameRequest extends RequestBase {

    public RenameRequest(INode node, String attributes){
        super("a");
        this.id = node.getId();
        this.serializedAttributes = attributes;
    }

    @JsonProperty("n")
    private final String id;

    public String getId() {
        return id;
    }

    @JsonProperty("attr")
    private final String serializedAttributes;

    public String getSerializedAttributes() {
        return serializedAttributes;
    }
}
