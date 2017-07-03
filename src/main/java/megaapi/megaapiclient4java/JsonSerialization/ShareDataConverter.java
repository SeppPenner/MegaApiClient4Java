package megaapi.megaapiclient4java.JsonSerialization;

import megaapi.megaapiclient4java.Exceptions.ArgumentException;

public class ShareDataConverter extends JsonConverter {

    @Override
    public void writeJson(JsonWriter writer, Object value, JsonSerializer serializer) throws ArgumentException {
        ShareData data = (ShareData) value;
        if (data == null) {
            throw new ArgumentException("invalid data to serialize");
        }
        writer.WriteStartArray();

        writer.WriteStartArray();
        writer.WriteValue(data.getNodeId());
        writer.WriteEndArray();

        writer.WriteStartArray();
        for(ShareDataItem item : data.getItems()
        
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
    public Object readJson(JsonReader reader, Type objectType, Object existingValue, JsonSerializer serializer) {
        throw new NotImplementedException();
    }

    @Override
    public boolean canConvert(Type objectType) {
        return objectType == typeof(ShareData);
    }
}
