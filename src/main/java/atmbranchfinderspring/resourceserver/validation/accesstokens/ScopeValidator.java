package atmbranchfinderspring.resourceserver.validation.accesstokens;

public class ScopeValidator implements TokenValidator {

	private AccessToken.Scope requiredScope;

	public ScopeValidator(AccessToken.Scope requiredScope) {
		this.requiredScope = requiredScope;
	}

	@Override
	public boolean validate(AccessToken token) {
		return token.getScopes().contains(requiredScope);
	}

	public String errorMessage() {
		return "Incorrect token scope.";
	}
}
