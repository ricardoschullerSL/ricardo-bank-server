package atmbranchfinderspring.resourceserver;

import atmbranchfinderspring.resourceserver.authentication.AuthManager;
import atmbranchfinderspring.resourceserver.authentication.PEMManager;
import atmbranchfinderspring.resourceserver.models.AccessToken;
import atmbranchfinderspring.resourceserver.models.ClientCredentials;
import atmbranchfinderspring.resourceserver.models.TPPClient;
import atmbranchfinderspring.resourceserver.repos.AccessTokenRepository;
import atmbranchfinderspring.resourceserver.repos.TPPClientRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthManagerTests {

	@MockBean
	PEMManager pemManager;

	@Autowired
	AuthManager authManager;
	@Autowired
	AccessTokenRepository accessTokenRepository;
	@Autowired
	TPPClientRepository tppClientRepository;


	@Test
	public void accessTokenCheckerTest() {
		AccessToken testToken = new AccessToken("Bearer", "abc");
		accessTokenRepository.add(testToken);

		assertThat(accessTokenRepository.getAllIds().size()).isEqualTo(1);
		assertThat(authManager.isAccessTokenValid(testToken.getAccessToken())).isEqualTo(true);
		assertThat(accessTokenRepository.getAllIds().size()).isEqualTo(0);
	}

	@Test
	public void credentialCheckerTest() {

		ClientCredentials credentials = new ClientCredentials("clientId", "clientSecret");
		TPPClient tppClient = new TPPClient.TPPClientBuilder()
				.setClientCredentials(credentials)
				.setRedirectUri(null)
				.setSSA(null).build();
		tppClientRepository.add(tppClient);

		assertThat(authManager.checkClientCredentials(credentials.getClientId(),credentials.getClientSecret())).isEqualTo(true);

	}


}
