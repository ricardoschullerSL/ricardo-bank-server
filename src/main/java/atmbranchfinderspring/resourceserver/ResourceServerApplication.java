package atmbranchfinderspring.resourceserver;

import atmbranchfinderspring.resourceserver.repos.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ResourceServerApplication implements CommandLineRunner {

	@Autowired
	private AccountRepository accountRepository;

	public static void main(String[] args) {
		SpringApplication.run(ResourceServerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception{



	}
}
