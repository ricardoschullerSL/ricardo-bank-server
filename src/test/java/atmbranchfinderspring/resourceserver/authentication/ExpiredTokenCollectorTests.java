package atmbranchfinderspring.resourceserver.authentication;


import atmbranchfinderspring.resourceserver.models.AccessToken;
import atmbranchfinderspring.resourceserver.repos.AccessTokenRepository;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

public class ExpiredTokenCollectorTests {

	private AccessTokenRepository accessTokenRepository;
	private ExpiredTokenCollector expiredTokenCollector;

	private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

	@Before
	void setUpStream() {
		System.setOut(new PrintStream(outputStream));
	}

	@BeforeEach
	void setup() {
		accessTokenRepository = new AccessTokenRepository();

	}

	@AfterEach
	void cleanup() {
		if (expiredTokenCollector != null) {
			expiredTokenCollector.stopThread();
		}
		accessTokenRepository = null;
		expiredTokenCollector = null;
	}

	@Test
	void checkIfThreadStartsWhenCollectorIsConstructed() throws Exception {
		expiredTokenCollector = new ExpiredTokenCollector(accessTokenRepository);
		assertThat(expiredTokenCollector.getTokenCollectionThread().isAlive()).isEqualTo(true);
	}

	@Test
	void startAndStopThreadTest() throws Exception {
		expiredTokenCollector = new ExpiredTokenCollector(accessTokenRepository);
		assertThat(expiredTokenCollector.getTokenCollectionThread().isAlive()).isEqualTo(true);
		expiredTokenCollector.stopThread();
		assertThat(expiredTokenCollector.getTokenCollectionThread().isInterrupted()).isEqualTo(true);
	}

	@Test
	void checkIfItDeletesExpiredTokens() throws Exception {
		accessTokenRepository.add(new AccessToken("testClient", "Bearer", "token1", -100L));
		accessTokenRepository.add(new AccessToken("testClient", "Bearer", "token2", -100L));
		accessTokenRepository.add(new AccessToken("testClient", "Bearer", "token3", -100L));
		assertThat(accessTokenRepository.getAllIds().size()).isEqualTo(3);
		expiredTokenCollector = new ExpiredTokenCollector(accessTokenRepository);
		Thread.sleep(500);
		assertThat(accessTokenRepository.getAllIds().size()).isEqualTo(0);

	}

	@Test
	void checkIfItDoesntDeleteNonExpiredTokens() throws Exception {
		accessTokenRepository.add(new AccessToken("testClient", "Bearer", "token1", -100L));
		accessTokenRepository.add(new AccessToken("testClient", "Bearer", "token2", +100L));
		accessTokenRepository.add(new AccessToken("testClient", "Bearer", "token3", -100L));
		assertThat(accessTokenRepository.getAllIds().size()).isEqualTo(3);
		expiredTokenCollector = new ExpiredTokenCollector(accessTokenRepository);
		Thread.sleep(500);
		assertThat(accessTokenRepository.getAllIds().size()).isEqualTo(1);
	}
}
