package atmbranchfinderspring.resourceserver.authentication.accesstokenvalidation;

import atmbranchfinderspring.resourceserver.models.AccessToken;

public class GrantValidator implements TokenValidator {

	private AccessToken.Grant requiredGrant;

	public GrantValidator(AccessToken.Grant requiredGrant) {
		this.requiredGrant = requiredGrant;
	}

	public boolean validate(AccessToken token) {
		return token.getGrant() == requiredGrant;
	}

	public String errorMessage() {
		return "Incorrect token grant";
	}
}
