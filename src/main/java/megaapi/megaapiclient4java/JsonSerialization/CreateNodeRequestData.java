package megaapi.megaapiclient4java.JsonSerialization;

import megaapi.megaapiclient4java.Enumerations.NodeType;

public class CreateNodeRequestData{

    @JsonProperty("h")
    private String completionHandle;
    
    public String getCompletionHandle(){
        return completionHandle;
    }
    
    @JsonProperty("t")   
    private NodeType type;
    
    public NodeType getType(){
        return type;
    }

    @JsonProperty("a")
    private String attributes;
    
    public String getAttributes(){
        return attributes;
    }

    @JsonProperty("k")
    private String key;
    
    public String getKey(){
        return key;
    }
}