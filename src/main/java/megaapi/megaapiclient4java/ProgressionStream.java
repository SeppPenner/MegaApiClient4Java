package megaapi.megaapiclient4java;

import java.util.stream.Stream;

public class ProgressionStream implements Stream {

    private final Stream baseStream;
    private final IProgress<double> progress;
    private final long reportProgressChunkSize;

    private long chunkSize;

    public ProgressionStream(Stream baseStream, IProgress<double> progress, long reportProgressChunkSize) {
        this.baseStream = baseStream;
        this.progress = progress;
        this.reportProgressChunkSize = reportProgressChunkSize;
    }

    public int Read(byte[] array, int offset, int count) {
        int bytesRead = this.baseStream.Read(array, offset, count);
        this.ReportProgress(bytesRead);

        return bytesRead;
    }

    public void Write(byte[] buffer, int offset, int count) {
        this.baseStream.Write(buffer, offset, count);
    }

    protected void Dispose(boolean disposing) {
        base.Dispose(disposing);
        this.progress.Report(100);
    }

    public void Flush() {
        this.baseStream.Flush();
    }

    public long Seek(long offset, SeekOrigin origin) {
        return this.baseStream.Seek(offset, origin);
    }

    public   void SetLength(long value) {
        this.baseStream.SetLength(value);
    }

    public boolean CanRead  =  > this.baseStream.CanRead;

    public boolean CanSeek  =  > this.baseStream.CanSeek;

    public boolean CanWrite  =  > this.baseStream.CanWrite;

    public long Length =  > this.baseStream.Length;

    public long Position

    {
        get {
            return this.baseStream.Position;
        }
        set {
            this.baseStream.Position = value;
        }
    }

    private void ReportProgress(int count) {
        this.chunkSize += count;
        if (this.chunkSize >= this.reportProgressChunkSize) {
            this.chunkSize = 0;
            this.progress.Report(this.Position / (double) this.Length * 100);
        }
    }
}
