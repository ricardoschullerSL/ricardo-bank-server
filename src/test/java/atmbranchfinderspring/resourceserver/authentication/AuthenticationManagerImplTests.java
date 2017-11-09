package atmbranchfinderspring.resourceserver.authentication;

import atmbranchfinderspring.resourceserver.models.Admin;
import atmbranchfinderspring.resourceserver.models.Credentials;
import atmbranchfinderspring.resourceserver.models.TPPClient;
import atmbranchfinderspring.resourceserver.models.User;
import atmbranchfinderspring.resourceserver.repos.*;
import atmbranchfinderspring.resourceserver.validation.accesstokens.AccessToken;
import atmbranchfinderspring.resourceserver.validation.accesstokens.AccessTokenValidator;
import atmbranchfinderspring.resourceserver.validation.accesstokens.AccessTokenValidatorImpl;
import atmbranchfinderspring.resourceserver.validation.accountrequests.AccountRequest;
import atmbranchfinderspring.resourceserver.validation.accountrequests.Permission;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


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
		AccessToken testToken = new AccessToken("testClient", AccessToken.TokenType.BEARER, 100L, AccessToken.Grant.CLIENT_CREDENTIALS , "testid");
		accessTokenRepository.add(testToken);
		assertThat(authenticationManager.isRequestTokenValid(testToken.getAccessToken())).isEqualTo(true);
	}


	@Test
	@DisplayName("Check if AuthenticationManagerImpl validates access token")
	void validateAccessTokenTest() {
		AccessToken testToken = new AccessToken("testClient", AccessToken.TokenType.BEARER, 100L, AccessToken.Grant.AUTHORIZATION_CODE, "testId");
		accessTokenRepository.add(testToken);
		AccountRequest accountRequest = new AccountRequest();
		accountRequest.setPermissions(new HashSet<Permission>(){{add(Permission.ReadAccountsBasic);}});
		accountRequest.setAccountRequestId("testId");
		accountRequestRepository.add(accountRequest);
		assertThat(authenticationManager.isAccessTokenValid(testToken.getAccessToken(), new HashSet<Permission>(){{add(Permission.ReadAccountsBasic);}})).isEqualTo(true);
	}

	@Test
	@DisplayName("Check if AuthenticationManagerImpl validates access token with incorrect grant.")
	void validateInvalidGrantAccessTokenTest() {
		AccessToken testToken = new AccessToken("testClient", AccessToken.TokenType.BEARER, 100L, null, "testid");
		accessTokenRepository.add(testToken);
		assertThat(authenticationManager.isAccessTokenValid(testToken.getAccessToken(), new TreeSet<Permission>(){{add(Permission.ReadAccountsBasic);}})).isEqualTo(false);
	}

	@Test
	@DisplayName("Check if AuthenticationManagerImpl validates access token with incorrect permission.")
	void validateInvalidScopeAccessTokenTest() {
		AccessToken testToken = new AccessToken("testClient", AccessToken.TokenType.BEARER, 100L, AccessToken.Grant.AUTHORIZATION_CODE, "testId");
		accessTokenRepository.add(testToken);
		assertThat(authenticationManager.isAccessTokenValid(testToken.getAccessToken(), new TreeSet<Permission>(){{add(Permission.ReadAccountsBasic);}})).isEqualTo(false);
	}

	@Test
	@DisplayName("Check if AuthenticationManagerImpl validates expired access token")
	void validateExpiredAccessTokenTest() {
		AccessToken testToken = new AccessToken("testClient", AccessToken.TokenType.BEARER, -100L, AccessToken.Grant.AUTHORIZATION_CODE, "testId");
		accessTokenRepository.add(testToken);
		assertThat(authenticationManager.isAccessTokenValid(testToken.getAccessToken(),new TreeSet<Permission>(){{add(Permission.ReadAccountsBasic);}})).isEqualTo(false);
	}

	@Test
	@DisplayName("Check if AuthenticationManagerImpl validates non existing access token")
	void validateNonExistingAccessTokenTest() {
		AccessToken testToken = new AccessToken("testClient", AccessToken.TokenType.BEARER, 100L, AccessToken.Grant.AUTHORIZATION_CODE, "testId");
		accessTokenRepository.add(testToken);
		assertThat(authenticationManager.isAccessTokenValid("wrongtoken", new TreeSet<Permission>(){{add(Permission.ReadAccountsBasic);}})).isEqualTo(false);
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
	@DisplayName("Check if isAuthorizationCodeValid checks if repo contains the string.")
	void checkAuthorizationCodeTest() {
		authorizationCodeRepository.add("testAuthCode", "testAccountRequestId");

		assertThat(authenticationManager.isAuthorizationCodeValid("testAuthCode")).isEqualTo(true);
		assertThat(authorizationCodeRepository.getAllIds().size()).isEqualTo(1);
	}

	@Test
	@DisplayName("Check if getAccountRequest returns account request")
	void getAccountRequestTest() {
		accountRequestRepository.add(new AccountRequest("testId","testClient", LocalDateTime.now(), LocalDateTime.now().plusDays(1L), new HashSet<Permission>(),LocalDateTime.now(), LocalDateTime.now().plusDays(1L), AccountRequest.AccountRequestStatus.AWAITINGAUTHORIZATION  ));

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

	@Test
	@DisplayName("Check if isAccountRequestIdValid return correct boolean")
	void isAccountRequestIdValidTest() {
		accountRequestRepository.add(new AccountRequest("testId","testClient", LocalDateTime.now(), LocalDateTime.now().plusDays(1L), new HashSet<>() ,LocalDateTime.now(), LocalDateTime.now().plusDays(1L), AccountRequest.AccountRequestStatus.AWAITINGAUTHORIZATION  ));
		assertThat(authenticationManager.isAccountRequestIdValid("testId")).isEqualTo(true);
		assertThat(authenticationManager.isAccountRequestIdValid("wrongId")).isEqualTo(false);
	}

	@Test
	@DisplayName("Check if getAccountRequestFromAuthorizationCode returns Account Request")
	void getAccountRequestFromAuthorizationCodeTest() {
		authorizationCodeRepository.add("testCode", "testId");
		accountRequestRepository.add(new AccountRequest("testId", "testClient", null, null, null, null, null, AccountRequest.AccountRequestStatus.AWAITINGAUTHORIZATION));
		AccountRequest accountRequest = authenticationManager.getAccountRequestFromAuthorizationCode("testCode");
		assertThat(accountRequest.getClientId()).isEqualTo("testClient");
		AccountRequest wrongRequest = authenticationManager.getAccountRequestFromAuthorizationCode("wrongCode");
		assertThat(wrongRequest).isNull();
	}

}
