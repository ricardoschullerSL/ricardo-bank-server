package atmbranchfinderspring.resourceserver;

import atmbranchfinderspring.resourceserver.authentication.PEMManagerImpl;
import atmbranchfinderspring.resourceserver.controllers.TPPController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ResourceServerApplicationTests {

	@LocalServerPort
	private int port;

	@MockBean
	private PEMManagerImpl pemManager;

	@Autowired
	private TPPController tppController;
// TODO: This test needs to be redone for Junit5
//	@Test
//	public void contextLoads() throws Exception {
//		assertThat(tppController).isNotNull();
//	}

}
