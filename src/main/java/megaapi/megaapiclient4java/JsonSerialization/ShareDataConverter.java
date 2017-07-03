package megaapi.megaapiclient4java.JsonSerialization;

import java.util.Base64;
import megaapi.megaapiclient4java.Cryptography.Crypto;
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
        for(ShareDataItem item:data.getItems()){
            writer.WriteValue(0);
            writer.WriteValue(counter++);
            byte[] encryptedKey = Crypto.EncryptKey(item.getData(), item.getKey());
            String encryptedKeyBase64 = Base64.getEncoder().encodeToString(encryptedKey);
            writer.WriteValue(encryptedKeyBase64);
        }
        writer.WriteEndArray();

        writer.WriteEndArray();
    }

    public Object readJson(JsonReader reader, Type objectType, Object existingValue, JsonSerializer serializer) {
        throw new NotImplementedException();
    }

    public boolean canConvert(Type objectType) {
        return objectType == typeof(ShareData);
    }
}
