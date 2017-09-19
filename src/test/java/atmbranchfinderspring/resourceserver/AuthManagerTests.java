package atmbranchfinderspring.resourceserver;

import atmbranchfinderspring.resourceserver.authentication.AuthManager;
import atmbranchfinderspring.resourceserver.authentication.EncryptionManager;
import atmbranchfinderspring.resourceserver.authentication.PEMManager;
import atmbranchfinderspring.resourceserver.authentication.PEMManagerImp;
import atmbranchfinderspring.resourceserver.models.AccessToken;
import atmbranchfinderspring.resourceserver.models.ClientCredentials;
import atmbranchfinderspring.resourceserver.models.TPPClient;
import atmbranchfinderspring.resourceserver.repos.AccessTokenRepository;
import atmbranchfinderspring.resourceserver.repos.AdminRepository;
import atmbranchfinderspring.resourceserver.repos.TPPClientRepository;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

import static org.assertj.core.api.Assertions.assertThat;


public class AuthManagerTests {

	PEMManager pemManager;
	AuthManager authManager;
	EncryptionManager encryptionManager;
	AccessTokenRepository accessTokenRepository;
	AdminRepository adminRepository;
	TPPClientRepository tppClientRepository;

	@BeforeEach
	void setup() {
		pemManager = new PEMManagerMock();
		accessTokenRepository = new AccessTokenRepository();
		tppClientRepository = new TPPClientRepository();
		encryptionManager = new EncryptionManager(pemManager);
		adminRepository = new AdminRepository();
		authManager = new AuthManager(accessTokenRepository, tppClientRepository, adminRepository, encryptionManager);
	}

	@AfterEach
	void cleanup() {
		pemManager = null;
		accessTokenRepository = null;
		tppClientRepository = null;
		encryptionManager = null;
		adminRepository = null;
		authManager = null;
	}


	@Test
	@DisplayName("Check if AuthManager validates access token")
	public void accessTokenCheckerTest() {
		AccessToken testToken = new AccessToken("Bearer", "abc");
		accessTokenRepository.add(testToken);
		assertThat(authManager.isAccessTokenValid(testToken.getAccessToken())).isEqualTo(true);
	}

	@Test
	@DisplayName("Check if AuthManager validates access token")
	public void addAccessTokenToRepositoryTest() {
		AccessToken testToken = new AccessToken("Bearer", "abc");
		accessTokenRepository.add(testToken);
		assertThat(accessTokenRepository.getAllIds().size()).isEqualTo(1);
	}

	@Test
	@DisplayName("Check if access token repository deletes access token after use")
	public void accessTokenDeletionTest() {
		AccessToken testToken = new AccessToken("Bearer", "abc");
		accessTokenRepository.add(testToken);
		authManager.isAccessTokenValid(testToken.getAccessToken());
		assertThat(accessTokenRepository.getAllIds().size()).isEqualTo(0);
	}

	@Test
	@DisplayName("Check if AuthManager validates tpp client credentials correctly")
	public void credentialCheckerTest() {

		ClientCredentials credentials = new ClientCredentials("clientId", "clientSecret");
		TPPClient tppClient = new TPPClient.TPPClientBuilder()
				.setClientCredentials(credentials)
				.setRedirectUri(null)
				.setSSA(null).build();
		tppClientRepository.add(tppClient);

		assertThat(authManager.checkClientCredentials(credentials.getClientId(),credentials.getClientSecret())).isEqualTo(true);

	}

	@Test
	@DisplayName("Check if AuthManager invalidates tpp client if clientId is wrong")
	void wrongClientIdCredentialCheckerTest() {

		ClientCredentials credentials = new ClientCredentials("clientId", "clientSecret");
		TPPClient tppClient = new TPPClient.TPPClientBuilder()
				.setClientCredentials(credentials)
				.setRedirectUri(null)
				.setSSA(null).build();
		tppClientRepository.add(tppClient);

		assertThat(authManager.checkClientCredentials("wrongId",credentials.getClientSecret())).isEqualTo(false);
	}

	@Test
	@DisplayName("Check if AuthManager invalidates tpp client if clientId is wrong")
	void wrongPasswordCredentialCheckerTest() {

		ClientCredentials credentials = new ClientCredentials("clientId", "clientSecret");
		TPPClient tppClient = new TPPClient.TPPClientBuilder()
				.setClientCredentials(credentials)
				.setRedirectUri(null)
				.setSSA(null).build();
		tppClientRepository.add(tppClient);

		assertThat(authManager.checkClientCredentials(credentials.getClientId(),"wrongPassword")).isEqualTo(false);
	}

	private class PEMManagerMock implements PEMManager{
		@Override
		public JWTVerifier getJwtVerifier() {
			return null;
		}

		@Override
		public Algorithm getAlgorithm() {
			return null;
		}

		@Override
		public ECPrivateKey getPrivateKey() {
			return null;
		}

		@Override
		public ECPublicKey getPublicKey() {
			return null;
		}
	}

}
