package megaapi.megaapiclient4java.JsonSerialization;

import megaapi.megaapiclient4java.Enumerations.NodeType;
import megaapi.megaapiclient4java.Interfaces.INode;
import megaapi.megaapiclient4java.Interfaces.INodeCrypto;

public class CreateNodeRequest extends RequestBase {

    private CreateNodeRequest(INode parentNode, NodeType type, String attributes,
            String encryptedKey, byte[] key, String completionHandle) 
        : base("p"){
        
        this.parentId = parentNode.getId();
        this.nodes = new []{
            new CreateNodeRequestData
            
        {
            Attributes = attributes
            ,
                Key = encryptedKey
            ,
                Type = type
            ,
                CompletionHandle = completionHandle
        }
    }
    ;

        INodeCrypto parentNodeCrypto = (INodeCrypto) parentNode;
    if (parentNodeCrypto

    
        == null){
            throw new ArgumentException("parentNode node must implement INodeCrypto");
    }
    if (parentNodeCrypto.getSharedKey

    
        != null){
            this.share = new ShareData(parentNode.Id);
        this.share.AddItem(completionHandle, key, parentNodeCrypto.getSharedKey());
    }
}

@JsonProperty("t")
        private String parentId;

    public String getParentId(){
        return parentId;
    }

    @JsonProperty("cr")
        private ShareData share;
    
    public ShareData getShare(){
        return share;
    }

    @JsonProperty("n")
        private CreateNodeRequestData[] nodes;

    public CreateNodeRequestData[] getNodes(){
        return nodes;
    }

    public static CreateNodeRequest CreateFileNodeRequest(INode parentNode, String attributes, 
        String encryptedkey, byte[] fileKey, String completionHandle){
            return new CreateNodeRequest(parentNode, NodeType.File, attributes, encryptedkey, fileKey, completionHandle);
    }

    public static CreateNodeRequest CreateFolderNodeRequest(INode parentNode, 
        String attributes, String encryptedkey, byte[] key){
        return new CreateNodeRequest(parentNode, NodeType.Directory, attributes, encryptedkey, key, "xxxxxxxx");
    }
}
