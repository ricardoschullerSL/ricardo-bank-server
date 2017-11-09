package atmbranchfinderspring.resourceserver.authentication;


import atmbranchfinderspring.resourceserver.repos.AccessTokenRepository;
import atmbranchfinderspring.resourceserver.validation.accesstokens.AccessToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExpiredTokenCollectorTests {

	private AccessTokenRepository accessTokenRepository;
	private ExpiredTokenCollector expiredTokenCollector;
	private long sleepTime = 50L;

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
		expiredTokenCollector = new ExpiredTokenCollector(accessTokenRepository, true);
		assertThat(expiredTokenCollector.getTokenCollectionThread().isAlive()).isEqualTo(true);
	}

	@Test
	void startAndStopThreadTest() throws Exception {
		expiredTokenCollector = new ExpiredTokenCollector(accessTokenRepository, true);
		assertThat(expiredTokenCollector.getTokenCollectionThread().isAlive()).isEqualTo(true);
		expiredTokenCollector.stopThread();
		assertThat(expiredTokenCollector.getTokenCollectionThread().isInterrupted()).isEqualTo(true);
	}

	@Test
	void startAndStartThreadTest() throws Exception {
		expiredTokenCollector = new ExpiredTokenCollector(accessTokenRepository, true);
		assertThat(expiredTokenCollector.getTokenCollectionThread().isAlive()).isEqualTo(true);
		expiredTokenCollector.startThread();
		assertThat(expiredTokenCollector.getTokenCollectionThread().isAlive()).isEqualTo(true);
	}

	@Test
	void checkIfItDeletesExpiredTokens() throws Exception {
		accessTokenRepository.add(new AccessToken("testClient", AccessToken.TokenType.BEARER, "token2", -100L, "testId"));
		accessTokenRepository.add(new AccessToken("testClient", AccessToken.TokenType.BEARER, "token3", -100L, "testId"));
		accessTokenRepository.add(new AccessToken("testClient", AccessToken.TokenType.BEARER, "token1", -100L, "testId"));
		assertThat(accessTokenRepository.getAllIds().size()).isEqualTo(3);
		expiredTokenCollector = new ExpiredTokenCollector(accessTokenRepository, true);
		Thread.sleep(sleepTime);
		assertThat(accessTokenRepository.getAllIds().size()).isEqualTo(0);

	}

	@Test
	void checkIfItDoesntDeleteNonExpiredTokens() throws Exception {
		accessTokenRepository.add(new AccessToken("testClient", AccessToken.TokenType.BEARER, "token1", -100L, "testId"));
		accessTokenRepository.add(new AccessToken("testClient", AccessToken.TokenType.BEARER, "token2", +100L, "testId"));
		accessTokenRepository.add(new AccessToken("testClient", AccessToken.TokenType.BEARER, "token3", -100L, "testId"));
		assertThat(accessTokenRepository.getAllIds().size()).isEqualTo(3);
		expiredTokenCollector = new ExpiredTokenCollector(accessTokenRepository, true);
		Thread.sleep(sleepTime);
		assertThat(accessTokenRepository.getAllIds().size()).isEqualTo(1);
	}

	@Test
	void checkIfItDoesntRunWhenCollectorOnFlagIsFalse() throws Exception {
		accessTokenRepository.add(new AccessToken("testClient", AccessToken.TokenType.BEARER, "token1", -100L, "testId"));
		accessTokenRepository.add(new AccessToken("testClient", AccessToken.TokenType.BEARER, "token2", +100L, "testId"));
		accessTokenRepository.add(new AccessToken("testClient", AccessToken.TokenType.BEARER, "token3", -100L, "testId"));
		assertThat(accessTokenRepository.getAllIds().size()).isEqualTo(3);
		expiredTokenCollector = new ExpiredTokenCollector(accessTokenRepository, false);
		expiredTokenCollector.setCollectorOn(false);
		expiredTokenCollector.startThread();
		Thread.sleep(sleepTime);
		assertThat(accessTokenRepository.getAllIds().size()).isEqualTo(3);
	}

	@Test
	void checkCollectorOnGetterTest() {
		expiredTokenCollector = new ExpiredTokenCollector(accessTokenRepository, false);
		assertThat(expiredTokenCollector.isCollectorOn()).isEqualTo(true);
	}

	@Test
	void checkSleepTimeGetter() {
		expiredTokenCollector = new ExpiredTokenCollector(accessTokenRepository, false);
		assertThat(expiredTokenCollector.getSleepTime()).isEqualTo(30000L);
	}

	@Test
	void checkSleepTimeSetter() {
		expiredTokenCollector = new ExpiredTokenCollector(accessTokenRepository, false);
		expiredTokenCollector.setSleepTime(1200L);
		assertThat(expiredTokenCollector.getSleepTime()).isEqualTo(1200L);
	}
}
