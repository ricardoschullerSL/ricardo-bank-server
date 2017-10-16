package atmbranchfinderspring.resourceserver.authentication.accesstokenvalidation;

import atmbranchfinderspring.resourceserver.models.AccessToken;

public interface TokenValidator {
	boolean validate(AccessToken token);
	String errorMessage();
}
