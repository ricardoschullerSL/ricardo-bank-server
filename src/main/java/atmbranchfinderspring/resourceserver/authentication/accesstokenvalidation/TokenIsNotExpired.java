package atmbranchfinderspring.resourceserver.authentication.accesstokenvalidation;

import atmbranchfinderspring.resourceserver.models.AccessToken;

import java.time.LocalDateTime;

public class TokenIsNotExpired implements TokenValidator {

	public TokenIsNotExpired() {}

	public boolean validate(AccessToken token) {
		return LocalDateTime.now().isBefore(token.getExpirationDate());
	}

	public String errorMessage() {
		return "Token is expired";
	}
}
