package atmbranchfinderspring.resourceserver;

import atmbranchfinderspring.resourceserver.authentication.AuthManager;
import atmbranchfinderspring.resourceserver.authentication.EncryptionManager;
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

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthManagerTests {

	@MockBean
	EncryptionManager encryptionManager;

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
		try {
			ClientCredentials credentials = new ClientCredentials("clientId", "clientSecret");
			TPPClient tppClient = new TPPClient.TPPClientBuilder()
					.setClientCredentials(credentials)
					.setRedirectUri(new URI("http://test.com/redirect"))
					.setSSA(null).build();
			tppClientRepository.add(tppClient);

			assertThat(authManager.areCredentialsCorrect(credentials.getClientId(),credentials.getClientSecret())).isEqualTo(true);

		} catch (URISyntaxException e) {
			System.out.println(e);
		}
	}
}
