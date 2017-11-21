package atmbranchfinderspring.resourceserver.validation.accesstokens;

import atmbranchfinderspring.resourceserver.models.ResponseObject;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Access token is used to authenticate software clients.
 * Highly advisable that these are immutable.
 */
public class AccessToken implements ResponseObject {

	public enum Grant {
		CLIENT_CREDENTIALS, AUTHORIZATION_CODE
	}

	public enum TokenType {
		ACCESS, REFRESH, BEARER, REQUEST, TEST
	}

	private final String tokenString;
	private final TokenType tokenType;
	private final LocalDateTime issueDate;
	private final LocalDateTime expirationDate;
	private final String clientId;
	private final String accountRequestId;
	@JsonIgnore
	private Grant grant;


	public AccessToken(String clientId, TokenType tokenType, String customToken, Long expirationTime, String accountRequestId) {
		this.tokenType = tokenType;
		this.tokenString = customToken;
		this.clientId = clientId;
		this.issueDate = LocalDateTime.now();
		this.expirationDate = LocalDateTime.now().plusSeconds(expirationTime);
		this.accountRequestId = accountRequestId;
	}

	public AccessToken(String clientId, TokenType tokenType, Long expirationTime, Grant grant, String accountRequestId) {
		this.clientId = clientId;
		this.tokenType = tokenType;
		this.tokenString = UUID.randomUUID().toString();
		this.issueDate = LocalDateTime.now();
		this.expirationDate = issueDate.plusSeconds(expirationTime);
		this.grant = grant;
		this.accountRequestId = accountRequestId;
	}

	public AccessToken(String tokenString, String clientId, TokenType tokenType, LocalDateTime issueDate,
	                   LocalDateTime expirationDate, Grant grant, String accountRequestId) {
		this.tokenString = tokenString;
		this.clientId = clientId;
		this.tokenType = tokenType;
		this.issueDate = issueDate;
		this.expirationDate = expirationDate;
		this.grant = grant;
		this.accountRequestId = accountRequestId;
	}

	public String getClientId() {return clientId;}
	public String getTokenString() {
		return tokenString;
	}
	public TokenType getTokenType() { return tokenType; }
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
