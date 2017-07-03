package megaapi.megaapiclient4java.JsonSerialization;

import megaapi.megaapiclient4java.Interfaces.IStorageMetrics;

public class StorageMetrics implements IStorageMetrics {

    public StorageMetrics(String nodeId, long[] metrics) {
        this.nodeId = nodeId;
        this.bytesUsed = metrics[0];
        this.filesCount = metrics[1];
        this.foldersCount = metrics[2];
    }

    private final String nodeId;

    private final long bytesUsed;

    private final long filesCount;

    private final long foldersCount;

    @Override
    public String NodeId() {
        return nodeId;
    }

    @Override
    public long BytesUsed() {
        return bytesUsed;
    }

    @Override
    public long FilesCount() {
        return filesCount;
    }

    @Override
    public long FoldersCount() {
        return foldersCount;
    }
}
