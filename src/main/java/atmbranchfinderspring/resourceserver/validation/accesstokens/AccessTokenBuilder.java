package atmbranchfinderspring.resourceserver.validation.accesstokens;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Helper class for AccessToken creation. Should only be used internally as it doesn't check for any correctness.
 *
 */
public class AccessTokenBuilder {

	private String accessToken;
	private AccessToken.TokenType tokenType;
	private LocalDateTime issueDate;
	private LocalDateTime expirationDate;
	private String clientId;
	private String accountRequestId;
	private AccessToken.Grant grant;

	public AccessTokenBuilder () {
		this.accessToken = UUID.randomUUID().toString();
	}

	public AccessTokenBuilder setClientId(String clientId) {
		this.clientId = clientId;
		return this;
	}

	public AccessTokenBuilder setIssueDate(LocalDateTime issueDate) {
		this.issueDate = issueDate;
		return this;
	}

	public AccessTokenBuilder setExpirationDate(LocalDateTime expirationDate) {
		this.expirationDate = expirationDate;
		return this;
	}

	public AccessTokenBuilder setAccountRequestId(String accountRequestId) {
		this.accountRequestId = accountRequestId;
		return this;
	}

	public AccessTokenBuilder setGrant(AccessToken.Grant grant) {
		this.grant = grant;
		return this;
	}

	public AccessTokenBuilder setTokenType(AccessToken.TokenType tokenType) {
		this.tokenType = tokenType;
		return this;
	}

	public AccessToken build() {
		AccessToken token = new AccessToken(this.accessToken, this.clientId, this.tokenType, this.issueDate, this.expirationDate, this.grant, this.accountRequestId);
		return token;
	}
}

