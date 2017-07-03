package megaapi.megaapiclient4java.JsonSerialization;

import java.util.ArrayList;

@JsonConverter(typeof(ShareDataConverter))
public class ShareData {

    private final Iterable<ShareDataItem> items;

    public ShareData(String nodeId) {
        this.nodeId = nodeId;
        this.items = new ArrayList<>();
    }

    private final String nodeId;

    public String getNodeId() {
        return nodeId;
    }

    public Iterable<ShareDataItem> getItems() {
        return this.items;
    }

    public void AddItem(String nodeId, byte[] data, byte[] key) {
        ShareDataItem item = new ShareDataItem();
        item.setNodeId(nodeId);
        item.setData(data);
        item.setKey(key);
        this.items.add(item);
    }
}
