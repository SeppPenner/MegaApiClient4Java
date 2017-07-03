package megaapi.megaapiclient4java.Interfaces;

public interface INodeCrypto {

    public byte[] getKey();

    public byte[] getSharedKey();

    public byte[] getIv();

    public byte[] getMetaMac();

    public byte[] getFullKey();
}
