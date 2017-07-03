package megaapi.megaapiclient4java;

import java.net.URI;
import javafx.concurrent.Task;
import megaapi.megaapiclient4java.Interfaces.IMegaApiClientAsync;
import megaapi.megaapiclient4java.Interfaces.INode;
import megaapi.megaapiclient4java.JsonSerialization.AuthInfos;
import megaapi.megaapiclient4java.JsonSerialization.LogonSessionToken;

  public class MegaApiClientAsync implements IMegaApiClientAsync
  {
      private final MegaApiClient client = new MegaApiClient();
      
      @Override
    public Task<LogonSessionToken> loginAsync(String email, String password)
    {

        return Task<Integer> task = new Task<Integer>() {
            @Override protected Integer call() throws Exception {
               return client.login(email, password);
            }
        };
        
        
        

        
      return Task.Run(() => this.Login(email, password));
    }

    public Task<LogonSessionToken> LoginAsync(AuthInfos authInfos)
    {
      return Task.Run(() => this.Login(authInfos));
    }

    public Task LoginAsync(LogonSessionToken logonSessionToken)
    {
      return Task.Run(() => this.Login(logonSessionToken));
    }

    @Override
    public Task LoginAnonymousAsync()
    {
      return Task.Run(() => this.LoginAnonymous());
    }

    @Override
    public Task LogoutAsync()
    {
      return Task.Run(() => this.Logout());
    }

    public Task<IAccountInformation> GetAccountInformationAsync()
    {
      return Task.Run(() => this.GetAccountInformation());
    }

    public Task<IEnumerable<INode>> GetNodesAsync()
    {
      return Task.Run(() => this.GetNodes());
    }

    public Task<IEnumerable<INode>> GetNodesAsync(INode parent)
    {
      return Task.Run(() => this.GetNodes(parent));
    }

    @Override
    public Task<INode> CreateFolderAsync(String name, INode parent)
    {
        return Task.Run(() => this.CreateFolder(name, parent));
    }

    @Override
    public Task DeleteAsync(INode node)
    {
      return Task.Run(() => this.Delete(node, true));
    }
    
    @Override
    public Task DeleteAsyncNoMoveToTrash(INode node)
    {
      return Task.Run(() => this.Delete(node, false));
    }

    @Override
    public Task<INode> MoveAsync(INode sourceNode, INode destinationParentNode)
    {
      return Task.Run(() => this.Move(sourceNode, destinationParentNode));
    }

    public Task<INode> RenameAsync(INode sourceNode, string newName)
    {
      return Task.Run(() => this.Rename(sourceNode, newName));
    }

    public Task<Uri> GetDownloadLinkAsync(INode node)
    {
      return Task.Run(() => this.GetDownloadLink(node));
    }

    public Task<Stream> DownloadAsync(INode node, IProgress<double> progress, CancellationToken? cancellationToken = null)
    {
      return Task.Run(() =>
      {
        return (Stream)new ProgressionStream(this.Download(node, cancellationToken), progress, this.options.ReportProgressChunkSize);
      }, cancellationToken.GetValueOrDefault());
    }

    public Task<Stream> DownloadAsync(Uri uri, IProgress<double> progress, CancellationToken? cancellationToken = null)
    {
      return Task.Run(() =>
      {
        return (Stream)new ProgressionStream(this.Download(uri, cancellationToken), progress, this.options.ReportProgressChunkSize);
      }, cancellationToken.GetValueOrDefault());
    }

    public Task DownloadFileAsync(INode node, string outputFile, IProgress<double> progress, CancellationToken? cancellationToken = null)
    {
      return Task.Run(() =>
      {
        using (Stream stream = new ProgressionStream(this.Download(node, cancellationToken), progress, this.options.ReportProgressChunkSize))
        {
          this.SaveStream(stream, outputFile);
        }
      }, cancellationToken.GetValueOrDefault());
    }

    public Task DownloadFileAsync(Uri uri, string outputFile, IProgress<double> progress, CancellationToken? cancellationToken = null)
    {
      return Task.Run(() =>
      {
        if (string.IsNullOrEmpty(outputFile))
        {
          throw new ArgumentNullException("outputFile");
        }

        using (Stream stream = new ProgressionStream(this.Download(uri, cancellationToken), progress, this.options.ReportProgressChunkSize))
        {
          this.SaveStream(stream, outputFile);
        }
      }, cancellationToken.GetValueOrDefault());
    }

    public Task<INode> UploadAsync(Stream stream, string name, INode parent, IProgress<double> progress, DateTime? modificationDate = null, CancellationToken? cancellationToken = null)
    {
      return Task.Run(() =>
      {
        if (stream == null)
        {
          throw new ArgumentNullException("stream");
        }

        using (Stream progressionStream = new ProgressionStream(stream, progress, this.options.ReportProgressChunkSize))
        {
          return this.Upload(progressionStream, name, parent, modificationDate, cancellationToken);
        }
      }, cancellationToken.GetValueOrDefault());
    }

    public Task<INode> UploadFileAsync(string filename, INode parent, IProgress<double> progress, CancellationToken? cancellationToken = null)
    {
      return Task.Run(() =>
      {
        DateTime modificationDate = File.GetLastWriteTime(filename);
        using (Stream stream = new ProgressionStream(new FileStream(filename, FileMode.Open, FileAccess.Read), progress, this.options.ReportProgressChunkSize))
        {
          return this.Upload(stream, Path.GetFileName(filename), parent, modificationDate, cancellationToken);
        }
      }, cancellationToken.GetValueOrDefault());
    }

    public Task<INodeInfo> GetNodeFromLinkAsync(URI uri)
    {
      return Task.Run(() => this.GetNodeFromLink(uri));
    }

    public Task<IEnumerable<INode>> GetNodesFromLinkAsync(Uri uri)
    {
      return Task.Run(() => this.GetNodesFromLink(uri));
    }
  }
