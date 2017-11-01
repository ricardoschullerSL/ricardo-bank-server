package atmbranchfinderspring.resourceserver.validation.accountrequests;

import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * AccountRequestValidator is a helper object used to check if the permission scopes requested by the incoming account-request
 * are valid.
 */

@Component
public class AccountRequestValidator {
	private static final Permission[] allPermissions = Permission.values();
	private static final EnumSet<Permission> PERMISSIONS = EnumSet.allOf(Permission.class);

	public Boolean checkPermissionList(List<String> permissionArray) {
		for (String permission: permissionArray) {
			if (checkPermission(permission) == false) {
				return false;
			}
		}
		return true;
	}

	public Boolean checkPermission(String permissionToBeChecked) {
		for(Permission permission: allPermissions) {
			if(permission.name().equals(permissionToBeChecked)) {
				return true;
			}
		}
		return false;
	}

	public Set<Permission> convertPermissions(List<String> permissionsList) {
		Set<Permission> permissions = new HashSet<>();
		for (String permission: permissionsList) {
			permissions.add(Permission.valueOf(permission));
		}

		return permissions;
	}

}
