package megaapi.megaapiclient4java.JsonSerialization;

public class ShareDataItem {

    private String nodeId;

    private byte[] Data;

    private byte[] Key;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public byte[] getData() {
        return Data;
    }

    public void setData(byte[] Data) {
        this.Data = Data;
    }

    public byte[] getKey() {
        return Key;
    }

    public void setKey(byte[] Key) {
        this.Key = Key;
    }
}
