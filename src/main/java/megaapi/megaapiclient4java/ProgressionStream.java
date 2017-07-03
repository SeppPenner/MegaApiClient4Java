package MegaApiClient4Java;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.BaseStream;
import java.util.stream.Collector;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
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

    public override

    int Read(byte[] array, int offset, int count) {
        int bytesRead = this.baseStream.Read(array, offset, count);
        this.ReportProgress(bytesRead);

        return bytesRead;
    }

    public override

    void Write(byte[] buffer, int offset, int count) {
        this.baseStream.Write(buffer, offset, count);
    }

    protected override

    void Dispose(bool disposing) {
        base.Dispose(disposing);
        this.progress.Report(100);
    }

    public override

    void Flush() {
        this.baseStream.Flush();
    }

    public override

    long Seek(long offset, SeekOrigin origin) {
        return this.baseStream.Seek(offset, origin);
    }

    public override

    void SetLength(long value) {
        this.baseStream.SetLength(value);
    }

    public override bool CanRead  =  > this.baseStream.CanRead;

    public override bool CanSeek  =  > this.baseStream.CanSeek;

    public override bool CanWrite  =  > this.baseStream.CanWrite;

    public override long Length =  > this.baseStream.Length;

    public override long Position

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
