package megaapi.megaapiclient4java.Interfaces;

public interface IStorageMetrics {
    
    public String NodeId();

    public long BytesUsed();

    public long FilesCount ();

    public long FoldersCount();
}