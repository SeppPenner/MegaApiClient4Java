package MegaApiClient4Java;

import MegaApiClient4Java.Interfaces.INode;
import MegaApiClient4Java.Interfaces.INodeCrypto;
import MegaApiClient4Java.Interfaces.INodeInfo;
import java.util.Date;

public class PublicNode implements INode, INodeCrypto{

    private Node node;
    private String shareId;

    private PublicNode(Node node, String shareId){
        this.node = node;
        this.shareId = shareId;
    }

    public String getShareId(){
        return shareId;
    }

    public boolean equals(INodeInfo other){
        return this.node.equals(other) && (this.shareId == null ? ((PublicNode)other).getShareId() 
                == null : this.shareId.equals(((PublicNode)other).getShareId()));
    }

    @Override
    public long getSize(){
        return this.node.getSize();
    }
    
    @Override
    public String getName(){
        return this.node.getName();
    }
    
    public Date getModificationDate{
        return this.node.getModificationDate();
    }
    
    public String getId(){
        return this.node.getId();
    }
    
    public String getParentId(){
        return this.node.getParentId();
    }
    
    public String getOwner(){
        return this.node.getOwner();
    }
    
    public NodeType getType(){
        return this.node.getType();
    }

    public Date getCreationDate{
        return this.node.getCreationDate();
    }
    
    public byte[] getKey(){
        return this.node.getKey();
    }

    public byte[] getSharedKey(){
        return this.node.getSharedKey();
    }
    
    public byte[] getIv(){
        return this.node.getIv();
    }
    
    public byte[] getMetaMac(){
        return this.node.getMetaMac();
    }
    
    public byte[] getFullKey(){
        return this.node.getFullKey();
    }
 }