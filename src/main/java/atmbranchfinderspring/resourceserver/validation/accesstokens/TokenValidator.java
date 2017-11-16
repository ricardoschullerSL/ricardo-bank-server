package atmbranchfinderspring.resourceserver.validation.accesstokens;

/**
 * TokenValidator interface follows the Command Pattern. Whenever an access token is checked, a list of these validators
 * is created to check if every requirement is fulfilled.
 */
public interface TokenValidator {

	default boolean validate(AccessToken token) {
		return false;
	}

	default String errorMessage() {
		return "Invalid token.";
	}
}
