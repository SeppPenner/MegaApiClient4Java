package megaapi.megaapiclient4java.JsonSerialization;

import megaapi.megaapiclient4java.Interfaces.INode;

public class RenameRequest implements RequestBase{
    
    public RenameRequest(INode node, String attributes): base("a"){
        this.id = node.getId();
        this.serializedAttributes = attributes;
    }

    [JsonProperty("n")]
    private String id;
    
    public String getId(){
        return id;
    }
    
    [JsonProperty("attr")]
    private String serializedAttributes;
    
    public String getSerializedAttributes(){
        return serializedAttributes;
    }
}