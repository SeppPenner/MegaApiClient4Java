package megaapi.megaapiclient4java;

import java.lang.reflect.Array;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Base64;
import megaapi.megaapiclient4java.Interfaces.IMegaApiClient;
import megaapi.megaapiclient4java.Interfaces.IWebClient;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import megaapi.megaapiclient4java.Cryptography.BigInteger;
import megaapi.megaapiclient4java.Cryptography.Crypto;
import megaapi.megaapiclient4java.Enumerations.NodeType;
import megaapi.megaapiclient4java.Exceptions.ApiException;
import megaapi.megaapiclient4java.Exceptions.ArgumentException;
import megaapi.megaapiclient4java.Exceptions.ArgumentNullException;
import megaapi.megaapiclient4java.Exceptions.NotSupportedException;
import megaapi.megaapiclient4java.Exceptions.UploadException;
import megaapi.megaapiclient4java.Interfaces.IAccountInformation;
import megaapi.megaapiclient4java.Interfaces.INode;
import megaapi.megaapiclient4java.Interfaces.INodeCrypto;
import megaapi.megaapiclient4java.Interfaces.INodeInfo;
import megaapi.megaapiclient4java.JsonSerialization.AccountInformationRequest;
import megaapi.megaapiclient4java.JsonSerialization.AccountInformationResponse;
import megaapi.megaapiclient4java.JsonSerialization.AnonymousLoginRequest;
import megaapi.megaapiclient4java.JsonSerialization.Attributes;
import megaapi.megaapiclient4java.JsonSerialization.AuthInfos;
import megaapi.megaapiclient4java.JsonSerialization.CreateNodeRequest;
import megaapi.megaapiclient4java.JsonSerialization.DeleteRequest;
import megaapi.megaapiclient4java.JsonSerialization.DownloadUrlRequest;
import megaapi.megaapiclient4java.JsonSerialization.DownloadUrlResponse;
import megaapi.megaapiclient4java.JsonSerialization.GetDownloadLinkRequest;
import megaapi.megaapiclient4java.JsonSerialization.GetNodesRequest;
import megaapi.megaapiclient4java.JsonSerialization.GetNodesResponse;
import megaapi.megaapiclient4java.JsonSerialization.LoginRequest;
import megaapi.megaapiclient4java.JsonSerialization.LoginResponse;
import megaapi.megaapiclient4java.JsonSerialization.LogonSessionToken;
import megaapi.megaapiclient4java.JsonSerialization.LogoutRequest;
import megaapi.megaapiclient4java.JsonSerialization.MoveRequest;
import megaapi.megaapiclient4java.JsonSerialization.RenameRequest;
import megaapi.megaapiclient4java.JsonSerialization.RequestBase;
import megaapi.megaapiclient4java.JsonSerialization.ShareNodeRequest;

public class MegaApiClient implements IMegaApiClient{
    private static URI BaseApiUri;
    private static URI BaseUri;

    private final Options options;
    private final IWebClient webClient;

    private final Object apiRequestLocker = new Object();

    private Node trashNode;
    private String sessionId;
    private byte[] masterKey;
    private long sequenceIndex = (long)(Long.MAX_VALUE * new Random().nextDouble());
    private boolean authenticatedLogin;

