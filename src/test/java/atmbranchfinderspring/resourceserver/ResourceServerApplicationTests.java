package atmbranchfinderspring.resourceserver;

import atmbranchfinderspring.resourceserver.authentication.EncryptionManager;
import atmbranchfinderspring.resourceserver.authentication.PEMManager;
import atmbranchfinderspring.resourceserver.controllers.TPPController;
import com.fasterxml.jackson.annotation.JsonProperty;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ResourceServerApplicationTests {

	@LocalServerPort
	private int port;

	@MockBean
	PEMManager pemManager;

	@Autowired
	private TPPController tppController;

	@Test
	public void contextLoads() throws Exception {
		assertThat(tppController).isNotNull();
	}

}
