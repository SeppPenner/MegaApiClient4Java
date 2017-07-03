package megaapi.megaapiclient4java;

import java.util.stream.Stream;
import megaapi.megaapiclient4java.Exceptions.ArgumentNullException;

  public class CancellableStream implements Stream
  {
    private Stream stream;
    private final CancellationToken cancellationToken;

    public CancellableStream(Stream stream, CancellationToken cancellationToken) throws ArgumentNullException
    {
      if (stream == null)
      {
        throw new ArgumentNullException(stream.toString());
      }

      this.stream = stream;
      this.cancellationToken = cancellationToken;
    }

    public boolean canRead()
    {
        cancellationToken.ThrowIfCancellationRequested();
        return stream.canRead;
    }

    public boolean canSeek()
    {
        cancellationToken.ThrowIfCancellationRequested();
        return stream.CanSeek;
    }

    public boolean canWrite()
    {
        cancellationToken.ThrowIfCancellationRequested();
        return stream.CanWrite;
      }
    }

    public void Flush()
    {
      cancellationToken.ThrowIfCancellationRequested();
      stream.Flush();
    }

    public long Length()
    {
        cancellationToken.ThrowIfCancellationRequested();
        return stream.Length;
    }

    public long Position
    {
      get
      {
        this.cancellationToken.ThrowIfCancellationRequested();
        return this.stream.Position;
      }

      set
      {
        this.cancellationToken.ThrowIfCancellationRequested();
        this.stream.Position = value;
      }
    }

    public override int Read(byte[] buffer, int offset, int count)
    {
      this.cancellationToken.ThrowIfCancellationRequested();
      return this.stream.Read(buffer, offset, count);
    }

    public override long Seek(long offset, SeekOrigin origin)
    {
      this.cancellationToken.ThrowIfCancellationRequested();
      return this.stream.Seek(offset, origin);
    }

    public override void SetLength(long value)
    {
      this.cancellationToken.ThrowIfCancellationRequested();
      this.stream.SetLength(value);
    }

    public override void Write(byte[] buffer, int offset, int count)
    {
      this.cancellationToken.ThrowIfCancellationRequested();
      this.stream.Write(buffer, offset, count);
    }

    public override void Close()
    {
      this.stream?.Close();

      base.Close();
    }

    protected override void Dispose(bool disposing)
    {
      if (disposing)
      {
        this.stream?.Dispose();
        this.stream = null;
      }
    }
  }
}
