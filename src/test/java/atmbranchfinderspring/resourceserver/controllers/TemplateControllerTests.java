package atmbranchfinderspring.resourceserver.controllers;

import atmbranchfinderspring.resourceserver.authentication.AuthenticationManagerImpl;
import atmbranchfinderspring.resourceserver.validation.accountrequests.AccountRequest;
import atmbranchfinderspring.resourceserver.models.TPPClient;
import atmbranchfinderspring.resourceserver.repos.AccountRequestRepository;
import atmbranchfinderspring.resourceserver.repos.AuthorizationCodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest
public class TemplateControllerTests {


	private MockMvc mockMvc;
	private TemplateController templateController;
	private AuthenticationManagerImpl authenticationManager;
	private AccountRequestRepository accountRequestRepository;
	private AuthorizationCodeRepository authorizationCodeRepository;

	@BeforeEach
	void setup() {
		authenticationManager = mock(AuthenticationManagerImpl.class);
		accountRequestRepository = mock(AccountRequestRepository.class);
		authorizationCodeRepository = mock(AuthorizationCodeRepository.class);
		templateController = new TemplateController(authenticationManager, accountRequestRepository, authorizationCodeRepository);
		mockMvc = MockMvcBuilders.standaloneSetup(templateController).build();
	}

	@Test
	void loginPageTest() throws Exception {
		mockMvc.perform(get("/login/testaccountrequestId"))
				.andExpect(status().isOk());
	}

	@Test
	void authorizeAppPageTest() throws Exception {
		AccountRequest accountRequest = new AccountRequest();
		when(accountRequestRepository.get("testaccountrequestId")).thenReturn(accountRequest);
		mockMvc.perform(get("/authorizeApp/testaccountrequestId"))
				.andExpect(status().isOk());
	}

	@Test
	void authorizeAppPageAuthorizationTest() throws Exception {
		AccountRequest accountRequest = new AccountRequest();
		accountRequest.setStatus(AccountRequest.AccountRequestStatus.AWAITINGAUTHORIZATION);
		accountRequest.setClientId("testClient");
		when(authenticationManager.getAccountRequest("testaccountrequestId")).thenReturn(accountRequest);
		when(authenticationManager.isAccountRequestIdValid("testaccountrequestId")).thenReturn(true);
		TPPClient tppClient = new TPPClient.TPPClientBuilder().setRedirectUri("Test/URI").build();
		when(authenticationManager.getTPPClient("testClient")).thenReturn(tppClient);
		mockMvc.perform(get("/authorizeApp/testaccountrequestId/1"))
				.andExpect(redirectedUrlPattern("Test/URI/*"));
	}

	@Test
	void failAuthorizeAppPageAuthorizationTest() throws Exception {
		AccountRequest accountRequest = new AccountRequest();
		accountRequest.setStatus(AccountRequest.AccountRequestStatus.AWAITINGAUTHORIZATION);
		accountRequest.setClientId("testClient");
		when(authenticationManager.getAccountRequest("testaccountrequestId")).thenReturn(accountRequest);
		when(authenticationManager.isAccountRequestIdValid("testaccountrequestId")).thenReturn(true);
		TPPClient tppClient = new TPPClient.TPPClientBuilder().setRedirectUri("http://testuri.com/test").build();
		when(authenticationManager.getTPPClient("testClient")).thenReturn(tppClient);
		mockMvc.perform(get("/authorizeApp/testaccountrequestId/0"))
				.andExpect(status().is(302))
				.andExpect(redirectedUrlPattern("**/test/"));
	}

	@Test
	void authenticateAccountRequestIdTest() throws Exception {
		AccountRequest accountRequest = new AccountRequest();
		accountRequest.setStatus(AccountRequest.AccountRequestStatus.AWAITINGAUTHORIZATION);
		accountRequest.setClientId("testClient");
		when(authenticationManager.getAccountRequest("testId")).thenReturn(accountRequest);
		when(authenticationManager.areUserCredentialsValid("user", "secret")).thenReturn(true);
		when(authenticationManager.isAccountRequestIdValid("testId")).thenReturn(true);
		RequestBuilder request = post("/authenticate/testId")
				.param("id", "user")
				.param("secret", "secret");
		mockMvc.perform(request)
				.andExpect(status().is(302))
				.andExpect(redirectedUrl("/authorizeApp/testId"));
	}

	@Test
	void authenticateWrongAccountRequestIdTest() throws Exception {
		AccountRequest accountRequest = new AccountRequest();
		accountRequest.setStatus(AccountRequest.AccountRequestStatus.AWAITINGAUTHORIZATION);
		accountRequest.setClientId("testClient");
		when(authenticationManager.getAccountRequest("testId")).thenReturn(accountRequest);
		when(authenticationManager.areUserCredentialsValid("user", "secret")).thenReturn(true);
		RequestBuilder request = post("/authenticate/testId")
				.param("id", "user")
				.param("secret", "secret");
		mockMvc.perform(request)
				.andExpect(status().is(403));
	}

	@Test
	void authenticateWrongUsernameTest() throws Exception {
		AccountRequest accountRequest = new AccountRequest();
		accountRequest.setStatus(AccountRequest.AccountRequestStatus.AWAITINGAUTHORIZATION);
		accountRequest.setClientId("testClient");
		when(authenticationManager.getAccountRequest("testId")).thenReturn(accountRequest);
		when(authenticationManager.areUserCredentialsValid("user", "secret")).thenReturn(true);
		RequestBuilder request = post("/authenticate/testId")
				.param("id", "")
				.param("secret", "secret");
		mockMvc.perform(request)
				.andExpect(status().is(403));
	}

}
