package megaapi.megaapiclient4java.Interfaces;

import java.net.URI;
import java.util.Date;
import java.util.stream.Stream;
import javafx.concurrent.Task;
import megaapi.megaapiclient4java.JsonSerialization.AuthInfos;
import megaapi.megaapiclient4java.JsonSerialization.LogonSessionToken;

public interface IMegaApiClientAsync {

    Task<LogonSessionToken> LoginAsync(String email, String password);

    Task<LogonSessionToken> LoginAsync(AuthInfos authInfos);

    Task LoginAsync(LogonSessionToken logonSessionToken);

    Task LoginAnonymousAsync();

    Task LogoutAsync();

    Task<IAccountInformation> GetAccountInformationAsync();

    Task<Iterable<INode>> GetNodesAsync();

    Task<Iterable<INode>> GetNodesAsync(INode parent);

    Task<INode> CreateFolderAsync(String name, INode parent);

    Task DeleteAsync(INode node, boolean moveToTrash

    = true);

    Task<INode> MoveAsync(INode sourceNode, INode destinationParentNode);

    Task<INode> RenameAsync(INode sourceNode, String newName);

    Task<URI> GetDownloadLinkAsync(INode node);

    Task<Stream> DownloadAsync(INode node, IProgress<double> progress, CancellationToken

    ? cancellationToken = null);

    Task<Stream> DownloadAsync(URI uri, IProgress<double> progress, CancellationToken

    ? cancellationToken = null);

    Task DownloadFileAsync(INode node, String outputFile, IProgress<double> progress, CancellationToken

    ? cancellationToken = null);

    Task DownloadFileAsync(URI uri, string outputFile, IProgress<double> progress, CancellationToken

    ? cancellationToken = null);

    Task<INode> UploadAsync(Stream stream, string name, INode parent, IProgress<double> progress, Date

    ? modificationDate = null, CancellationToken? cancellationToken = null);

    Task<INode> UploadFileAsync(String filename, INode parent, IProgress<double> progress, CancellationToken

    ? cancellationToken = null);

    Task<INodeInfo> GetNodeFromLinkAsync(URI uri);

    Task<Iterable<INode>> GetNodesFromLinkAsync(URI uri);
}
