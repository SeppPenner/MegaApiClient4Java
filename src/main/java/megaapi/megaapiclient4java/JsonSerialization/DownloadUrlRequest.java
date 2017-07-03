package megaapi.megaapiclient4java.JsonSerialization;

import MegaApiClient4Java.PublicNode;
import megaapi.megaapiclient4java.Interfaces.INode;

public class DownloadUrlRequest extends RequestBase {

    public DownloadUrlRequest(INode node) 
        : base("g"){
        this.id = node.getId();

        PublicNode publicNode = (PublicNode) node;
        if (publicNode != null) {
            this.QueryArguments["n"] = publicNode.getShareId();
        }
    }

    public int getG() {
        return 1;
    }

    @JsonProperty("n")
    private String id;

    public String getId() {
        return id;
    }
}
