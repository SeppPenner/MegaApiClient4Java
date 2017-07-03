package megaapi.megaapiclient4java.JsonSerialization;

import java.nio.charset.Charset;
import java.util.Base64;
import megaapi.megaapiclient4java.Cryptography.Crypto;
import megaapi.megaapiclient4java.Interfaces.INode;
import megaapi.megaapiclient4java.Interfaces.INodeCrypto;

public class ShareNodeRequest extends RequestBase {

    public ShareNodeRequest(INode node, byte[] masterKey, Iterable<INode> nodes) {
        super("s2");
        this.id = node.getId();
        this.options = new Object[]{0, "EXP" };

        INodeCrypto nodeCrypto = (INodeCrypto) node;
    byte[] uncryptedSharedKey = nodeCrypto.getSharedKey();
    if (uncryptedSharedKey == null){
        uncryptedSharedKey = Crypto.CreateAesKey();
    }
    
    byte [] encryptedSharedKeyKey = Crypto.EncryptKey(uncryptedSharedKey, masterKey);
    
    this.sharedKey  = Base64.getEncoder().encodeToString(encryptedSharedKeyKey);

    if (nodeCrypto.getSharedKey()== null){
            this.share = new ShareData(node.getId());

        this.share.AddItem(node.getId(), nodeCrypto.getFullKey(), uncryptedSharedKey);

        // Add all children
        Iterable<INode> allChildren = this.GetRecursiveChildren(nodes.toArray(), node);
        for (INode child : allChildren) {
            this.share.AddItem(child.getId(), ((INodeCrypto) child).getFullKey(), uncryptedSharedKey);
        }
    }

    byte[] handle = (node.getId() + node.getId()).getBytes(Charset.forName("UTF-8"));
     
    byte[] encryptedHandle = Crypto.EncryptKey(handle, masterKey);
    
    this.handleAuth  = Base64.getEncoder().encodeToString(encryptedHandle);
    

private Iterable<INode> GetRecursiveChildren(INode[] nodes, INode parent){
        for (var node in nodes.Where(x => x.Type == NodeType.Directory || x.Type == NodeType.File)){
            String parentId = node.getId();
            do
            {
                parentId = nodes.FirstOrDefault(x => x.Id == parentId)?.ParentId;
                if(parentId == null ? parent.getId() == null : parentId.equals(parent.getId()))
                {
                    yield return node;
                    break;
                }
            } while (parentId != null);
        }
    }

    @JsonProperty("n")
        private final String id;

    public String getId(){
        return id;
    }
    
    @JsonProperty("ha")
        private final String handleAuth;

    public String getHandleAuth(){
        return handleAuth;
    }

    @JsonProperty("s")
        private final Object[] options;

    public Object[] getOptions(){
        return options;
    }
    
    @JsonProperty("cr")
        private final ShareData share;

    public ShareData getShare(){
        return share;
    }

    @JsonProperty("ok")
        private final String sharedKey;

    public String getSharedKey(){
        return sharedKey;
    }
}
