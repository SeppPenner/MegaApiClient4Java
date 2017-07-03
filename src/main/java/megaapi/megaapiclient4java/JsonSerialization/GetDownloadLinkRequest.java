package megaapi.megaapiclient4java.JsonSerialization;

import megaapi.megaapiclient4java.Interfaces.INode;

public class GetDownloadLinkRequest extends RequestBase {

    public GetDownloadLinkRequest(INode node) 
        {
            super("l");
      this.id = node.getId();
    }

    @JsonProperty("n")
    private final String id;

    public String getId() {
        return id;
    }
}
