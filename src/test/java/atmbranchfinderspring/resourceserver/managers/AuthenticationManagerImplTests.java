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
import org.mockito.Mockito;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

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
	@DisplayName("Check if AuthenticationManagerImpl validates request token")
	void requestTokenCheckerTest() {
		AccessToken testToken = new AccessToken("testClient", "Bearer", 100L, AccessToken.Grant.CLIENT_CREDENTIALS , Arrays.asList(AccessToken.Scope.ACCOUNTS));
		accessTokenRepository.add(testToken);
		assertThat(authenticationManager.isRequestTokenValid(testToken.getAccessToken())).isEqualTo(true);
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
	@DisplayName("Check if AuthenticationManagerImpl validates access token")
	void validateAccessTokenTest() {
		AccessToken testToken = new AccessToken("testClient", "Bearer", 100L, AccessToken.Grant.AUTHORIZATION_CODE, Arrays.asList(AccessToken.Scope.ACCOUNTS));
		accessTokenRepository.add(testToken);
		assertThat(authenticationManager.isAccessTokenValid(testToken.getAccessToken()));
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

		assertThat(authenticationManager.areClientCredentialsValid(credentials.getId(),credentials.getSecret())).isEqualTo(true);

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

		assertThat(authenticationManager.areClientCredentialsValid("wrongId",credentials.getSecret())).isEqualTo(false);
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

		assertThat(authenticationManager.areClientCredentialsValid(credentials.getId(),"wrongPassword")).isEqualTo(false);
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

		assertThat(authenticationManager.areAdminCredentialsValid("testAdmin", "testSecret")).isEqualTo(true);
		assertThat(authenticationManager.areAdminCredentialsValid("testAdmin", "wrongSecret")).isEqualTo(false);
	}

	@Test
	@DisplayName("Check if AuthenticationManagerImple validates AdminCredentials when admin is null")
	void nullAdminCredentialCheckerTest() {

		when(adminRepository.get("testAdmin")).thenReturn(null);
		assertThat(authenticationManager.areAdminCredentialsValid("testAdmin", "testSecret")).isEqualTo(false);
	}

	@Test
	@DisplayName("Check if areUserCredentialsValid returns false when user can't be found.")
	void nullUserCredentialCheckerTest() {
		when(userRepository.findByUserName("user")).thenReturn(null);
		assertThat(authenticationManager.areUserCredentialsValid("user","wrongsecret")).isEqualTo(false);
	}


	@Test
	@DisplayName("Check if addAdmin adds an admin")
	void addAdminTest() {
		authenticationManager.addAdmin("testAdmin", "testSecret");

		try {
			String salt = UUID.randomUUID().toString();
			String secret = "testsecret";
			byte[] hashedSecret = MessageDigest.getInstance("SHA-256").digest((secret + salt).getBytes(StandardCharsets.UTF_8));
			verify(adminRepository, times(1)).add(Mockito.any());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Check if AuthenticationManagerImpl validates user credentials correctly")
	void userCredentialCheckerTest() {
		try {
		String salt = UUID.randomUUID().toString();
		String secret = "testsecret";
		byte[] hashedSecret = MessageDigest.getInstance("SHA-256").digest((secret + salt).getBytes(StandardCharsets.UTF_8));

		User user = User.builder(101,"user", hashedSecret, salt);
		when(userRepository.findByUserName("user")).thenReturn(user);
		when(encryptionManager.SHA256((secret + salt))).thenCallRealMethod();

		assertThat(authenticationManager.areUserCredentialsValid("user","testsecret")).isEqualTo(true);
		assertThat(authenticationManager.areUserCredentialsValid("user","wrongsecret")).isEqualTo(false);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Check if isAuthorizationCodeValid checks if repo contains the string and deletes it")
	void checkAuthorizationCodeTest() {
		authorizationCodeRepository.add("testAuthCode");

		assertThat(authenticationManager.isAuthorizationCodeValid("testAuthCode")).isEqualTo(true);
		assertThat(authorizationCodeRepository.getAllIds().size()).isEqualTo(0);
		assertThat(authenticationManager.isAuthorizationCodeValid("testAuthCode")).isEqualTo(false);
	}

	@Test
	@DisplayName("Check if getAccountRequest returns account request")
	void getAccountRequestTest() {
		accountRequestRepository.add(new AccountRequest("testId", LocalDateTime.now(), LocalDateTime.now().plusDays(1L), new ArrayList<Permission>(),LocalDateTime.now(), LocalDateTime.now().plusDays(1L), AccountRequest.AccountRequestStatus.AWAITINGAUTHORIZATION  ));

		assertThat(authenticationManager.getAccountRequest("testId").getStatus()).isEqualTo(AccountRequest.AccountRequestStatus.AWAITINGAUTHORIZATION);
	}

	@Test
	@DisplayName("Check if getTPPClient returns TPPClient")
	void getTPPClientTest() {

		Credentials credentials = new Credentials("clientId", "clientSecret");
		TPPClient tppClient = new TPPClient.TPPClientBuilder()
				.setClientCredentials(credentials)
				.setRedirectUri(null)
				.setSSA(null).build();
		tppClientRepository.add(tppClient);

		assertThat(authenticationManager.getTPPClient("clientId").getCredentials().getId()).matches("clientId");
	}

}
