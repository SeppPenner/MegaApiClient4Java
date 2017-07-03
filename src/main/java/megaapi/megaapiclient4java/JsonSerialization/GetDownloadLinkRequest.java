package megaapi.megaapiclient4java.JsonSerialization;

import megaapi.megaapiclient4java.Interfaces.INode;

public class GetDownloadLinkRequest extends RequestBase {

    public GetDownloadLinkRequest(INode node) 
        : base("l"){
      this.id = node.getId();
    }

    [

    JsonProperty(
    "n")]
    private String id;

    public String getId() {
        return id;
    }
}
