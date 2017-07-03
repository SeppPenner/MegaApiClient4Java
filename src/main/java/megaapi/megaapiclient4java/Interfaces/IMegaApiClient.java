package megaapi.megaapiclient4java.Interfaces;

import java.net.URI;
import java.util.Date;
import java.util.stream.Stream;
import megaapi.megaapiclient4java.JsonSerialization.AuthInfos;
import megaapi.megaapiclient4java.JsonSerialization.LogonSessionToken;

public interface IMegaApiClient {

    LogonSessionToken login(String email, String password);

    LogonSessionToken login(AuthInfos authInfos);

    void login(LogonSessionToken logonSessionToken);

    void loginAnonymous();

    void logout();

    IAccountInformation getAccountInformation();

    Iterable<INode> getNodes();

    Iterable<INode> getNodes(INode parent);

    void delete(INode node);
    
    void deleteNoMoveToTrash(INode node);

    INode createFolder(String name, INode parent);

    URI getDownloadLink(INode node);

    void downloadFile(INode node, String outputFile);

    void downloadFile(INode node, String outputFile, CancellationToken cancellationToken);

    void downloadFile(URI uri, String outputFile);

    void downloadFile(URI uri, String outputFile, CancellationToken cancellationToken);

    Stream download(INode node);

    Stream download(INode node, CancellationToken cancellationToken);

    Stream download(URI uri);

    Stream download(URI uri, CancellationToken cancellationToken);

    INodeInfo getNodeFromLink(URI uri);

    Iterable<INode> getNodesFromLink(URI uri);

    INode uploadFile(String filename, INode parent);

    INode uploadFile(String filename, INode parent, CancellationToken cancellationToken);

    INode upload(Stream stream, String name, INode parent, Date lastModifiedDate);

    INode upload(Stream stream, String name, INode parent, Date modificationDate, CancellationToken cancellationToken);

    INode move(INode node, INode destinationParentNode);

    INode rename(INode node, String newName);

    event EventHandler
    <ApiRequestFailedEventArgs
    > ApiRequestFailed ;

    boolean isLoggedIn();
}
