package atmbranchfinderspring.resourceserver.authentication;

import atmbranchfinderspring.resourceserver.repos.AccessTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;


/**
 * ExpiredTokenCollector is a helper object that spawns a seperate Thread where it periodically goes through
 * the AccessTokenRepository to check if any access token are expired. If they are expired, it deletes them.
 */

@Component
public class ExpiredTokenCollector {

	private AccessTokenRepository accessTokenRepository;
	private static Thread tokenCollectionThread;
	private boolean collectorOn = true;
	private long sleepTime = 30000L;

	@Autowired
	public ExpiredTokenCollector(AccessTokenRepository accessTokenRepository) {
		this.accessTokenRepository = accessTokenRepository;
		tokenCollectionThread = new Thread(() -> collectExpiredTokens());
		System.out.println(LocalDateTime.now().toString() + " Starting ExpiredTokenCollector Thread.");
		tokenCollectionThread.start();
	}

	public ExpiredTokenCollector(AccessTokenRepository accessTokenRepository, boolean startNow) {
		this.accessTokenRepository = accessTokenRepository;
		tokenCollectionThread = new Thread(() -> collectExpiredTokens());
		if (startNow) {
			System.out.println(LocalDateTime.now().toString() + " Starting ExpiredTokenCollector Thread.");
			tokenCollectionThread.start();
		}
	}

	private void collectExpiredTokens() {
		while(collectorOn) {
			doRun();
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				//Do nothing.
			}
		}
	}

	private void doRun() {
		Collection<String> tokenIds = accessTokenRepository.getAllIds();

		if (tokenIds.size() == 0) {
			return;
		}

		int counter = 0;
		System.out.println(LocalDateTime.now().toString() + " Starting expired token collection run.");
		for (String tokenId: tokenIds) {
			if(accessTokenRepository.get(tokenId).getExpirationDate().isBefore(LocalDateTime.now())) {
				accessTokenRepository.delete(tokenId);
				counter++;
			}
		}

		System.out.println(LocalDateTime.now().toString() + " Run is finished. " + counter + " tokens deleted. " +
				"Number of active tokens: " + accessTokenRepository.getAllIds().size());

	}

	public void startThread() {
		if (tokenCollectionThread.isAlive()) {
			return;
		}
		System.out.println(LocalDateTime.now().toString() + " Starting ExpiredTokenCollector thread.");
		tokenCollectionThread.start();
	}

	public void stopThread() {
		tokenCollectionThread.interrupt();
	}

	public Thread getTokenCollectionThread() {
		return ExpiredTokenCollector.tokenCollectionThread;
	}

	public boolean isCollectorOn() {
		return collectorOn;
	}

	public void setCollectorOn(boolean collectorOn) {
		this.collectorOn = collectorOn;
	}

	public long getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(long sleepTime) {
		this.sleepTime = sleepTime;
	}
}
