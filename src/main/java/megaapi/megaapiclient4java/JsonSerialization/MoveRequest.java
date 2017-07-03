package megaapi.megaapiclient4java.JsonSerialization;

import megaapi.megaapiclient4java.Interfaces.INode;

public class MoveRequest extends RequestBase {

    public MoveRequest(INode node, INode destinationParentNode){
        super("m");
      this.id = node.getId();
        this.destinationParentId = destinationParentNode.getId();
    }

    @JsonProperty(    "n")
    private final String id;

    public String getId() {
        return id;
    }

    @JsonProperty("t")
    private final String destinationParentId;

    public String getDestinationParentId() {
        return destinationParentId;
    }
}
