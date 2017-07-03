package megaapi.megaapiclient4java.JsonSerialization;

import MegaApiClient4Java.Node;
import java.util.ArrayList;

public class GetNodesResponse {

    private Node[] nodes;

    public Node[] getNodes() {
        return nodes;
    }

    @JsonProperty("f")
    private JRaw nodesSerialized;

    public JRaw getNodesSerialized() {
        return nodesSerialized;
    }

    @JsonProperty("ok")
    private ArrayList<SharedKey> sharedKeys;

    public ArrayList<SharedKey> getSharedKeys() {
        return sharedKeys;
    }

    @OnDeserialized
    public void OnDeserialized(StreamingContext ctx) {
        JsonSerializerSettings settings = new JsonSerializerSettings();

        // First Nodes deserialization to retrieve all shared keys
        settings.Context = new StreamingContext(StreamingContextStates.All, new []{this});
        JsonConvert.DeserializeObject<Node[]>
        (this.NodesSerialized.ToString(), settings
        );

      // Deserialize nodes
      settings.Context = new StreamingContext(StreamingContextStates.All, new []{this, ctx.Context});
        this.Nodes = JsonConvert.DeserializeObject < Node[] > (this.NodesSerialized.ToString(), settings
    

);
    }
}
