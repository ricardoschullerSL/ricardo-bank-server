package atmbranchfinderspring.resourceserver.authentication;

import org.junit.jupiter.api.BeforeEach;

public class PEMManagerTests {

	private PEMManager pemManager;

	@BeforeEach()
	void setup() {
		pemManager = new PEMManagerImpl();
	}


}
