package atmbranchfinderspring.resourceserver.validation.accesstokens;

import atmbranchfinderspring.resourceserver.models.AccessToken;

public interface TokenValidator {
	boolean validate(AccessToken token);
	String errorMessage();
}
