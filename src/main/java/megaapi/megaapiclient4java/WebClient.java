package megaapi.megaapiclient4java;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.stream.Stream;
import megaapi.megaapiclient4java.Enumerations.Timeout;
import megaapi.megaapiclient4java.Interfaces.IWebClient;

public class WebClient extends IWebClient{

    private final int DefaultResponseTimeout = Timeout.Infinite.getValue();
    private final int responseTimeout;
    private final String userAgent;

    public WebClient()
    {
        super.setBufferSize(new Options().DefaultBufferSize);
        userAgent = generateUserAgent();
        responseTimeout = DefaultResponseTimeout;
    }
    
    public WebClient(int responseTimeout, String userAgent)
    {
        super.setBufferSize(new Options().DefaultBufferSize);
        this.responseTimeout = responseTimeout;
        this.userAgent = userAgent;
    }

    public int BufferSize;

    @Override
    public String postRequestJson(URI url, String jsonData) {
        MemoryStream jsonStream = new MemoryStream(jsonData.getBytes(Charset.forName("UTF-8")));
        return this.PostRequest(url, jsonStream, "application/json");
    }

    @Override
    public String postRequestRaw(URI url, Stream dataStream) {
        return this.PostRequest(url, dataStream, "application/octet-stream");
    }

    @Override
    public Stream getRequestRaw(URI url) {
        HttpWebRequest request = this.CreateRequest(url);
        request.Method = "GET";
        return request.GetResponse().GetResponseStream();
    }

    private String postRequest(URI url, Stream dataStream, String contentType) {
        HttpWebRequest request = this.CreateRequest(url);
        request.ContentLength = dataStream.Length;
        request.Method = "POST";
        request.ContentType = contentType;

        using(Stream requestStream = request.GetRequestStream()
        
            )
      {
        dataStream.Position = 0;
            dataStream.CopyTo(requestStream, this.BufferSize);
        }

        using(HttpWebResponse response = (HttpWebResponse) request.GetResponse()
        
            )
      {
        using(Stream responseStream = response.GetResponseStream()
            
                )
        {
          using(StreamReader streamReader = new StreamReader(responseStream, Encoding.UTF8)
                
                    )
          {
            return streamReader.ReadToEnd();
                }
            }
        }
    }

    private HttpWebRequest createRequest(URI url) {
        HttpWebRequest request = (HttpWebRequest) WebRequest.Create(url);
        request.Timeout = this.responseTimeout;
        request.UserAgent = this.userAgent;

        return request;
    }

    private String generateUserAgent() {
        AssemblyName assemblyName = Assembly.GetExecutingAssembly().GetName();
        return String.Format("{0} v{1}", assemblyName.Name, assemblyName.Version.ToString(2));
    }
}
