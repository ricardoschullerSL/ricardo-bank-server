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

	/**
	 * PermissionValidator checks for the union of the set of required permissions, and the permissions given by the
	 * {@link atmbranchfinderspring.resourceserver.validation.accountrequests.AccountRequest}. If the required set size
	 * is equal to 0, no permissions are required so return true, else check if the union set size is greater than 0.
	 *
	 * @return boolean
	 */
	@Override
	public boolean validate(AccessToken token) {
		setPermissions.retainAll(requiredPermissions);
		return requiredPermissions.size() == 0 ? true : setPermissions.size() > 0;
	}

	@Override
	public String errorMessage() {
		return "Insufficient permissions.";
	}
}
