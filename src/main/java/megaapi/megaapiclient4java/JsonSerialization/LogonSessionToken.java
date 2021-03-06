package megaapi.megaapiclient4java.JsonSerialization;

import java.util.Objects;

public class LogonSessionToken implements Iterable<LogonSessionToken> {

    @JsonProperty
    private String sessionId;

    public String getSessionId() {
        return sessionId;
    }

    @JsonProperty
    public byte[] masterKey;

    public byte[] getMasterKey() {
        return masterKey;
    }

    private LogonSessionToken() {
    }

    public LogonSessionToken(String sessionId, byte[] masterKey) {
        this.sessionId = sessionId;
        this.masterKey = masterKey;
    }

    public boolean equals(LogonSessionToken other) {
        if (other == null) {
            return false;
        }
        if (this.sessionId == null || other.sessionId == null || Objects.equals(this.sessionId, other.sessionId) != 0) {
            return false;
        }
        if (this.masterKey == null || other.masterKey == null || !Enumerable.SequenceEqual(masterKey, other.masterKey)) {
            return false;
        }
        return true;
    }
}
