package atmbranchfinderspring.resourceserver.managers;

import atmbranchfinderspring.resourceserver.authentication.*;
import atmbranchfinderspring.resourceserver.authentication.accesstokenvalidation.AccessTokenValidator;
import atmbranchfinderspring.resourceserver.authentication.accesstokenvalidation.AccessTokenValidatorImpl;
import atmbranchfinderspring.resourceserver.models.*;
import atmbranchfinderspring.resourceserver.repos.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.assertThat;


public class AuthenticationManagerImplTests {

	AuthenticationManagerImpl authenticationManager;
	EncryptionManager encryptionManager;
	AccessTokenRepository accessTokenRepository;
	AccessTokenValidator accessTokenValidator;
	AdminRepository adminRepository;
	UserRepository userRepository;
	TPPClientRepository tppClientRepository;
	AccountRequestRepository accountRequestRepository;
	AuthorizationCodeRepository authorizationCodeRepository;

	@BeforeEach
	void setup() {
		accessTokenRepository = new AccessTokenRepository();
		accessTokenValidator = new AccessTokenValidatorImpl(accessTokenRepository);
		tppClientRepository = new TPPClientRepository();
		encryptionManager = mock(EncryptionManager.class);
		adminRepository = mock(AdminRepository.class);
		userRepository = mock(UserRepository.class);
		accountRequestRepository = new AccountRequestRepository();
		authorizationCodeRepository = new AuthorizationCodeRepository();
		authenticationManager = new AuthenticationManagerImpl(accessTokenRepository, accessTokenValidator, accountRequestRepository, authorizationCodeRepository, null, tppClientRepository, userRepository, adminRepository, encryptionManager);
	}

	@AfterEach
	void cleanup() {
		accessTokenRepository = null;
		tppClientRepository = null;
		encryptionManager = null;
		adminRepository = null;
		authenticationManager = null;
	}


	@Test
	@DisplayName("Check if getJWTVerifier calls encryptionManager.getJwtVerifier()")
	void getJWTVerifierTest() {
		authenticationManager.getJWTVerifier();
		verify(encryptionManager, times(1)).getJwtVerifier();
	}

	@Test
	@DisplayName("Check if getEncryptionManager() returns an object")
	void getEncryptionManagerTest() {
		assertThat(authenticationManager.getEncryptionManager()).isNotNull();
	}

	@Test
	@DisplayName("Check if AuthenticationManagerImpl validates access token")
	void requestTokenCheckerTest() {
		AccessToken testToken = new AccessToken("testClient", "Bearer", 100L, AccessToken.Grant.CLIENT_CREDENTIALS , Arrays.asList(AccessToken.Scope.ACCOUNTS));
		accessTokenRepository.add(testToken);
		assertThat(authenticationManager.isRequestTokenValid(testToken.getAccessToken())).isEqualTo(true);
	}


	@Test
	@DisplayName("Check if AuthenticationManagerImpl validates access token")
	void addRequestTokenToRepositoryTest() {
		AccessToken testToken = new AccessToken("testClient", "Bearer", "test",100L );
		accessTokenRepository.add(testToken);
		assertThat(accessTokenRepository.getAllIds().size()).isEqualTo(1);
	}

	// DON'T IMMEDIATELY DELETE IMPROPER ACCESS TOKENS, LET TOKEN COLLECTOR TAKE CARE OF IT.
//	@Test
//	@DisplayName("Check if access token repository deletes access token is expired")
//	void requestTokenDeletionTest() {
//		AccessToken testToken = new AccessToken("testClient", "Bearer", "test", -100L );
//		accessTokenRepository.add(testToken);
//		authenticationManager.isRequestTokenValid(testToken.getAccessToken());
//		assertThat(accessTokenRepository.getAllIds().size()).isEqualTo(0);
//	}

	@Test
	@DisplayName("Check if isAccessTokenValid validates a token")
	void isAccessTokenValidTest() {
		AccessToken testToken = new AccessToken("testClient", "Bearer", "test",100L );
	}

	@Test
	@DisplayName("Check if AuthenticationManagerImpl validates tpp client credentials correctly")
	void credentialCheckerTest() {

		Credentials credentials = new Credentials("clientId", "clientSecret");
		TPPClient tppClient = new TPPClient.TPPClientBuilder()
				.setClientCredentials(credentials)
				.setRedirectUri(null)
				.setSSA(null).build();
		tppClientRepository.add(tppClient);

		assertThat(authenticationManager.checkClientCredentials(credentials.getId(),credentials.getSecret())).isEqualTo(true);

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

		assertThat(authenticationManager.checkClientCredentials("wrongId",credentials.getSecret())).isEqualTo(false);
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

		assertThat(authenticationManager.checkClientCredentials(credentials.getId(),"wrongPassword")).isEqualTo(false);
	}

	@Test
	@DisplayName("Check if AuthenticationManagerImple validates AdminCredentials")
	void adminCredentialCheckerTest() throws Exception{
		String secret = "testSecret";
		String salt = "testSalt";
		byte[] mockHash = MessageDigest.getInstance("SHA-256").digest((secret + salt).getBytes(StandardCharsets.UTF_8));

		Admin mockAdmin = new Admin("testAdmin", mockHash, "testSalt");
		when(adminRepository.get("testAdmin")).thenReturn(mockAdmin);
		when(encryptionManager.SHA256((secret + salt))).thenReturn(mockHash);

		assertThat(authenticationManager.checkAdminCredentials("testAdmin", "testSecret")).isEqualTo(true);
		assertThat(authenticationManager.checkAdminCredentials("testAdmin", "wrongSecret")).isEqualTo(false);
	}

	@Test
	@DisplayName("Check if checkAuthorizationCode checks if repo contains the string and deletes it")
	void checkAuthorizationCodeTest() {
		authorizationCodeRepository.add("testAuthCode");

		assertThat(authenticationManager.checkAuthorizationCode("testAuthCode")).isEqualTo(true);
		assertThat(authorizationCodeRepository.getAllIds().size()).isEqualTo(0);
		assertThat(authenticationManager.checkAuthorizationCode("testAuthCode")).isEqualTo(false);
	}

	@Test
	@DisplayName("Check if getAccountRequest returns account request")
	void getAccountRequestTest() {
		accountRequestRepository.add(new AccountRequest("testId", LocalDateTime.now(), LocalDateTime.now().plusDays(1L), new ArrayList<Permission>(),LocalDateTime.now(), LocalDateTime.now().plusDays(1L), AccountRequest.AccountRequestStatus.AWAITINGAUTHORIZATION  ));

		assertThat(authenticationManager.getAccountRequest("testId").getStatus()).isEqualTo(AccountRequest.AccountRequestStatus.AWAITINGAUTHORIZATION);
	}

}
