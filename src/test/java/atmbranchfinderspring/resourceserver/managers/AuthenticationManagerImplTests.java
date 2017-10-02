package atmbranchfinderspring.resourceserver.managers;

import atmbranchfinderspring.resourceserver.authentication.AuthenticationManagerImpl;
import atmbranchfinderspring.resourceserver.authentication.EncryptionManager;
import atmbranchfinderspring.resourceserver.authentication.PEMManager;
import atmbranchfinderspring.resourceserver.models.AccessToken;
import atmbranchfinderspring.resourceserver.models.Credentials;
import atmbranchfinderspring.resourceserver.models.TPPClient;
import atmbranchfinderspring.resourceserver.repos.*;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

import static org.assertj.core.api.Assertions.assertThat;


public class AuthenticationManagerImplTests {

	PEMManager pemManager;
	AuthenticationManagerImpl authenticationManagerImpl;
	EncryptionManager encryptionManager;
	AccessTokenRepository accessTokenRepository;
	AdminRepository adminRepository;
	UserRepository userRepository;
	TPPClientRepository tppClientRepository;
	AccountRequestRepository accountRequestRepository;
	AuthorizationCodeRepository authorizationCodeRepository;

	@BeforeEach
	void setup() {
		pemManager = new PEMManagerMock();
		accessTokenRepository = new AccessTokenRepository();
		tppClientRepository = new TPPClientRepository();
		encryptionManager = new EncryptionManager(pemManager);
		adminRepository = new AdminRepository();
		userRepository = null;
		accountRequestRepository = new AccountRequestRepository();
		authorizationCodeRepository = new AuthorizationCodeRepository();
		authenticationManagerImpl = new AuthenticationManagerImpl(accessTokenRepository, accountRequestRepository, authorizationCodeRepository, tppClientRepository, userRepository, adminRepository, encryptionManager);
	}

	@AfterEach
	void cleanup() {
		pemManager = null;
		accessTokenRepository = null;
		tppClientRepository = null;
		encryptionManager = null;
		adminRepository = null;
		authenticationManagerImpl = null;
	}


	@Test
	@DisplayName("Check if AuthenticationManagerImpl validates access token")
	public void accessTokenCheckerTest() {
		AccessToken testToken = new AccessToken("Bearer", "abc", 100L);
		accessTokenRepository.add(testToken);
		assertThat(authenticationManagerImpl.isAccessTokenValid(testToken.getAccessToken())).isEqualTo(true);
	}

	@Test
	@DisplayName("Check if AuthenticationManagerImpl validates access token")
	public void addAccessTokenToRepositoryTest() {
		AccessToken testToken = new AccessToken("Bearer", "abc", 100L);
		accessTokenRepository.add(testToken);
		assertThat(accessTokenRepository.getAllIds().size()).isEqualTo(1);
	}

	@Test
	@DisplayName("Check if access token repository deletes access token is expired")
	public void accessTokenDeletionTest() {
		AccessToken testToken = new AccessToken("Bearer", "abc", -100L);
		accessTokenRepository.add(testToken);
		authenticationManagerImpl.isAccessTokenValid(testToken.getAccessToken());
		assertThat(accessTokenRepository.getAllIds().size()).isEqualTo(0);
	}

	@Test
	@DisplayName("Check if AuthenticationManagerImpl validates tpp client credentials correctly")
	public void credentialCheckerTest() {

		Credentials credentials = new Credentials("clientId", "clientSecret");
		TPPClient tppClient = new TPPClient.TPPClientBuilder()
				.setClientCredentials(credentials)
				.setRedirectUri(null)
				.setSSA(null).build();
		tppClientRepository.add(tppClient);

		assertThat(authenticationManagerImpl.checkClientCredentials(credentials.getId(),credentials.getSecret())).isEqualTo(true);

	}

	@Test
	@DisplayName("Check if AuthenticationManagerImpl invalidates tpp client if clientId is wrong")
	void wrongClientIdCredentialCheckerTest() {

		Credentials credentials = new Credentials("clientId", "clientSecret");
		TPPClient tppClient = new TPPClient.TPPClientBuilder()
				.setClientCredentials(credentials)
				.setRedirectUri(null)
				.setSSA(null).build();
		tppClientRepository.add(tppClient);

		assertThat(authenticationManagerImpl.checkClientCredentials("wrongId",credentials.getSecret())).isEqualTo(false);
	}

	@Test
	@DisplayName("Check if AuthenticationManagerImpl invalidates tpp client if clientId is wrong")
	void wrongPasswordCredentialCheckerTest() {

		Credentials credentials = new Credentials("clientId", "clientSecret");
		TPPClient tppClient = new TPPClient.TPPClientBuilder()
				.setClientCredentials(credentials)
				.setRedirectUri(null)
				.setSSA(null).build();
		tppClientRepository.add(tppClient);

		assertThat(authenticationManagerImpl.checkClientCredentials(credentials.getId(),"wrongPassword")).isEqualTo(false);
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
