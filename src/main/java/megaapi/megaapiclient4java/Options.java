package MegaApiClient4Java;

public class Options{
    
    public final String DefaultApplicationKey = "axhQiYyQ";
    public final boolean DefaultSynchronizeApiRequests = true;
    public final int DefaultApiRequestAttempts = 60;
    public final int DefaultApiRequestDelay = 500;
    public final float DefaultApiRequestDelayExponentialFactor = 1.5f;
    public final int DefaultBufferSize = 8192;
    public final int DefaultChunksPackSize = 1024 * 1024;
    public final long DefaultReportProgressChunkSize = 1024 * 50;

    public Options(){
        this.applicationKey = DefaultApplicationKey;
        this.synchronizeApiRequests = DefaultSynchronizeApiRequests;
        this.apiRequestAttempts = DefaultApiRequestAttempts;
        this.apiRequestDelay = DefaultApiRequestDelay;
        this.apiRequestDelayExponentialFactor = DefaultApiRequestDelayExponentialFactor;
        this.bufferSize = DefaultBufferSize;
        this.chunksPackSize = DefaultChunksPackSize;
        this.reportProgressChunkSize = DefaultReportProgressChunkSize;
    }
    
    public Options(String applicationKey, boolean synchronizeApiRequests, int apiRequestAttempts,
        int apiRequestDelay, float apiRequestDelayExponentialFactor, int bufferSize, int chunksPackSize,
        long reportProgressChunkSize){
            this.applicationKey = applicationKey;
            this.synchronizeApiRequests = synchronizeApiRequests;
            this.apiRequestAttempts = apiRequestAttempts;
            this.apiRequestDelay = apiRequestDelay;
            this.apiRequestDelayExponentialFactor = apiRequestDelayExponentialFactor;
            this.bufferSize = bufferSize;
            this.chunksPackSize = chunksPackSize;
            this.reportProgressChunkSize = reportProgressChunkSize;
    }

    private final String applicationKey;
    
    public String getApplicationKey() {
        return applicationKey;
    }

    private final boolean synchronizeApiRequests;
    
    public boolean getSynchronizeApiRequests() {
        return synchronizeApiRequests;
    }
    
    private final int apiRequestAttempts;
    
    public int getApiRequestAttempts() {
        return apiRequestAttempts;
    }
    
    private final int apiRequestDelay;
    
    public int getApiRequestDelay() {
        return apiRequestDelay;
    }

    private final float apiRequestDelayExponentialFactor;
    
    public float getApiRequestDelayExponentialFactor() {
        return apiRequestDelayExponentialFactor;
    }
    
    // Size of the buffer used when downloading files
    // This value has an impact on the progression.
    // A lower value means more progression reports but a possible higher CPU usage
    private final int bufferSize;
    
    public int getBufferSize() {
        return bufferSize;
    }

    // Upload is splitted in multiple fragments (useful for big uploads)
    // The size of the fragments is defined by mega.nz and are the following:
    // 0 / 128K / 384K / 768K / 1280K / 1920K / 2688K / 3584K / 4608K / ... (every 1024 KB) / EOF
    // The upload method tries to upload multiple fragments at once.
    // Fragments are merged until the total size reaches this value.
    // The special value -1 merges all chunks in a single fragment and a single upload
    private final int chunksPackSize;
    
    public int getChunksPackSize() {
        return chunksPackSize;
    }
    
    private final long reportProgressChunkSize;
    
    public long getReportProgressChunkSize() {
        return reportProgressChunkSize;
    }
}