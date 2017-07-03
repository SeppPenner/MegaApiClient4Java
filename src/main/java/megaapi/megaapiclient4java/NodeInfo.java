package megaapi.megaapiclient4java;

import MegaApiClient4Java.Interfaces.INodeInfo;

public class NodeInfo implements INodeInfo {

    protected NodeInfo() {
    }

    private NodeInfo(String id, DownloadUrlResponse downloadResponse, byte[] key) {
        this.Id = id;
        this.Attributes = Crypto.DecryptAttributes(downloadResponse.SerializedAttributes.FromBase64(), key);
        this.Size = downloadResponse.Size;
        this.Type = NodeType.File;
    }

    [
    JsonIgnore
    ]
    public string Name

    {
        get {
            return this.Attributes ?.Name;
        }
    }

    [

    JsonProperty(
    "s")]
    public long Size

    {get; protected set ;
}

[JsonProperty("t")]
    public NodeType Type { get; protected set; }

    [JsonProperty("h")]
    public string Id { get; private set; }

    [JsonIgnore]
    public Date ModificationDate
    {
      get { return this.Attributes?.ModificationDate; }
    }

    [JsonIgnore]
    public Attributes Attributes { get; protected set; }

    @Override
        public bool equals(INodeInfo other)
    {
      return other != null && this.Id == other.Id;
    }

    @Override
        public override int hashCode()
    {
      return this.Id.getHashCode();
    }

    @Override
        public boolean equals(object obj){
      return this.equals((INodeInfo)obj);
    }

  }
}
