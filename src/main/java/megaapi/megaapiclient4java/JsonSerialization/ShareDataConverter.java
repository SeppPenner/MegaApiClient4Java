package megaapi.megaapiclient4java.JsonSerialization;

public class ShareDataConverter extends JsonConverter {

    @Override
    public void writeJson(JsonWriter writer, object value, JsonSerializer serializer) {
        ShareData data = (ShareData) value;
        if (data == null) {
            throw new ArgumentException("invalid data to serialize");
        }
        writer.WriteStartArray();

        writer.WriteStartArray();
        writer.WriteValue(data.NodeId);
        writer.WriteEndArray();

        writer.WriteStartArray();
        foreach(ShareDataItem item in data.Items
        
            ){
            writer.WriteValue(item.getNodeId());
        }
        writer.WriteEndArray();

        writer.WriteStartArray();
        int counter = 0;
        foreach(ShareDataItem item:
        data.Items
        
            ){
            writer.WriteValue(0);
            writer.WriteValue(counter++);
            writer.WriteValue(Crypto.EncryptKey(item.getData(), item.getKey()).ToBase64());
        }
        writer.WriteEndArray();

        writer.WriteEndArray();
    }

    @Override
    public object readJson(JsonReader reader, Type objectType, object existingValue, JsonSerializer serializer) {
        throw new NotImplementedException();
    }

    @Override
    public boolean canConvert(Type objectType) {
        return objectType == typeof(ShareData);
    }
}
