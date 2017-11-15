package atmbranchfinderspring.resourceserver.validation.accesstokens;

public interface TokenValidator {

	default boolean validate(AccessToken token) {
		return false;
	}

	default String errorMessage() {
		return "Invalid token.";
	}
}
