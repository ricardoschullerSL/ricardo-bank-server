package atmbranchfinderspring.resourceserver.repos;


import atmbranchfinderspring.resourceserver.models.AccessToken;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccessTokenRepositoryTests {

	private AccessTokenRepository accessTokenRepository;

	@BeforeEach
	void setup() {
		accessTokenRepository = new AccessTokenRepository();
		accessTokenRepository.add(new AccessToken("testClient","testToken", "abc", 10L));
		accessTokenRepository.add(new AccessToken("testClient","testToken", "def", 10L));
	}

	@AfterEach
	void cleanup() {
		accessTokenRepository = null;
	}

	@Test
	void getGetsToken() {
		AccessToken token = accessTokenRepository.get("abc");
		assertThat(token.getClientId()).isEqualTo("testClient");
	}

	@Test
	void getsReturnNullIfTokenDoesNotExist() {
		AccessToken token = accessTokenRepository.get("aaa");
		assertThat(token).isEqualTo(null);
	}

	@Test
	void containsReturnsBooleanTrue() {
		assertThat(accessTokenRepository.contains("abc")).isEqualTo(true);
	}

	@Test
	void containsReturnsBooleanFalse() {
		assertThat(accessTokenRepository.contains("aaa")).isEqualTo(false);
	}

	@Test
	void addAddsToken() {
		int size = accessTokenRepository.getAllIds().size();
		AccessToken token = new AccessToken("testClient2","testToken","token", 10L);
		accessTokenRepository.add(token);
		assertThat(accessTokenRepository.getAllIds().size()).isEqualTo(size + 1);
	}

	@Test
	void deleteDeletesToken() {
		int size = accessTokenRepository.getAllIds().size();
		accessTokenRepository.delete("abc");
		assertThat(accessTokenRepository.getAllIds().size()).isEqualTo(size - 1);
	}

	@Test
	void deleteDeletesTokenWithTokenParameter() {
		int size = accessTokenRepository.getAllIds().size();
		AccessToken token = accessTokenRepository.get("abc");
		accessTokenRepository.delete(token);
		assertThat(accessTokenRepository.getAllIds().size()).isEqualTo(size - 1);
	}
}
