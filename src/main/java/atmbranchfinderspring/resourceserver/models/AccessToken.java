package atmbranchfinderspring.resourceserver.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class AccessToken implements ResponseObject {

    private String accessToken;
    private String tokenType;
    private LocalDateTime issueDate;
    private LocalDateTime expirationDate;

    public AccessToken(String tokenType, String customToken) {
        this.tokenType = tokenType;
        this.accessToken = customToken;
    }

    public AccessToken(String tokenType, Long expirationTime) {
        this.tokenType = tokenType;
        this.accessToken = UUID.randomUUID().toString();
        this.issueDate = LocalDateTime.now();
        this.expirationDate = issueDate.plusSeconds(expirationTime);
    }

    public String getAccessToken() {
        return accessToken;
    }
    public String getTokenType() { return tokenType; }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }
}
