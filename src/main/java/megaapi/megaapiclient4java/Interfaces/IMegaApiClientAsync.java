package megaapi.megaapiclient4java.Interfaces;

import java.net.URI;
import java.util.Date;
import java.util.stream.Stream;
import javafx.concurrent.Task;
import megaapi.megaapiclient4java.JsonSerialization.AuthInfos;
import megaapi.megaapiclient4java.JsonSerialization.LogonSessionToken;

public interface IMegaApiClientAsync {

    Task<LogonSessionToken> loginAsync(String email, String password);

    Task<LogonSessionToken> loginAsync(AuthInfos authInfos);

    Task loginAsync(LogonSessionToken logonSessionToken);

    Task loginAnonymousAsync();

    Task logoutAsync();

    Task<IAccountInformation> getAccountInformationAsync();

    Task<Iterable<INode>> getNodesAsync();

    Task<Iterable<INode>> getNodesAsync(INode parent);

    Task<INode> createFolderAsync(String name, INode parent);

    Task deleteAsync(INode node);
    
    Task deleteAsyncNoMoveToTrash(INode node);

    Task<INode> moveAsync(INode sourceNode, INode destinationParentNode);

    Task<INode> renameAsync(INode sourceNode, String newName);

    Task<URI> getDownloadLinkAsync(INode node);

    Task<Stream> downloadAsync(INode node, IProgress<double> progress, CancellationToken cancellationToken);

    Task<Stream> downloadAsync(URI uri, IProgress<double> progress, CancellationToken cancellationToken);

    Task downloadFileAsync(INode node, String outputFile, IProgress<double> progress, CancellationToken cancellationToken);

    Task downloadFileAsync(URI uri, String outputFile, IProgress<double> progress, CancellationToken cancellationToken);

    Task<INode> uploadAsync(Stream stream, String name, INode parent, IProgress<double> progress, 
            Date modificationDate, CancellationToken cancellationToken);

    Task<INode> uploadFileAsync(String filename, INode parent, IProgress<double> progress, CancellationToken cancellationToken);

    Task<INodeInfo> getNodeFromLinkAsync(URI uri);

    Task<Iterable<INode>> getNodesFromLinkAsync(URI uri);
}
