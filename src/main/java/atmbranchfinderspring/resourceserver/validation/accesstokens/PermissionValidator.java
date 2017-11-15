package atmbranchfinderspring.resourceserver.validation.accesstokens;

import atmbranchfinderspring.resourceserver.validation.accountrequests.Permission;

import java.util.Set;

public class PermissionValidator implements TokenValidator {

	private Set<Permission> requiredPermissions;
	private Set<Permission> setPermissions;

	public PermissionValidator(Set<Permission> requiredPermissions, Set<Permission> setPermissions) {
		this.requiredPermissions = requiredPermissions;
		this.setPermissions = setPermissions;
	}

	@Override
	public boolean validate(AccessToken token) {
		setPermissions.retainAll(requiredPermissions);
		return setPermissions.size() > 0;
	}

	@Override
	public String errorMessage() {
		return "Insufficient permissions.";
	}
}
