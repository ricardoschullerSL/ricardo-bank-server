package atmbranchfinderspring.resourceserver.repos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;


public class AuthorizationCodeRepositoryTests {

	private AuthorizationCodeRepository authorizationCodeRepository;

	@BeforeEach
	void setup() {
		authorizationCodeRepository = new AuthorizationCodeRepository();
		authorizationCodeRepository.add("testCode", "testRequestId");
	}

	@Test
	void addAdds() {
		authorizationCodeRepository.add("testCode2","testRequestId2");
		assertThat(authorizationCodeRepository.getAllIds().size()).isEqualTo(2);
	}

	@Test
	void getGets() {
		assertThat(authorizationCodeRepository.get("testCode")).isEqualTo("testRequestId");
	}

	@Test
	void getReturnsNullIfAuthorizationCodeDoesntExist() {
		assertThat(authorizationCodeRepository.get("wrongCode")).isEqualTo(null);
	}

	@Test
	void containsReturnsBooleanTrue() {
		assertThat(authorizationCodeRepository.contains("testCode")).isEqualTo(true);
	}

	@Test
	void containsReturnsBooleanFalse() {
		assertThat(authorizationCodeRepository.contains("wrongCode")).isEqualTo(false);
	}

	@Test
	void deleteDeletes() {
		authorizationCodeRepository.delete("testCode");
		assertThat(authorizationCodeRepository.getAllIds().size()).isEqualTo(0);
	}

	@Test
	void getAllIdsGetsAllIds() {
		Collection<String> allIds = authorizationCodeRepository.getAllIds();
		assertThat(allIds).isEqualTo(new HashSet<String>(){{add("testCode");}});
	}
}
