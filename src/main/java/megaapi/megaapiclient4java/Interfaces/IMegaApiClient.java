package megaapi.megaapiclient4java.Interfaces;

import java.net.URI;
import java.util.stream.Stream;
import megaapi.megaapiclient4java.JsonSerialization.AuthInfos;
import megaapi.megaapiclient4java.JsonSerialization.LogonSessionToken;

public interface IMegaApiClient {

    LogonSessionToken Login(String email, String password);

    LogonSessionToken Login(AuthInfos authInfos);

    void Login(LogonSessionToken logonSessionToken);

    void LoginAnonymous();

    void Logout();

    IAccountInformation GetAccountInformation();

    Iterable<INode> GetNodes();

    Iterable<INode> GetNodes(INode parent);

    void Delete(INode node, boolean moveToTrash = true);

    INode CreateFolder(String name, INode parent);

    URI GetDownloadLink(INode node);

    void DownloadFile(INode node, String outputFile);

    void DownloadFile(INode node, String outputFile, CancellationToken? cancellationToken = null);

    void DownloadFile(URI uri, String outputFile);

    void DownloadFile(URI uri, String outputFile, CancellationToken? cancellationToken = null);

    Stream Download(INode node);

    Stream Download(INode node, CancellationToken? cancellationToken = null);

    Stream Download(URI uri);

    Stream Download(URI uri, CancellationToken? cancellationToken = null);

    INodeInfo GetNodeFromLink(URI uri);

    Iterable<INode> GetNodesFromLink(URI uri);

    INode UploadFile(String filename, INode parent);

    INode UploadFile(String filename, INode parent, CancellationToken? cancellationToken = null);

    INode Upload(Stream stream, String name, INode parent, DateTime? lastModifiedDate = null);

    INode Upload(Stream stream, String name, INode parent, DateTime? modificationDate = null, CancellationToken? cancellationToken = null);

    INode Move(INode node, INode destinationParentNode);

    INode Rename(INode node, String newName);
    
    event EventHandler<ApiRequestFailedEventArgs> ApiRequestFailed;

    boolean IsLoggedIn();
}