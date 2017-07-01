package megaapi.megaapiclient4java.JsonSerialization;

import megaapi.megaapiclient4java.Interfaces.INode;
import megaapi.megaapiclient4java.Interfaces.INodeCrypto;

public class ShareNodeRequest extends RequestBase{
    
    public ShareNodeRequest(INode node, byte[] masterKey, Iterable<INode> nodes): base("s2"){
        this.id = node.Id;
        this.options = new object[] { new { r = 0, u = "EXP" } };

        INodeCrypto nodeCrypto = (INodeCrypto)node;
        byte[] uncryptedSharedKey = nodeCrypto.getSharedKey();
        if (uncryptedSharedKey == null){
            uncryptedSharedKey = Crypto.CreateAesKey();
        }

        this.SharedKey = Crypto.EncryptKey(uncryptedSharedKey, masterKey).ToBase64();

        if (nodeCrypto.SharedKey == null){
            this.share = new ShareData(node.getId());

            this.share.AddItem(node.getId(), nodeCrypto.getFullKey(), uncryptedSharedKey);

            // Add all children
            Iterable<INode> allChildren = this.GetRecursiveChildren(nodes.ToArray(), node);
            for(INode child : allChildren){
                this.share.AddItem(child.getId(), ((INodeCrypto)child).getFullKey(), uncryptedSharedKey);
            }
        }

        byte[] handle = (node.getId() + node.getId()).ToBytes();
        this.handleAuth = Crypto.EncryptKey(handle, masterKey).ToBase64();
    }

    private Iterable<INode> GetRecursiveChildren(INode[] nodes, INode parent){
        for (var node in nodes.Where(x => x.Type == NodeType.Directory || x.Type == NodeType.File)){
            string parentId = node.Id;
            do
            {
                parentId = nodes.FirstOrDefault(x => x.Id == parentId)?.ParentId;
                if(parentId == parent.getId())
                {
                    yield return node;
                    break;
                }
            } while (parentId != null);
        }
    }

    @JsonProperty("n")
    private String id;

    public String getId(){
        return id;
    }
    
    @JsonProperty("ha")
    private String handleAuth;

    public String getHandleAuth(){
        return handleAuth;
    }

    @JsonProperty("s")
    private object[] options;

    public object[] getOptions(){
        return options;
    }
    
    @JsonProperty("cr")
    private ShareData share;

    public ShareData getShare(){
        return share;
    }

    @JsonProperty("ok")
    private String sharedKey;

    public String getSharedKey(){
        return sharedKey;
    }
}