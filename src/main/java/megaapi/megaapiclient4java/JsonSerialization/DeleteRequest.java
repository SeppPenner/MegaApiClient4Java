package megaapi.megaapiclient4java.JsonSerialization;

import megaapi.megaapiclient4java.Interfaces.INode;

public class DeleteRequest extends RequestBase {

    public DeleteRequest(INode node) {
        super("d");
        this.node = node.getId();
    }

    @JsonProperty("n")
    private final String node;

    public String getNode() {
        return node;
    }
}
