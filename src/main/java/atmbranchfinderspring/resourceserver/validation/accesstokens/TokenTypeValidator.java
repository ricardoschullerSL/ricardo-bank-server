package atmbranchfinderspring.resourceserver.validation.accesstokens;

public class TokenTypeValidator implements TokenValidator {

	private AccessToken.TokenType requiredTokenType;

	public TokenTypeValidator(AccessToken.TokenType requiredTokenType) {
		this.requiredTokenType = requiredTokenType;
	}

	@Override
	public boolean validate(AccessToken token) {
		return requiredTokenType.equals(token.getTokenType());
	}

	@Override
	public String errorMessage() {
		return "Incorrect token type.";
	}
}
