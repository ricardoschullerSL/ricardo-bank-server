package atmbranchfinderspring.resourceserver.managers;

import atmbranchfinderspring.resourceserver.authentication.PEMManager;
import atmbranchfinderspring.resourceserver.authentication.PEMManagerImpl;
import org.junit.jupiter.api.BeforeEach;

public class PEMManagerTests {

	private PEMManager pemManager;

	@BeforeEach()
	void setup() {
		pemManager = new PEMManagerImpl();
	}


}
