package atmbranchfinderspring.resourceserver.validation.accesstokens;

import atmbranchfinderspring.resourceserver.models.ResponseObject;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class AccessToken implements ResponseObject {

	/**
	 * Access token is used to authenticate software clients.
	 * Highly advisable that these are immutable.
	 */

	public enum Grant {
		CLIENT_CREDENTIALS, AUTHORIZATION_CODE
	}

	public enum Scope {
		ACCOUNTS
	}

	private final String accessToken;
	private final String tokenType;
	private final LocalDateTime issueDate;
	private final LocalDateTime expirationDate;
	private final String clientId;
	@JsonIgnore
	private Grant grant;
	private List<Scope> scopes;



	public AccessToken(String clientId, String tokenType, String customToken, Long expirationTime) {
		this.tokenType = tokenType;
		this.accessToken = customToken;
		this.clientId = clientId;
		this.issueDate = LocalDateTime.now();
		this.expirationDate = LocalDateTime.now().plusSeconds(expirationTime);
	}

	public AccessToken(String clientId, String tokenType, Long expirationTime, Grant grant, List<Scope> scopes) {
		this.clientId = clientId;
		this.tokenType = tokenType;
		this.accessToken = UUID.randomUUID().toString();
		this.issueDate = LocalDateTime.now();
		this.expirationDate = issueDate.plusSeconds(expirationTime);
		this.grant = grant;
		this.scopes = scopes;
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

	public Grant getGrant() {
		return grant;
	}

	public List<Scope> getScopes() {
		return scopes;
	}
}
