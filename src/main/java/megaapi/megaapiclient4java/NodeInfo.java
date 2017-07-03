package megaapi.megaapiclient4java;

import java.util.Date;
import megaapi.megaapiclient4java.Cryptography.Crypto;
import megaapi.megaapiclient4java.Enumerations.NodeType;
import megaapi.megaapiclient4java.Interfaces.INodeInfo;
import megaapi.megaapiclient4java.JsonSerialization.DownloadUrlResponse;

public class NodeInfo implements INodeInfo {

    protected NodeInfo() {
    }

    private NodeInfo(String id, DownloadUrlResponse downloadResponse, byte[] key) {
        this.id = id;
        attributes = Crypto.DecryptAttributes(downloadResponse.getSerializedAttributes().FromBase64(), key);
        size = downloadResponse.getSize();
        type = NodeType.File;
    }

    @JsonIgnore
    @Override
    public String getName()
    {
        if(attributes == null){
            return "";
        }
        else{
            return attributes.getName();
        }
    }

    @JsonProperty("s")
    public long size;
    
    @Override
    public long getSize(){
        return size;
    }

    @JsonProperty("t")
    private NodeType type;
    
    public NodeType getNodeType(){
        return type;
    }

    @JsonProperty("h")
    private String id;
    
    @Override
    public String getId(){
        return id;
    }
    

    @JsonIgnore
    @Override
    public Date getModificationDate(){
        if(attributes == null){
            return null;
        }
        else{
            return attributes.getModificationDate();
        }
    }

    @JsonIgnore
    private Attributes attributes;
    
    public Attributes getAttributes(){
        return attributes;
    }
    

    public boolean equals(INodeInfo other)
    {
      return other != null && (id == null ? other.getId() == null : id.equals(other.getId()));
    }

    @Override
    public int hashCode()
    {
      return id.hashCode();
    }

    @Override
    public boolean equals(Object obj){
        return this.equals((INodeInfo)obj);
    }

  }