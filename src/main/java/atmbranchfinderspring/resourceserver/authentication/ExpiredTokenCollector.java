package atmbranchfinderspring.resourceserver.authentication;

import atmbranchfinderspring.resourceserver.repos.AccessTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;

@Component
public class ExpiredTokenCollector {

	private AccessTokenRepository accessTokenRepository;
	private Thread tokenCollectionThread;
	private boolean collectorOn = true;
	private long sleepTime = 30000L;

	@Autowired
	public ExpiredTokenCollector(AccessTokenRepository accessTokenRepository) {
		this.accessTokenRepository = accessTokenRepository;
		this.tokenCollectionThread = new Thread(new Runnable() {
			@Override
			public void run() {
				collectExpiredTokens();
			}
		});
		System.out.println("Starting ExpiredTokenCollector Thread.");
		tokenCollectionThread.start();
	}

	public void collectExpiredTokens() {
		while(collectorOn) {
			doRun();
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void doRun() {
		Collection<String> tokenIds = accessTokenRepository.getAllIds();

		if (tokenIds.size() == 0) {
			return;
		}

		int counter = 0;
		System.out.println("Starting expired token collection run.");
		for (String tokenId: tokenIds) {
			if(accessTokenRepository.get(tokenId).getExpirationDate().isBefore(LocalDateTime.now())) {
				accessTokenRepository.delete(tokenId);
				counter++;
			}
		}

		System.out.print("Run is finished. " + counter + " tokens deleted." );

	}

	public void startThread() {
		if (tokenCollectionThread.isAlive()) {
			return;
		}
		System.out.println(LocalDateTime.now().toString() + "Starting ExpiredTokenCollector thread.");
		this.tokenCollectionThread.start();
	}

	public void stopThread() {
		if (tokenCollectionThread.isAlive()) {
			System.out.println(LocalDateTime.now().toString() + "Stopping ExpiredTokenCollector thread.");
			tokenCollectionThread.interrupt();
		}
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
