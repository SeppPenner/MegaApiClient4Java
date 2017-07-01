package megaapi.megaapiclient4java.JsonSerialization;

import megaapi.megaapiclient4java.Interfaces.INode;

public class DeleteRequest extends RequestBase
  {
    public DeleteRequest(INode node): base("d"){
        this.node = node.getId();
    }

    @JsonProperty("n")
    private String node;

    public String getNode(){
        return node;
    }
}