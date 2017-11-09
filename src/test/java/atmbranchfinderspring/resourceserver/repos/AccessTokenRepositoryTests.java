package atmbranchfinderspring.resourceserver.repos;


import atmbranchfinderspring.resourceserver.validation.accesstokens.AccessToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AccessTokenRepositoryTests {

	private AccessTokenRepository accessTokenRepository;

	@BeforeEach
	void setup() {
		accessTokenRepository = new AccessTokenRepository();
		accessTokenRepository.add(new AccessToken("testClient", AccessToken.TokenType.TEST, "abc", 10L, "testId"));
		accessTokenRepository.add(new AccessToken("testClient", AccessToken.TokenType.TEST, "def", 10L, "testId"));
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
		AccessToken token = new AccessToken("testClient2", AccessToken.TokenType.TEST,"token", 10L, "testId");
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
