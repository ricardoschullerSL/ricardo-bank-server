package atmbranchfinderspring.resourceserver;

import atmbranchfinderspring.resourceserver.authentication.PEMManager;
import atmbranchfinderspring.resourceserver.authentication.TPPManager;
import atmbranchfinderspring.resourceserver.models.ClientCredentials;
import atmbranchfinderspring.resourceserver.models.TPPClient;
import atmbranchfinderspring.resourceserver.repos.TPPClientRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TPPManagerTests {

	@MockBean
	PEMManager pemManager;

	@Autowired
	private TPPManager tppManager;

	@Autowired
	private TPPClientRepository tppClientRepository;


	@Test
	public void registerTPPClientTest() {
		try {
			ClientCredentials credentials = new ClientCredentials("clientId", "clientSecret");
			TPPClient tppClient = new TPPClient.TPPClientBuilder()
					.setClientCredentials(credentials)
					.setRedirectUri(new URI("http://test.com/redirect"))
					.setSSA(null).build();

			tppManager.registerClient(tppClient);
			assertThat(tppClientRepository.getAllIds().size()).isEqualTo(1);
		} catch (URISyntaxException e) {
			System.out.println(e);
		}
	}

}
