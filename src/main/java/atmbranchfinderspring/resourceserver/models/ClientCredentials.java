package atmbranchfinderspring.resourceserver.models;

import atmbranchfinderspring.resourceserver.models.ResponseObject;

import java.time.LocalDateTime;


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
