package atmbranchfinderspring.resourceserver.controllers;

import atmbranchfinderspring.resourceserver.authentication.AuthenticationManagerImpl;
import atmbranchfinderspring.resourceserver.models.AccountRequest;
import atmbranchfinderspring.resourceserver.models.TPPClient;
import atmbranchfinderspring.resourceserver.repos.AccountRequestRepository;
import atmbranchfinderspring.resourceserver.repos.AuthorizationCodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
		when(accountRequestRepository.get("testaccountrequestId")).thenReturn(accountRequest);
		TPPClient tppClient = new TPPClient.TPPClientBuilder().setRedirectUri("Test/URI").build();
		when(authenticationManager.getTPPClient("testClient")).thenReturn(tppClient);
		mockMvc.perform(get("/authorizeApp/testaccountrequestId/1"))
				.andExpect(redirectedUrlPattern("Test/URI/*"));
	}

	@Test
	void failauthorizeAppPageAuthorizationTest() throws Exception {
		AccountRequest accountRequest = new AccountRequest();
		accountRequest.setStatus(AccountRequest.AccountRequestStatus.AWAITINGAUTHORIZATION);
		accountRequest.setClientId("testClient");
		when(accountRequestRepository.get("testaccountrequestId")).thenReturn(accountRequest);
		TPPClient tppClient = new TPPClient.TPPClientBuilder().setRedirectUri("http://testuri.com/test").build();
		when(authenticationManager.getTPPClient("testClient")).thenReturn(tppClient);
		mockMvc.perform(get("/authorizeApp/testaccountrequestId/0"))
				.andExpect(redirectedUrlPattern("**/test/"));
	}

}
