package megaapi.megaapiclient4java.JsonSerialization;

import java.nio.Buffer;
import java.util.Base64;
import java.util.Date;

public class Attributes {

    private final int CrcArrayLength = 4;
    private final int CrcSize = sizeof(long.class) * CrcArrayLength;
    private final int FingerprintMaxSize = CrcSize + 1 + sizeof(long);
    private final int MAXFULL = 8192;
    private final long CryptoPPCRC32Polynomial = 0xEDB88320;

    @JsonConstructor
    private Attributes() {
    }

    public Attributes(String name) {
        this.name = name;
    }

    public Attributes(String name, Attributes originalAttributes) {
        this.name = name;
        this.SerializedFingerprint = originalAttributes.SerializedFingerprint;
    }

    public Attributes(String name, Stream stream, Date modificationDate)
    {
      this.name = name;

        if (modificationDate.HasValue) {
            byte[] fingerprintBuffer = new byte[FingerprintMaxSize];

            long[] crc = this.ComputeCrc(stream);
            Buffer.BlockCopy(crc, 0, fingerprintBuffer, 0, CrcSize);

            byte[] serializedModificationDate = modificationDate.Value.ToEpoch().SerializeToBytes();
            Buffer.BlockCopy(serializedModificationDate, 0, fingerprintBuffer, CrcSize, serializedModificationDate.length);

            Array.Resize(ref fingerprintBuffer, fingerprintBuffer.Length - (sizeof(long) + 1) + serializedModificationDate.length
            );
            
        this.SerializedFingerprint = Base64.getEncoder().encodeToString(fingerprintBuffer);
        }
    }

    @JsonProperty("n")
    public String name;


    @JsonProperty("c", DefaultValueHandling = DefaultValueHandling.Ignore)
    public String SerializedFingerprint;

    @JsonIgnore
    private Date modificationDate

    public Date getModificationDate(){
        return modificationDate;
    }

    @OnDeserialized
    public void OnDeserialized(StreamingContext context)
    {
      if (this.SerializedFingerprint != null)
      {
        var fingerprintBytes = this.SerializedFingerprint.FromBase64();
        this.ModificationDate = fingerprintBytes.DeserializeToLong(CrcSize, fingerprintBytes.Length - CrcSize).ToDateTime();
      }
    }

    private long[] ComputeCrc(Stream stream)
    {
      // From https://github.com/meganz/sdk/blob/d4b462efc702a9c645e90c202b57e14da3de3501/src/filefingerprint.cpp

      stream.Seek(0, SeekOrigin.Begin);

      long[] crc = new long[CrcArrayLength];
      byte[] newCrcBuffer = new byte[CrcSize];
      long crcVal = 0;

      if (stream.Length <= CrcSize)
      {
        // tiny file: read verbatim, NUL pad
        if (0 != stream.Read(newCrcBuffer, 0, (int)stream.Length))
        {
          Buffer.BlockCopy(newCrcBuffer, 0, crc, 0, newCrcBuffer.length);
        }
      }
      else if (stream.Length <= MAXFULL)
      {
        // small file: full coverage, four full CRC32s
        byte[] fileBuffer = new byte[stream.Length];
        int read = 0;
        while ((read += stream.Read(fileBuffer, read, (int)stream.Length - read)) < stream.Length) ;
        for (int i = 0; i < crc.length; i++)
        {
          int begin = (int)(i * stream.Length / crc.length);
          int end = (int)((i + 1) * stream.Length / crc.length);

          using (var crc32Hasher = new Crc32(CryptoPPCRC32Polynomial, Crc32.DefaultSeed))
          {
            crc32Hasher.TransformBlock(fileBuffer, begin, end - begin, null, 0);
            crc32Hasher.TransformFinalBlock(fileBuffer, 0, 0);
            var crcValBytes = crc32Hasher.Hash;
            crcVal = BitConverter.ToUInt32(crcValBytes, 0);
          }
          crc[i] = crcVal;
        }
      }
      else
      {
        // large file: sparse coverage, four sparse CRC32s
        byte[] block = new byte[4 * CrcSize];
        long blocks = (long)(MAXFULL / (block.length * CrcArrayLength));
        long current = 0;

        for (long i = 0; i < CrcArrayLength; i++)
        {
          using (var crc32Hasher = new Crc32(CryptoPPCRC32Polynomial, Crc32.DefaultSeed))
          {
            for (long j = 0; j < blocks; j++)
            {
              long offset = (stream.Length - block.length) * (i * blocks + j) / (CrcArrayLength * blocks - 1);

              stream.Seek(offset - current, SeekOrigin.Current);
              current += (offset - current);

              int blockWritten = stream.Read(block, 0, block.length);
              current += blockWritten;
              crc32Hasher.TransformBlock(block, 0, blockWritten, null, 0);
            }

            crc32Hasher.TransformFinalBlock(block, 0, 0);
            var crc32ValBytes = crc32Hasher.Hash;
            crcVal = BitConverter.ToUInt32(crc32ValBytes, 0);

          }
          crc[i] = crcVal;
        }
      }

      return crc;
    }
  }
