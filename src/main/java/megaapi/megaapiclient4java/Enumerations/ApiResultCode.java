package megaapi.megaapiclient4java.Enumerations;

public enum ApiResultCode {
    //API_OK (0): Success
    Ok(0),
    // API_EINTERNAL (-1): An internal error has occurred. Please submit a bug report,
    // detailing the exact circumstances in which this error occurred.
    InternalError(-1),
    // API_EARGS (-2): You have passed invalid arguments to this command.
    BadArguments(-2),
    // API_EAGAIN (-3) (always at the request level): A temporary congestion or server 
    // malfunction prevented your request from being processed. No data was altered.
    // Retry. Retries must be spaced with exponential backoff.
    RequestFailedRetry(-3),
    // API_ERATELIMIT (-4): You have exceeded your command weight per time quota.
    // Please wait a few seconds, then try again (this should never happen in sane
    // real-life applications).
    TooManyRequests(-4),
    // API_EFAILED (-5): The upload failed. Please restart it from scratch.
    RequestFailedPermanetly(-5),
    // API_ETOOMANY (-6): Too many concurrent IP addresses are accessing this upload target URL.
    TooManyRequestsForThisResource(-6),
    // API_ERANGE (-7): The upload file packet is out of range or not starting and ending on a chunk boundary.
    ResourceAccessOutOfRange(-7),
    // API_EEXPIRED (-8): The upload target URL you are trying to access has expired.
    // Please request a fresh one.
    ResourceExpired(-8),
    // API_EOENT (-9): Object (typically, node or user) not found
    ResourceNotExists(-9),
    // API_ECIRCULAR (-10): Circular linkage attempted
    CircularLinkage(-10),
    // API_EACCESS (-11): Access violation (e.g., trying to write to a read-only share)
    AccessDenied(-11),
    // API_EEXIST (-12): Trying to create an object that already exists
    ResourceAlreadyExists(-12),
    // API_EINCOMPLETE (-13): Trying to access an incomplete resource
    RequestIncomplete(-13),
    // API_EKEY (-14): A decryption operation failed (never returned by the API)
    CryptographicError(-14),
    // API_ESID (-15): Invalid or expired user session, please relogin
    BadSessionId(-15),
    // API_EBLOCKED (-16): User blocked
    ResourceAdministrativelyBlocked(-16),
    // API_EOVERQUOTA (-17): Request over quota
    QuotaExceeded(-17),
    // API_ETEMPUNAVAIL (-18): Resource temporarily not available, please try again later
    ResourceTemporarilyNotAvailable(-18),
    // API_ETOOMANYCONNECTIONS (-19): Too many connections on this resource
    TooManyConnectionsOnThisResource(-19),
    // API_EWRITE (-20): Write failed
    FileCouldNotBeWrittenTo(-20),
    // API_EREAD (-21): Read failed
    FileCouldNotBeReadFrom(-21),
    // API_EAPPKEY (-22): Invalid application key; request not processed
    InvalidOrMissingApplicationKey(-22);

    private final int value;

    ApiResultCode(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }
}
