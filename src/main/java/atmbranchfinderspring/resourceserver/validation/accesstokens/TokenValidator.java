package atmbranchfinderspring.resourceserver.validation.accesstokens;

public interface TokenValidator {
	boolean validate(AccessToken token);
	String errorMessage();
}
