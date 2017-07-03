package megaapi.megaapiclient4java;

import java.util.Date;
import megaapi.MegaApiClient4Java.Node;
import megaapi.megaapiclient4java.Enumerations.NodeType;
import megaapi.megaapiclient4java.Interfaces.INode;
import megaapi.megaapiclient4java.Interfaces.INodeCrypto;
import megaapi.megaapiclient4java.Interfaces.INodeInfo;

public class PublicNode implements INode, INodeCrypto {

    private final Node node;
    private final String shareId;

    private PublicNode(Node node, String shareId) {
        this.node = node;
        this.shareId = shareId;
    }

    public String getShareId() {
        return shareId;
    }

    public boolean equals(INodeInfo other) {
        return this.node.equals(other) && (this.shareId == null ? ((PublicNode) other).getShareId()
                == null : this.shareId.equals(((PublicNode) other).getShareId()));
    }

    @Override
    public long getSize() {
        return this.node.getSize();
    }

    @Override
    public String getName() {
        return this.node.getName();
    }

    public Date getModificationDate{
        return node.getModificationDate();
    }

    @Override
    public String getId() {
        return this.node.getId();
    }

    @Override
    public String getParentId() {
        return this.node.getParentId();
    }

    @Override
    public String getOwner() {
        return this.node.getOwner();
    }

    @Override
    public NodeType getType() {
        return this.node.getType();
    }

    @Override
    public Date getCreationDate(){
        return this.node.getCreationDate();
    }

    @Override
    public byte[] getKey() {
        return node.getKey();
    }

    @Override
    public byte[] getSharedKey() {
        return node.getSharedKey();
    }

    @Override
    public byte[] getIv() {
        return node.getIv();
    }

    @Override
    public byte[] getMetaMac() {
        return node.getMetaMac();
    }

    @Override
    public byte[] getFullKey() {
        return node.getFullKey();
    }

    @Override
    public Date getModificationDate() {
        throw node.getModificationDate();
    }
}
