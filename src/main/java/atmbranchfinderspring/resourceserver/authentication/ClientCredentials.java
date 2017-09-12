package atmbranchfinderspring.resourceserver.authentication;

import atmbranchfinderspring.resourceserver.models.ResponseObject;

import java.time.LocalDateTime;

/**
 * @author Ricardo Schuller
 * @version 0.1.0
 * @since 0.1.0
 */
public class ClientCredentials implements ResponseObject {

    private String clientId;
    private String clientSecret;
    private LocalDateTime creationDate;
    private LocalDateTime expirationDate;

    public ClientCredentials(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }
}
