package megaapi.megaapiclient4java;


import java.net.URI;
import java.nio.charset.Charset;
import java.util.stream.Stream;
import megaapi.megaapiclient4java.Interfaces.IWebClient;

  public class WebClient implements IWebClient
  {
    private final int DefaultResponseTimeout = Timeout.Infinite;

    private final HttpClient httpClient = new HttpClient();

    public WebClient(int responseTimeout = DefaultResponseTimeout, ProductInfoHeaderValue userAgent = null)
    {
      this.BufferSize = Options.DefaultBufferSize;
      this.httpClient.Timeout = TimeSpan.FromMilliseconds(responseTimeout);
      this.httpClient.DefaultRequestHeaders.UserAgent.Add(userAgent ?? this.GenerateUserAgent());
    }

    public int BufferSize { get; set; }

    public String PostRequestJson(URI url, String jsonData)
    {
      using (MemoryStream jsonStream = new MemoryStream(jsonData.getBytes(Charset.forName("UTF-8"))))
      {
        return this.PostRequest(url, jsonStream, "application/json");
      }
    }

    public String PostRequestRaw(URI url, Stream dataStream)
    {
      return this.PostRequest(url, dataStream, "application/octet-stream");
    }

    public Stream GetRequestRaw(URI url)
    {
      return this.httpClient.GetStreamAsync(url).Result;
    }

    private String PostRequest(URI url, Stream dataStream, String contentType)
    {
      using (StreamContent content = new StreamContent(dataStream, this.BufferSize))
      {
        content.Headers.ContentType = new MediaTypeHeaderValue(contentType);
        using (HttpResponseMessage response = this.httpClient.PostAsync(url, content).Result)
        {
          using (Stream stream = response.Content.ReadAsStreamAsync().Result)
          {
            using (StreamReader streamReader = new StreamReader(stream, Encoding.UTF8))
            {
              return streamReader.ReadToEnd();
            }
          }
        }
      }
    }

    private ProductInfoHeaderValue GenerateUserAgent()
    {
      AssemblyName assemblyName = Assembly.GetExecutingAssembly().GetName();
      return new ProductInfoHeaderValue(assemblyName.Name, assemblyName.Version.ToString(2));
    }
  }
