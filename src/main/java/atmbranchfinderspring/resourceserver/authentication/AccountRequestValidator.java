package atmbranchfinderspring.resourceserver.authentication;

import atmbranchfinderspring.resourceserver.models.Permission;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AccountRequestValidator {

	private static final Permission[] allPermissions = Permission.values();

	public Boolean checkPermissionArray(List<String> permissionArray) {
		for(String permission: permissionArray) {
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

	public List<Permission> convertPermissions(List<String> permissionsList) {
		List<Permission> permissions = new ArrayList<>();
		for (String permission: permissionsList) {
			permissions.add(Permission.valueOf(permission));
		}

		return permissions;
	}

}
