package atmbranchfinderspring.resourceserver.validation.accesstokens;

import atmbranchfinderspring.resourceserver.models.ResponseObject;
import atmbranchfinderspring.resourceserver.validation.accountrequests.Permission;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public class AccessToken implements ResponseObject {

	/**
	 * Access token is used to authenticate software clients.
	 * Highly advisable that these are immutable.
	 */

	public enum Grant {
		CLIENT_CREDENTIALS, AUTHORIZATION_CODE
	}

	private final String accessToken;
	private final String tokenType;
	private final LocalDateTime issueDate;
	private final LocalDateTime expirationDate;
	private final String clientId;
	private final String accountRequestId;
	@JsonIgnore
	private Grant grant;




	public AccessToken(String clientId, String tokenType, String customToken, Long expirationTime, String accountRequestId) {
		this.tokenType = tokenType;
		this.accessToken = customToken;
		this.clientId = clientId;
		this.issueDate = LocalDateTime.now();
		this.expirationDate = LocalDateTime.now().plusSeconds(expirationTime);
		this.accountRequestId = accountRequestId;
	}

	public AccessToken(String clientId, String tokenType, Long expirationTime, Grant grant, String accountRequestId) {
		this.clientId = clientId;
		this.tokenType = tokenType;
		this.accessToken = UUID.randomUUID().toString();
		this.issueDate = LocalDateTime.now();
		this.expirationDate = issueDate.plusSeconds(expirationTime);
		this.grant = grant;
		this.accountRequestId = accountRequestId;
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

	public String getAccountRequestId() {return accountRequestId;}
}
