package atmbranchfinderspring.resourceserver.authentication;


import atmbranchfinderspring.resourceserver.models.Permission;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class IncomingAccountRequestValidatorTests {

	private AccountRequestValidator accountRequestValidator;

	@BeforeEach
	void setup() {
		this.accountRequestValidator = new AccountRequestValidator();
	}

	@AfterEach
	void cleanup() {
		this.accountRequestValidator = null;
	}

	@Test
	@DisplayName("checkPermission checks if String permission is a valid Permission")
	void checkPermissionTest() {
		assertThat(accountRequestValidator.checkPermission("ReadAccountsBasic")).isEqualTo(true);
		assertThat(accountRequestValidator.checkPermission("WrongPermission")).isEqualTo(false);
	}

	@Test
	@DisplayName("checkPermissions checks if all values in String list are valid Permissions")
	void checkPermissionArrayTest() {
		List<String> permissionArray = new ArrayList<String>(Arrays.asList("ReadAccountsBasic", "ReadAccountsDetail"));
		assertThat(accountRequestValidator.checkPermissionArray(permissionArray)).isEqualTo(true);
		List<String> wrongPermissionArray = new ArrayList<String>(Arrays.asList("ReadAccountsBasic", "WrongPermission"));
		assertThat(accountRequestValidator.checkPermissionArray(wrongPermissionArray)).isEqualTo(false);
	}

	@Test
	@DisplayName("convertPermissions converts String list to Permission list")
	void convertPermissionsTest() {
		List<String> stringArray = new ArrayList<String>(Arrays.asList("ReadAccountsBasic", "ReadAccountsDetail"));
		List<Permission> permissionArray = accountRequestValidator.convertPermissions(stringArray);
		assertThat(permissionArray.size()).isEqualTo(2);
	}
}
