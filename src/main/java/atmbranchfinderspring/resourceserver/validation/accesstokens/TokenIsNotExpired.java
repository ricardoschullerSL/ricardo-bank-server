package atmbranchfinderspring.resourceserver.validation.accesstokens;

import java.time.LocalDateTime;

public class TokenIsNotExpired implements TokenValidator {

	public TokenIsNotExpired() {}

	@Override
	public boolean validate(AccessToken token) {
		return LocalDateTime.now().isBefore(token.getExpirationDate());
	}

	@Override
	public String errorMessage() {
		return "Token is expired";
	}
}
