package atmbranchfinderspring.resourceserver.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class AccessToken implements ResponseObject {

	/**
	 * Access token is used to authenticate software clients.
	 * Highly advisable that these are immutable.
	 */

	private final String accessToken;
	private final String tokenType;
	private final LocalDateTime issueDate;
	private final LocalDateTime expirationDate;
	private final String clientId;

	public AccessToken(String clientId, String tokenType, String customToken, Long expirationTime) {
		this.tokenType = tokenType;
		this.accessToken = customToken;
		this.clientId = clientId;
		this.issueDate = LocalDateTime.now();
		this.expirationDate = LocalDateTime.now().plusSeconds(expirationTime);
	}

	public AccessToken(String clientId, String tokenType, Long expirationTime) {
		this.clientId = clientId;
		this.tokenType = tokenType;
		this.accessToken = UUID.randomUUID().toString();
		this.issueDate = LocalDateTime.now();
		this.expirationDate = issueDate.plusSeconds(expirationTime);
	}

	public String getClientId() {return clientId;}
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
