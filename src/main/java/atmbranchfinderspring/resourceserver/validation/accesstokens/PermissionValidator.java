package atmbranchfinderspring.resourceserver.validation.accesstokens;

import atmbranchfinderspring.resourceserver.validation.accountrequests.Permission;

import java.util.Set;

public class PermissionValidator implements TokenValidator {

	private Set<Permission> requiredPermission;

	public PermissionValidator(Set<Permission> permissions) {
		this.requiredPermission =  permissions;
	}

	@Override
	public boolean validate(AccessToken token) {
		Set<Permission> tokenPermissions = token.getPermissions();
		tokenPermissions.retainAll(requiredPermission);
		return tokenPermissions.size() > 0;
	}

	public String errorMessage() {
		return "Incorrect token permission.";
	}
}
