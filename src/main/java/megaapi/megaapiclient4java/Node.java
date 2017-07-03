package megaapi.MegaApiClient4Java;

import java.util.Date;
import megaapi.megaapiclient4java.Cryptography.Crypto;
import megaapi.megaapiclient4java.Enumerations.NodeType;
import megaapi.megaapiclient4java.Interfaces.INode;
import megaapi.megaapiclient4java.Interfaces.INodeCrypto;
import megaapi.megaapiclient4java.JsonSerialization.GetNodesResponse;
import megaapi.megaapiclient4java.JsonSerialization.SharedKey;
import megaapi.megaapiclient4java.NodeInfo;

public class Node extends NodeInfo implements INode, INodeCrypto {

    public Node() {
    }

    @JsonProperty("p")]
    private String parentId;
    
    public String getParentId(){
        return parentId;
    }


    @JsonProperty("u")
    private String owner;
    
    public String getOwner(){
        return owner;
    }

    @JsonProperty("su")
    private String sharingId;
    
    public String getSharingId(){
        return sharingId;
    }
    
    @JsonProperty("sk")
    private String sharingKey;
    
    public String getSharingKey(){
        return sharingKey;
    }

    @JsonIgnore
    private Date creationDate;
    
    public Date getCreationDate(){
        return creationDate;
    }

    @JsonIgnore
    private byte[] key;
    
    @Override
    public byte[] getKey(){
        return key;
    }
    
    @JsonIgnore
    private byte[] fullkey;
    
    @Override
    public byte[] getFullKey(){
        return fullkey;
    }
    
    @JsonIgnore
    private byte[] sharedkey;
    
    public byte[] getSharedkey(){
        return sharedkey;
    }

    @JsonIgnore
    private byte[] iv;
    
    @Override
    public byte[] getIv(){
        return iv;
    }

    @JsonIgnore
    private byte[] metaMac;
    
    @Override
    public byte[] getMetaMac(){
        return metaMac;
    }

    @JsonProperty("ts")
    public long SerializedCreationDate;

    @JsonProperty("a")
    public String SerializedAttributes;

    @JsonProperty("k")
    public String SerializedKey;

    @OnDeserialized
    public void OnDeserialized(StreamingContext ctx)
    {
      Object[] context = (Object[])ctx.Context;
      GetNodesResponse nodesResponse = (GetNodesResponse)context[0];
      if (context.length == 1)
      {
        // Add key from incoming sharing.
        if (sharingKey != null && nodesResponse.SharedKeys.Any(x => x.Id == id) == false)
        {
          nodesResponse.addSharedKey(new SharedKey(id, sharingKey));
        }
        return;
      }
      else
      {
        byte[] masterKey = (byte[])context[1];

        creationDate = serializedCreationDate.ToDateTime();

        if (type == NodeType.File || type == NodeType.Directory)
        {
          // There are cases where the SerializedKey property contains multiple keys separated with /
          // This can occur when a folder is shared and the parent is shared too.
          // Both keys are working so we use the first one
          String serializedKey = this.SerializedKey.Split('/')[0];
          int splitPosition = serializedKey.IndexOf(":", StringComparison.InvariantCulture);
          byte[] encryptedKey = serializedKey.substring(splitPosition + 1).FromBase64();

          // If node is shared, we need to retrieve shared masterkey
          if (nodesResponse.getSharedKeys() != null)
          {
            String handle = serializedKey.substring(0, splitPosition);
            SharedKey sharedKey = nodesResponse.SharedKeys.FirstOrDefault(x => x.Id == handle);
            if (sharedKey != null)
            {
              masterKey = Crypto.DecryptKey(sharedKey.getKey().FromBase64(), masterKey);
              if (type == NodeType.Directory)
              {
                sharedKey = masterKey;
              }
              else
              {
                sharedKey = Crypto.DecryptKey(encryptedKey, masterKey);
              }
            }
          }

          fullKey = Crypto.DecryptKey(encryptedKey, masterKey);

          if (type == NodeType.File)
          {
            byte[] currentIv, currentMetaMac, currentFileKey;
            Crypto.getPartsFromDecryptedKey(this.FullKey, out currentIv, out currentMetaMac, out currentFileKey);

            iv = currentIv;
            metaMac = currentMetaMac;
            key = currentFileKey;
          }
          else
          {
            key = fullKey;
          }

          attributes = Crypto.DecryptAttributes(this.SerializedAttributes.FromBase64(), key);
        }
      }
