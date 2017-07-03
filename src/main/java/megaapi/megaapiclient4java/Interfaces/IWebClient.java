package megaapi.megaapiclient4java.Interfaces;

import java.net.URI;
import java.util.stream.Stream;

public abstract class IWebClient {

    private int bufferSize;

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public abstract String PostRequestJson(URI url, String jsonData);

    public abstract String PostRequestRaw(URI url, Stream dataStream);

    public abstract Stream GetRequestRaw(URI url);
}