    /// <summary>
    /// Instantiate a new <see cref="MegaApiClient" /> object with default <see cref="Options"/> and default <see cref="IWebClient"/> 
    /// </summary>
    public MegaApiClient()
    {
        try {
            options = new Options();
            webClient = new WebClient();
            BaseApiUri = new URI("https://g.api.mega.co.nz/cs");
            BaseUri = new URI("https://mega.nz");
        } catch (URISyntaxException ex) {
            Logger.getLogger(MegaApiClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /// <summary>
    /// Instantiate a new <see cref="MegaApiClient" /> object with custom <see cref="Options" /> and default <see cref="IWebClient"/> 
    /// </summary>
    public MegaApiClient(Options options)
    {
        try {
            this.options = options;
            webClient = new WebClient();
            BaseApiUri = new URI("https://g.api.mega.co.nz/cs");
            BaseUri = new URI("https://mega.nz");
        } catch (URISyntaxException ex) {
            Logger.getLogger(MegaApiClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /// <summary>
    /// Instantiate a new <see cref="MegaApiClient" /> object with default <see cref="Options" /> and custom <see cref="IWebClient"/> 
    /// </summary>
    public MegaApiClient(IWebClient webClient)
    {
        try {
            options = new Options();
            this.webClient = webClient;
            BaseApiUri = new URI("https://g.api.mega.co.nz/cs");
            BaseUri = new URI("https://mega.nz");
        } catch (URISyntaxException ex) {
            Logger.getLogger(MegaApiClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /// <summary>
    /// Instantiate a new <see cref="MegaApiClient" /> object with custom <see cref="Options"/> and custom <see cref="IWebClient" />
    /// </summary>
    public MegaApiClient(Options options, IWebClient webClient) throws ArgumentNullException
    {
      if (options == null)
      {
        throw new ArgumentNullException(options.toString());
      }

      if (webClient == null)
      {
        throw new ArgumentNullException(webClient.toString());
      }

      this.options = options;
      this.webClient = webClient;
      this.webClient.setBufferSize(options.getBufferSize());
    }

    /// <summary>
    /// Generate authentication informations and store them in a serializable object to allow persistence
    /// </summary>
    /// <param name="email">email</param>
    /// <param name="password">password</param>
    /// <returns><see cref="AuthInfos" /> object containing encrypted data</returns>
    /// <exception cref="ArgumentNullException">email or password is null</exception>
    public AuthInfos generateAuthInfos(String email, String password) throws ArgumentNullException
    {
      if (email != null && !email.isEmpty())
      {
        throw new ArgumentNullException("email");
      }

      
      if (password != null && !password.isEmpty())
      {
        throw new ArgumentNullException("password");
      }

      // Retrieve password as UTF8 byte array
      byte[] passwordBytes = password.getBytes(Charset.forName("UTF-8"));

      // Encrypt password to use password as key for the hash
      byte[] passwordAesKey = PrepareKey(passwordBytes);

      
      // Hash email and password to decrypt master key on Mega servers
      String hash = generateHash(email.toLowerCase(), passwordAesKey);

      return new AuthInfos(email, hash, passwordAesKey);
    }

    public event EventHandler<ApiRequestFailedEventArgs> ApiRequestFailed;

    public boolean IsLoggedIn()
    {
      return this.sessionId != null;
    }

    /// <summary>
    /// Login to Mega.co.nz service using email/password credentials
    /// </summary>
    /// <param name="email">email</param>
    /// <param name="password">password</param>
    /// <exception cref="ApiException">Service is not available or credentials are invalid</exception>
    /// <exception cref="ArgumentNullException">email or password is null</exception>
    /// <exception cref="NotSupportedException">Already logged in</exception>
    @Override
    public LogonSessionToken login(String email, String password)
    {
        try {
            return this.Login(generateAuthInfos(email, password));
        } catch (ArgumentNullException ex) {
            Logger.getLogger(MegaApiClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /// <summary>
    /// Login to Mega.co.nz service using hashed credentials
    /// </summary>
    /// <param name="authInfos">Authentication informations generated by <see cref="GenerateAuthInfos"/> method</param>
    /// <exception cref="ApiException">Service is not available or authInfos is invalid</exception>
    /// <exception cref="ArgumentNullException">authInfos is null</exception>
    /// <exception cref="NotSupportedException">Already logged in</exception>
    public LogonSessionToken Login(AuthInfos authInfos) throws ArgumentNullException
    {
      if (authInfos == null)
      {
        throw new ArgumentNullException("authInfos");
      }

        tryEnsureLoggedOut();
      this.authenticatedLogin = true;

      // Request Mega Api
      LoginRequest currentRequest = new LoginRequest(authInfos.getEmail(), authInfos.getHash());
      LoginResponse response = this.Request<LoginResponse>(currentRequest);

      // Decrypt master key using our password key
      byte[] cryptedMasterKey = response.getMasterKey().FromBase64();
      this.masterKey = Crypto.DecryptKey(cryptedMasterKey, authInfos.getPasswordAesKey());

      // Decrypt RSA private key using decrypted master key
      byte[] cryptedRsaPrivateKey = response.getPrivateKey().FromBase64();
      BigInteger[] rsaPrivateKeyComponents = Crypto.GetRsaPrivateKeyComponents(cryptedRsaPrivateKey, this.masterKey);

      // Decrypt session id
      byte[] encryptedSid = response.getSessionId().FromBase64();
      byte[] sid = Crypto.RsaDecrypt(encryptedSid.FromMPINumber(), rsaPrivateKeyComponents[0], rsaPrivateKeyComponents[1], rsaPrivateKeyComponents[2]);

      // Session id contains only the first 58 base64 characters
      String encryptedSidBase64 = Base64.getEncoder().encodeToString(sid);
      this.sessionId = encryptedSidBase64.substring(0, 58);

      return new LogonSessionToken(this.sessionId, this.masterKey);
    }

    @Override
    public void login(LogonSessionToken logonSessionToken)
    {
        try {
            this.ensureLoggedOut();
        } catch (NotSupportedException ex) {
            Logger.getLogger(MegaApiClient.class.getName()).log(Level.SEVERE, null, ex);
        }
      this.authenticatedLogin = true;
      this.sessionId = logonSessionToken.getSessionId();
      this.masterKey = logonSessionToken.getMasterKey();
    }

    /// <summary>
    /// Login anonymously to Mega.co.nz service
    /// </summary>
    /// <exception cref="ApiException">Throws if service is not available</exception>
    public void LoginAnonymous()
    {
      this.ensureLoggedOut();
      this.authenticatedLogin = false;

      Random random = new Random();

      // Generate random master key
      this.masterKey = new byte[16];
      random.nextBytes(this.masterKey);

      // Generate a random password used to encrypt the master key
      byte[] passwordAesKey = new byte[16];
      random.nextBytes(passwordAesKey);

      // Generate a random session challenge
      byte[] sessionChallenge = new byte[16];
      random.nextBytes(sessionChallenge);

      byte[] encryptedMasterKey = Crypto.EncryptAes(this.masterKey, passwordAesKey);

      // Encrypt the session challenge with our generated master key
      byte[] encryptedSessionChallenge = Crypto.EncryptAes(sessionChallenge, this.masterKey);
      byte[] encryptedSession = new byte[32];
      Array.Copy(sessionChallenge, 0, encryptedSession, 0, 16);
      Array.Copy(encryptedSessionChallenge, 0, encryptedSession, 16, encryptedSessionChallenge.length);

      // Request Mega Api to obtain a temporary user handle
      String encryptedMasterKeyBase64 = Base64.getEncoder().encodeToString(encryptedMasterKey);
      String encryptedSessionBase64 = Base64.getEncoder().encodeToString(encryptedSession);

      AnonymousLoginRequest currentRequest = new AnonymousLoginRequest(encryptedMasterKeyBase64, encryptedSessionBase64);
      String userHandle = this.Request(currentRequest);

      // Request Mega Api to retrieve our temporary session id
      LoginRequest request2 = new LoginRequest(userHandle, null);
      LoginResponse response2 = this.Request<LoginResponse>(request2);

      this.sessionId = response2.getTemporarySessionId();
    }

    /// <summary>
    /// Logout from Mega.co.nz service
    /// </summary>
    /// <exception cref="NotSupportedException">Not logged in</exception>
    public void Logout()
    {
      tryEnsureLoggedIn();

      if (this.authenticatedLogin == true)
      {
        this.Request(new LogoutRequest());
      }

      // Reset values retrieved by Login methods
      this.masterKey = null;
      this.sessionId = null;
    }

    /// <summary>
    /// Retrieve account (quota) information
    /// </summary>
    /// <returns>An object containing account information</returns>
    /// <exception cref="NotSupportedException">Not logged in</exception>
    /// <exception cref="ApiException">Mega.co.nz service reports an error</exception>
    public IAccountInformation GetAccountInformation()
    {
      tryEnsureLoggedIn();

      AccountInformationRequest currentRequest = new AccountInformationRequest();
      return this.Request<AccountInformationResponse>(currentRequest);
    }

    /// <summary>
    /// Retrieve all filesystem nodes
    /// </summary>
    /// <returns>Flat representation of all the filesystem nodes</returns>
    /// <exception cref="NotSupportedException">Not logged in</exception>
    /// <exception cref="ApiException">Mega.co.nz service reports an error</exception>
    public Iterable<INode> GetNodes()
    {
      tryEnsureLoggedIn();

      GetNodesRequest currentRequest = new GetNodesRequest();
      GetNodesResponse response = this.Request<GetNodesResponse>(currentRequest, this.masterKey);

      Node[] nodes = response.getNodes();
      if (this.trashNode == null)
      {
        this.trashNode = nodes.First(n => n.Type == NodeType.Trash);
      }

      return nodes.Distinct().OfType<INode>();
    }

    /// <summary>
    /// Retrieve children nodes of a parent node
    /// </summary>
    /// <returns>Flat representation of children nodes</returns>
    /// <exception cref="NotSupportedException">Not logged in</exception>
    /// <exception cref="ApiException">Mega.co.nz service reports an error</exception>
    /// <exception cref="ArgumentNullException">Parent node is null</exception>
    public Iterable<INode> GetNodes(INode parent) throws ArgumentNullException
    {
      if (parent == null)
      {
        throw new ArgumentNullException("parent");
      }

      return this.GetNodes().Where(n => n.ParentId == parent.Id);
    }

    /// <summary>
    /// Delete a node from the filesytem
    /// </summary>
    /// <remarks>
    /// You can only delete <see cref="NodeType.Directory" /> or <see cref="NodeType.File" /> node
    /// </remarks>
    /// <param name="node">Node to delete</param>
    /// <param name="moveToTrash">Moved to trash if true, Permanently deleted if false</param>
    /// <exception cref="NotSupportedException">Not logged in</exception>
    /// <exception cref="ApiException">Mega.co.nz service reports an error</exception>
    /// <exception cref="ArgumentNullException">node is null</exception>
    /// <exception cref="ArgumentException">node is not a directory or a file</exception>
    public void Delete(INode node, boolean moveToTrash) throws ArgumentNullException, ArgumentException
    {
      if (node == null)
      {
        throw new ArgumentNullException("node");
      }

      NodeType nodesType = node.getType();
      if (nodesType != NodeType.Directory && nodesType != NodeType.File)
      {
        throw new ArgumentException("Invalid node type");
      }

      tryEnsureLoggedIn();

      if (moveToTrash)
      {
        this.Move(node, this.trashNode);
      }
      else
      {
        this.Request(new DeleteRequest(node));
      }
    }

    /// <summary>
    /// Create a folder on the filesytem
    /// </summary>
    /// <param name="name">Folder name</param>
    /// <param name="parent">Parent node to attach created folder</param>
    /// <exception cref="NotSupportedException">Not logged in</exception>
    /// <exception cref="ApiException">Mega.co.nz service reports an error</exception>
    /// <exception cref="ArgumentNullException">name or parent is null</exception>
    /// <exception cref="ArgumentException">parent is not valid (all types are allowed expect <see cref="NodeType.File" />)</exception>
    public INode CreateFolder(String name, INode parent) throws ArgumentNullException, ArgumentException
    {
      if (String.IsNullOrEmpty(name))
      {
        throw new ArgumentNullException("name");
      }

      if (parent == null)
      {
        throw new ArgumentNullException("parent");
      }

      if (parent.getType() == NodeType.File)
      {
        throw new ArgumentException("Invalid parent node");
      }

      tryEnsureLoggedIn();

      byte[] currentKey = Crypto.CreateAesKey();
      byte[] attributes = Crypto.EncryptAttributes(new Attributes(name), currentKey);
      byte[] encryptedKey = Crypto.EncryptAes(currentKey, this.masterKey);

      String attributesBase64 = Base64.getEncoder().encodeToString(attributes);
      String encryptedKeyBase64 = Base64.getEncoder().encodeToString(encryptedKey);
      
      
      CreateNodeRequest currentRequest = CreateNodeRequest.CreateFolderNodeRequest(parent, attributesBase64, 
              encryptedKeyBase64, currentKey);
      GetNodesResponse response = this.Request<GetNodesResponse>(request, masterKey);
      return response.getNodes()[0];
    }

    /// <summary>
    /// Retrieve an url to download specified node
    /// </summary>
    /// <param name="node">Node to retrieve the download link (only <see cref="NodeType.File" /> or <see cref="NodeType.Directory" /> can be downloaded)</param>
    /// <returns>Download link to retrieve the node with associated key</returns>
    /// <exception cref="NotSupportedException">Not logged in</exception>
    /// <exception cref="ApiException">Mega.co.nz service reports an error</exception>
    /// <exception cref="ArgumentNullException">node is null</exception>
    /// <exception cref="ArgumentException">node is not valid (only <see cref="NodeType.File" /> or <see cref="NodeType.Directory" /> can be downloaded)</exception>
    public URI GetDownloadLink(INode node) throws ArgumentNullException, ArgumentException
    {
      if (node == null)
      {
        throw new ArgumentNullException("node");
      }

      NodeType nodesType = node.getType();
      if (nodesType != NodeType.File && nodesType != NodeType.Directory)
      {
        throw new ArgumentException("Invalid node");
      }

      tryEnsureLoggedIn();

      if (nodesType == NodeType.Directory)
      {
        // Request an export share on the node or we will receive an AccessDenied
        this.Request(new ShareNodeRequest(node, this.masterKey, this.GetNodes()));

        node = this.GetNodes().First(x => x.Equals(node));
      }

      INodeCrypto nodeCrypto = (INodeCrypto) node;
      if (nodeCrypto == null)
      {
        throw new ArgumentException("node must implement INodeCrypto");
      }

      GetDownloadLinkRequest currentRequest = new GetDownloadLinkRequest(node);
      String response = this.Request<String>(currentRequest);

      String nodeCryptoFullKeyBase64 = Base64.getEncoder().encodeToString(nodeCrypto.getFullKey());
      String nodeCryptoSharedKeyBase64 = Base64.getEncoder().encodeToString(nodeCrypto.getSharedKey());
      
      return new URI(BaseUri, String.format(
          "/#{0}!{1}!{2}",
          nodesType == NodeType.Directory ? "F" : "",
          response,
          nodesType == NodeType.Directory ? nodeCryptoSharedKeyBase64 : nodeCryptoFullKeyBase64));
    }

    /// <summary>
    /// Download a specified node and save it to the specified file
    /// </summary>
    /// <param name="node">Node to download (only <see cref="NodeType.File" /> can be downloaded)</param>
    /// <param name="outputFile">File to save the node to</param>
    /// <exception cref="NotSupportedException">Not logged in</exception>
    /// <exception cref="ApiException">Mega.co.nz service reports an error</exception>
    /// <exception cref="ArgumentNullException">node or outputFile is null</exception>
    /// <exception cref="ArgumentException">node is not valid (only <see cref="NodeType.File" /> can be downloaded)</exception>
    /// <exception cref="DownloadException">Checksum is invalid. Downloaded data are corrupted</exception>
    public void DownloadFile(INode node, String outputFile, CancellationToken cancellationToken)
    {
      if (node == null)
      {
        throw new ArgumentNullException("node");
      }

      if (String.IsNullOrEmpty(outputFile))
      {
        throw new ArgumentNullException("outputFile");
      }

      using (Stream stream = this.Download(node, cancellationToken))
      {
        this.SaveStream(stream, outputFile);
      }
    }

    /// <summary>
    /// Download a specified Uri from Mega and save it to the specified file
    /// </summary>
    /// <param name="uri">Uri to download</param>
    /// <param name="outputFile">File to save the Uri to</param>
    /// <exception cref="NotSupportedException">Not logged in</exception>
    /// <exception cref="ApiException">Mega.co.nz service reports an error</exception>
    /// <exception cref="ArgumentNullException">uri or outputFile is null</exception>
    /// <exception cref="ArgumentException">Uri is not valid (id and key are required)</exception>
    /// <exception cref="DownloadException">Checksum is invalid. Downloaded data are corrupted</exception>
    public void DownloadFile(URI uri, String outputFile, CancellationToken cancellationToken) throws ArgumentNullException
    {
      if (uri == null)
      {
        throw new ArgumentNullException("uri");
      }

      if (String.IsNullOrEmpty(outputFile))
      {
        throw new ArgumentNullException("outputFile");
      }


        using (Stream stream = this.Download(uri, cancellationToken))
      {
        this.SaveStream(stream, outputFile);
      }
    }

    /// <summary>
    /// Retrieve a Stream to download and decrypt the specified node
    /// </summary>
    /// <param name="node">Node to download (only <see cref="NodeType.File" /> can be downloaded)</param>
    /// <exception cref="NotSupportedException">Not logged in</exception>
    /// <exception cref="ApiException">Mega.co.nz service reports an error</exception>
    /// <exception cref="ArgumentNullException">node or outputFile is null</exception>
    /// <exception cref="ArgumentException">node is not valid (only <see cref="NodeType.File" /> can be downloaded)</exception>
    /// <exception cref="DownloadException">Checksum is invalid. Downloaded data are corrupted</exception>
    public Stream Download(INode node, CancellationToken? cancellationToken = null) throws ArgumentException throws ArgumentNullException
    {
      if (node == null)
      {
        throw new ArgumentNullException("node");
      }

      if (node.getType() != NodeType.File)
      {
        throw new ArgumentException("Invalid node");
      }

      INodeCrypto nodeCrypto = (INodeCrypto) node ;
      if (nodeCrypto == null)
      {
        throw new ArgumentException("node must implement INodeCrypto");
      }

      tryEnsureLoggedIn();

      // Retrieve download URL
      DownloadUrlRequest downloadRequest = new DownloadUrlRequest(node);
      DownloadUrlResponse downloadResponse = this.Request<DownloadUrlResponse>(downloadRequest);

      Stream dataStream = this.webClient.GetRequestRaw(new URI(downloadResponse.getUrl()));

      Stream resultStream = new MegaAesCtrStreamDecrypter(dataStream, downloadResponse.getSize(),
              nodeCrypto.getKey), nodeCrypto.getIv(), nodeCrypto.getMetaMac());
      if (cancellationToken.HasValue)
      {
        resultStream = new CancellableStream(resultStream, cancellationToken.Value);
      }
      return resultStream;
    }

    /// <summary>
    /// Retrieve a Stream to download and decrypt the specified Uri
    /// </summary>
    /// <param name="uri">Uri to download</param>
    /// <exception cref="NotSupportedException">Not logged in</exception>
    /// <exception cref="ApiException">Mega.co.nz service reports an error</exception>
    /// <exception cref="ArgumentNullException">uri is null</exception>
    /// <exception cref="ArgumentException">Uri is not valid (id and key are required)</exception>
    /// <exception cref="DownloadException">Checksum is invalid. Downloaded data are corrupted</exception>
    public Stream Download(URI uri, CancellationToken cancellationToken) throws ArgumentNullException
    {
      if (uri == null)
      {
        throw new ArgumentNullException("uri");
      }

      tryEnsureLoggedIn();

      String id;
      byte[] iv, metaMac, key;
      this.GetPartsFromUri(uri, out id, out iv, out metaMac, out key);

      // Retrieve download URL
      DownloadUrlRequestFromId downloadRequest = new DownloadUrlRequestFromId(id);
      DownloadUrlResponse downloadResponse = this.Request<DownloadUrlResponse>(downloadRequest);

      Stream dataStream = this.webClient.GetRequestRaw(new Uri(downloadResponse.Url));

      Stream resultStream = new MegaAesCtrStreamDecrypter(dataStream, downloadResponse.Size, key, iv, metaMac);
      if (cancellationToken.HasValue)
      {
        resultStream = new CancellableStream(resultStream, cancellationToken.Value);
      }
      return resultStream;
    }

    /// <summary>
    /// Retrieve public properties of a file from a specified Uri
    /// </summary>
    /// <param name="uri">Uri to retrive properties</param>
    /// <exception cref="NotSupportedException">Not logged in</exception>
    /// <exception cref="ApiException">Mega.co.nz service reports an error</exception>
    /// <exception cref="ArgumentNullException">uri is null</exception>
    /// <exception cref="ArgumentException">Uri is not valid (id and key are required)</exception>
    public INodeInfo GetNodeFromLink(URI uri) throws ArgumentNullException
    {
      if (uri == null)
      {
        throw new ArgumentNullException("uri");
      }

      tryEnsureLoggedIn();

      String id;
      byte[] iv, metaMac, key;
      this.GetPartsFromUri(uri, out id, out iv, out metaMac, out key);

      // Retrieve attributes
      DownloadUrlRequestFromId downloadRequest = new DownloadUrlRequestFromId(id);
      DownloadUrlResponse downloadResponse = this.Request<DownloadUrlResponse>(downloadRequest);

      return new NodeInfo(id, downloadResponse, key);
    }


    /// <summary>
    /// Retrieve list of nodes from a specified Uri
    /// </summary>
    /// <param name="uri">Uri</param>
    /// <exception cref="NotSupportedException">Not logged in</exception>
    /// <exception cref="ApiException">Mega.co.nz service reports an error</exception>
    /// <exception cref="ArgumentNullException">uri is null</exception>
    /// <exception cref="ArgumentException">Uri is not valid (id and key are required)</exception>
    public Iterable<INode> GetNodesFromLink(URI uri) throws ArgumentNullException
    {
      if (uri == null)
      {
        throw new ArgumentNullException("uri");
      }

      tryEnsureLoggedIn();

      String shareId;
      byte[] iv, metaMac, key;
      this.GetPartsFromUri(uri, out shareId, out iv, out metaMac, out key);

      // Retrieve attributes
      GetNodesRequest getNodesRequest = new GetNodesRequest(shareId);
      GetNodesResponse getNodesResponse = this.Request<GetNodesResponse>(getNodesRequest, key);

      return getNodesResponse.Nodes.Select(x => new PublicNode(x, shareId)).OfType<INode>();
    }

    /// <summary>
    /// Upload a file on Mega.co.nz and attach created node to selected parent
    /// </summary>
    /// <param name="filename">File to upload</param>
    /// <param name="parent">Node to attach the uploaded file (all types except <see cref="NodeType.File" /> are supported)</param>
    /// <returns>Created node</returns>
    /// <exception cref="NotSupportedException">Not logged in</exception>
    /// <exception cref="ApiException">Mega.co.nz service reports an error</exception>
    /// <exception cref="ArgumentNullException">filename or parent is null</exception>
    /// <exception cref="FileNotFoundException">filename is not found</exception>
    /// <exception cref="ArgumentException">parent is not valid (all types except <see cref="NodeType.File" /> are supported)</exception>
    public INode UploadFile(string filename, INode parent, CancellationToken? cancellationToken = null) throws ArgumentNullException
    {
      if (String.IsNullOrEmpty(filename))
      {
        throw new ArgumentNullException("filename");
      }

      if (parent == null)
      {
        throw new ArgumentNullException("parent");
      }

      if (!File.Exists(filename))
      {
        throw new FileNotFoundException(filename);
      }

      tryEnsureLoggedIn();

      Date modificationDate = File.GetLastWriteTime(filename);
      using (FileStream fileStream = new FileStream(filename, FileMode.Open, FileAccess.Read))
      {
        return this.Upload(fileStream, Path.GetFileName(filename), parent, modificationDate, cancellationToken);
      }
    }

    /// <summary>
    /// Upload a stream on Mega.co.nz and attach created node to selected parent
    /// </summary>
    /// <param name="stream">Data to upload</param>
    /// <param name="name">Created node name</param>
    /// <param name="parent">Node to attach the uploaded file (all types except <see cref="NodeType.File" /> are supported)</param>
    /// <returns>Created node</returns>
    /// <exception cref="NotSupportedException">Not logged in</exception>
    /// <exception cref="ApiException">Mega.co.nz service reports an error</exception>
    /// <exception cref="ArgumentNullException">stream or name or parent is null</exception>
    /// <exception cref="ArgumentException">parent is not valid (all types except <see cref="NodeType.File" /> are supported)</exception>
    public INode Upload(Stream stream, String name, INode parent, Date modificationDate, CancellationToken cancellationToken) throws ArgumentNullException, UploadException, ApiException
    {
      if (stream == null)
      {
        throw new ArgumentNullException("stream");
      }

      if (String.IsNullOrEmpty(name))
      {
        throw new ArgumentNullException("name");
      }

      if (parent == null)
      {
        throw new ArgumentNullException("parent");
      }

      if (parent.Type == NodeType.File)
      {
        throw new ArgumentException("Invalid parent node");
      }

      tryEnsureLoggedIn();

      if (cancellationToken.HasValue)
      {
        stream = new CancellableStream(stream, cancellationToken.Value);
      }

      String completionHandle = "";
      int requestDelay = this.options.getApiRequestDelay();
      int remainingRetry = this.options.getApiRequestAttempts();
      while (remainingRetry-- > 0)
      {
        // Retrieve upload URL
        UploadUrlRequest uploadRequest = new UploadUrlRequest(stream.length);
        UploadUrlResponse uploadResponse = this.Request<UploadUrlResponse>(uploadRequest);

        ApiResultCode apiResult = ApiResultCode.Ok;
        using (MegaAesCtrStreamCrypter encryptedStream = new MegaAesCtrStreamCrypter(stream))
        {
          var chunkStartPosition = 0;
          var chunksSizesToUpload = this.ComputeChunksSizesToUpload(encryptedStream.ChunksPositions, encryptedStream.Length).ToArray();
          URI uri = null;
          for (int i = 0; i < chunksSizesToUpload.Length; i++)
          {
            completionHandle = "";

            int chunkSize = chunksSizesToUpload[i];
            byte[] chunkBuffer = new byte[chunkSize];
            encryptedStream.Read(chunkBuffer, 0, chunkSize);

            using (MemoryStream chunkStream = new MemoryStream(chunkBuffer))
            {
              uri = new URI(uploadResponse.Url + "/" + chunkStartPosition);
              chunkStartPosition += chunkSize;
              try
              {
                completionHandle = this.webClient.PostRequestRaw(uri, chunkStream);
                if (String.IsNullOrEmpty(completionHandle))
                {
                  apiResult = ApiResultCode.Ok;
                  continue;
                }

                long retCode;
                if (completionHandle.FromBase64().Length != 27 && long.TryParse(completionHandle, out retCode))
                {
                  apiResult = (ApiResultCode)retCode;
                  break;
                }
              }
              catch (Exception ex)
              {
                apiResult = ApiResultCode.RequestFailedRetry;
                this.ApiRequestFailed?.Invoke(this, new ApiRequestFailedEventArgs(uri, remainingRetry, requestDelay, apiResult, ex));

                break;
              }
            }
          }

          if (apiResult != ApiResultCode.Ok)
          {
            this.ApiRequestFailed?.Invoke(this, new ApiRequestFailedEventArgs(uri, remainingRetry, requestDelay, apiResult, completionHandle));

            if (apiResult == ApiResultCode.RequestFailedRetry || apiResult == ApiResultCode.RequestFailedPermanetly || apiResult == ApiResultCode.TooManyRequests)
            {
              // Restart upload from the beginning
              Thread.Sleep(requestDelay = (int)Math.Round(requestDelay * this.options.ApiRequestDelayExponentialFactor));

              // Reset steam position
              stream.Seek(0, SeekOrigin.Begin);

              continue;
            }

            throw new ApiException(apiResult);
          }

          // Encrypt attributes
          byte[] cryptedAttributes = Crypto.EncryptAttributes(new Attributes(name, stream, modificationDate), encryptedStream.FileKey);

          // Compute the file key
          byte[] fileKey = new byte[32];
          for (int i = 0; i < 8; i++)
          {
            fileKey[i] = (byte)(encryptedStream.FileKey[i] ^ encryptedStream.Iv[i]);
            fileKey[i + 16] = encryptedStream.Iv[i];
          }

          for (int i = 8; i < 16; i++)
          {
            fileKey[i] = (byte)(encryptedStream.FileKey[i] ^ encryptedStream.MetaMac[i - 8]);
            fileKey[i + 16] = encryptedStream.MetaMac[i - 8];
          }

          byte[] encryptedKey = Crypto.EncryptKey(fileKey, this.masterKey);

          String cryptedAttributesBase64 = Base64.getEncoder().encodeToString(cryptedAttributes);
          String encryptedKeyBase64 = Base64.getEncoder().encodeToString(encryptedKey);
          
          
          CreateNodeRequest createNodeRequest = CreateNodeRequest.CreateFileNodeRequest(parent, 
                  cryptedAttributesBase64, encryptedKeyBase64, fileKey, completionHandle);
          GetNodesResponse createNodeResponse = this.Request<GetNodesResponse>(createNodeRequest, this.masterKey);
          return createNodeResponse.getNodes()[0];
        }
      }

      throw new UploadException(completionHandle);
    }

    /// <summary>
    /// Change node parent
    /// </summary>
    /// <param name="node">Node to move</param>
    /// <param name="destinationParentNode">New parent</param>
    /// <returns>Moved node</returns>
    /// <exception cref="NotSupportedException">Not logged in</exception>
    /// <exception cref="ApiException">Mega.co.nz service reports an error</exception>
    /// <exception cref="ArgumentNullException">node or destinationParentNode is null</exception>
    /// <exception cref="ArgumentException">node is not valid (only <see cref="NodeType.Directory" /> and <see cref="NodeType.File" /> are supported)</exception>
    /// <exception cref="ArgumentException">parent is not valid (all types except <see cref="NodeType.File" /> are supported)</exception>
    @Override
    public INode move(INode node, INode destinationParentNode) throws ArgumentNullException thorws ArgumentException
    {
      if (node == null)
      {
        throw new ArgumentNullException("node");
      }

      if (destinationParentNode == null)
      {
        throw new ArgumentNullException("destinationParentNode");
      }

      NodeType nodesType = node.getType();
      
      if (nodesType != NodeType.Directory && nodesType != NodeType.File)
      {
        throw new ArgumentException("Invalid node type");
      }

      if (destinationParentNode.getType() == NodeType.File)
      {
        throw new ArgumentException("Invalid destination parent node");
      }

      tryEnsureLoggedIn();

      this.Request(new MoveRequest(node, destinationParentNode));
      return this.GetNodes().First(n => n.Equals(node));
    }

    public INode Rename(INode node, String newName) throws ArgumentException
    {
      if (node == null)
      {
        throw new ArgumentNullException("node");
      }
      
      NodeType nodesType = node.getType();

      if (nodesType != NodeType.Directory && nodesType != NodeType.File)
      {
        throw new ArgumentException("Invalid node type");
      }

      if (String.IsNullOrEmpty(newName))
      {
        throw new ArgumentNullException("newName");
      }

      INodeCrypto nodeCrypto = (INodeCrypto)node;
      if (nodeCrypto == null)
      {
        throw new ArgumentException("node must implement INodeCrypto");
      }

      tryEnsureLoggedIn();

      byte[] encryptedAttributes = Crypto.EncryptAttributes(new Attributes(newName, ((Node)node).Attributes), nodeCrypto.getKey());
      
      String encryptedBase64 = Base64.getEncoder().encodeToString(encryptedAttributes);
      
      this.Request(new RenameRequest(node, encryptedBase64));
      return this.GetNodes().First(n => n.Equals(node));
    }


    private static String generateHash(String email, byte[] passwordAesKey)
    {
      byte[] emailBytes = email.getBytes(Charset.forName("UTF-8"));
      byte[] hash = new byte[16];

      // Compute email in 16 bytes array
      for (int i = 0; i < emailBytes.length; i++)
      {
        hash[i % 16] ^= emailBytes[i];
      }

      // Encrypt hash using password key
      for (int it = 0; it < 16384; it++)
      {
        hash = Crypto.EncryptAes(hash, passwordAesKey);
      }

      // Retrieve bytes 0-4 and 8-12 from the hash
      byte[] result = new byte[8];
      Array.Copy(hash, 0, result, 0, 4);
      Array.Copy(hash, 8, result, 4, 4);

      return Base64.getEncoder().encodeToString(result);
    }

    private static byte[] PrepareKey(byte[] data)
    {
      byte[] pkey = new byte[] { 0x93, 0xC4, 0x67, 0xE3, 0x7D, 0xB0, 0xC7, 0xA4, 0xD1, 0xBE, 0x3F, 0x81, 0x01, 0x52, 0xCB, 0x56 };

      for (int it = 0; it < 65536; it++)
      {
        for (int idx = 0; idx < data.length; idx += 16)
        {
          // Pad the data to 16 bytes blocks
          byte[] key = data.CopySubArray(16, idx);

          pkey = Crypto.EncryptAes(pkey, key);
        }
      }

      return pkey;
    }


    private String Request(RequestBase request)
    {
      return this.Request<String>(request);
    }

    private TResponse Request<TResponse>(RequestBase request, object context = null)
            where TResponse : class
    {
      if (this.options.SynchronizeApiRequests)
      {
        lock (this.apiRequestLocker)
        {
          return this.RequestCore<TResponse>(request, context);
        }
      }
      else
      {
        return this.RequestCore<TResponse>(request, context);
      }
    }

    private TResponse RequestCore<TResponse>(RequestBase request, object context = null)
        where TResponse : class
    {
      string dataRequest = JsonConvert.SerializeObject(new object[] { request });
      Uri uri = this.GenerateUrl(request.QueryArguments);
      object jsonData = null;
      int requestDelay = this.options.ApiRequestDelay;
      int remainingRetry = this.options.ApiRequestAttempts;
      while (remainingRetry-- > 0)
      {
        string dataResult = this.webClient.PostRequestJson(uri, dataRequest);

        if (string.IsNullOrEmpty(dataResult) 
          || (jsonData = JsonConvert.DeserializeObject(dataResult)) == null
          || jsonData is long
          || (jsonData is JArray && ((JArray)jsonData)[0].Type == JTokenType.Integer))
        {
          ApiResultCode apiCode = jsonData == null
            ? ApiResultCode.RequestFailedRetry
            : jsonData is long
              ?(ApiResultCode)Enum.ToObject(typeof(ApiResultCode), jsonData)
              : (ApiResultCode)((JArray)jsonData)[0].Value<int>();

          if (apiCode != ApiResultCode.Ok)
          {
            this.ApiRequestFailed?.Invoke(this, new ApiRequestFailedEventArgs(uri, this.options.ApiRequestAttempts - remainingRetry, requestDelay, apiCode, dataResult));
          }

          if (apiCode == ApiResultCode.RequestFailedRetry)
          {
            Thread.Sleep(requestDelay = (int)Math.Round(requestDelay * this.options.ApiRequestDelayExponentialFactor));
            continue;
          }

          if (apiCode != ApiResultCode.Ok)
          {
            throw new ApiException(apiCode);
          }
        }

        break;
      }

      JsonSerializerSettings settings = new JsonSerializerSettings();
      settings.Context = new StreamingContext(StreamingContextStates.All, context);

      string data = ((JArray)jsonData)[0].ToString();
      return (typeof(TResponse) == typeof(string)) ? data as TResponse : JsonConvert.DeserializeObject<TResponse>(data, settings);
    }

    private URI GenerateUrl(NameValueCollection queryArguments)
    {
      UriBuilder builder = new UriBuilder(BaseApiUri);
      NameValueCollection query = HttpUtility.ParseQueryString(builder.Query);
      query["id"] = (this.sequenceIndex++ % uint.MaxValue).ToString(CultureInfo.InvariantCulture);
      query["ak"] = this.options.ApplicationKey;

      if (!String.IsNullOrEmpty(this.sessionId))
      {
        query["sid"] = this.sessionId;
      }

      query.Add(queryArguments);

      builder.Query = query.ToString();
      return builder.Uri;
    }

    private void SaveStream(Stream stream, String outputFile)
    {
      using (FileStream fs = new FileStream(outputFile, FileMode.CreateNew, FileAccess.Write))
      {
        stream.CopyTo(fs, this.options.getBufferSize());
      }
    }


    private void ensureLoggedIn() throws NotSupportedException
    {
      if (this.sessionId == null)
      {
        throw new NotSupportedException("Not logged in");
      }
    }
    
    private void tryEnsureLoggedIn(){
        try {
            this.ensureLoggedIn();
        } catch (NotSupportedException ex) {
            Logger.getLogger(MegaApiClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void tryEnsureLoggedOut(){
        try {
            this.ensureLoggedOut();
        } catch (NotSupportedException ex) {
            Logger.getLogger(MegaApiClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void ensureLoggedOut() throws NotSupportedException
    {
      if (this.sessionId != null)
      {
        throw new NotSupportedException("Already logged in");
      }
    }

    private void GetPartsFromUri(URI uri, out String id, out byte[] iv, out byte[] metaMac, out byte[] key)
    {
      Regex uriRegex = new Regex("#(?<type>F?)!(?<id>.+)!(?<key>.+)");
      Match match = uriRegex.Match(uri.Fragment);
      if (match.Success == false)
      {
        throw new ArgumentException(string.Format("Invalid uri. Unable to extract Id and Key from the uri {0}", uri));
      }

      id = match.Groups["id"].Value;
      byte[] decryptedKey = match.Groups["key"].Value.FromBase64();
      var isFolder = match.Groups["type"].Value == "F";
      
      if (isFolder)
      {
        iv = null;
        metaMac = null;
        key = decryptedKey;
      }
      else
      {
        Crypto.getPartsFromDecryptedKey(decryptedKey, out iv, out metaMac, out key);
      }
    }

    private Iterable<int> ComputeChunksSizesToUpload(long[] chunksPositions, long streamLength)
    {
      for (int i = 0; i < chunksPositions.length; i++)
      {
        long currentChunkPosition = chunksPositions[i];
        long nextChunkPosition = i == chunksPositions.length - 1
          ? streamLength
          : chunksPositions[i + 1];

        int optionsChuncksPackSize = options.getChunksPackSize();
        
        // Pack multiple chunks in a single upload
        while (((int)(nextChunkPosition - currentChunkPosition) < optionsChuncksPackSize ||
                optionsChuncksPackSize == -1) && i < chunksPositions.length - 1)
        {
          i++;
          nextChunkPosition = i == chunksPositions.length - 1
            ? streamLength
            : chunksPositions[i + 1];
        }

        yield return (int)(nextChunkPosition - currentChunkPosition);
      }
    }


}
