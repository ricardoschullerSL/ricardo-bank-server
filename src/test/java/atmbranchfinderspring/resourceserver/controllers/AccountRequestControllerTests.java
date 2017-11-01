package atmbranchfinderspring.resourceserver.controllers;

import atmbranchfinderspring.resourceserver.authentication.TPPManager;
import atmbranchfinderspring.resourceserver.models.Credentials;
import atmbranchfinderspring.resourceserver.models.TPPClient;
import atmbranchfinderspring.resourceserver.repos.AccessTokenRepository;
import atmbranchfinderspring.resourceserver.repos.AccountRequestRepository;
import atmbranchfinderspring.resourceserver.validation.accesstokens.AccessToken;
import atmbranchfinderspring.resourceserver.validation.accountrequests.AccountRequest;
import atmbranchfinderspring.resourceserver.validation.accountrequests.AccountRequestValidator;
import atmbranchfinderspring.resourceserver.validation.accountrequests.IncomingAccountRequest;
import atmbranchfinderspring.resourceserver.validation.accountrequests.Permission;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class AccountRequestControllerTests {

	private MockMvc mockMvc;
	private AccountRequestController accountRequestController;
	private ObjectMapper mapper = new ObjectMapper();

	private AccountRequestRepository accountRequestRepository;
	private AccountRequestValidator accountRequestValidator;
	private AccessTokenRepository accessTokenRepository;
	private TPPManager tppManager;


	@BeforeEach
	void setup() {
		accessTokenRepository = mock(AccessTokenRepository.class);
		accountRequestRepository = mock(AccountRequestRepository.class);
		tppManager = mock(TPPManager.class);
		accountRequestValidator = new AccountRequestValidator();
		accountRequestController = new AccountRequestController(accountRequestRepository, accountRequestValidator, accessTokenRepository, tppManager);
		mockMvc = MockMvcBuilders.standaloneSetup(accountRequestController).build();
	}


	@Test
	void postAccountRequestTest() throws Exception {
		IncomingAccountRequest accountRequest = new IncomingAccountRequest();
		Set<Permission> permissions = new TreeSet<Permission>(Arrays.asList(Permission.ReadAccountsBasic, Permission.ReadAccountsDetail));
		accountRequest.setId("testAccountRequestId");
		accountRequest.setPermissions(Arrays.asList("ReadAccountsBasic", "ReadAccountsDetail"));
		AccessToken accessToken = new AccessToken("testClientId","Bearer", 100L, AccessToken.Grant.CLIENT_CREDENTIALS, accountRequest.getId());
		when(accessTokenRepository.get("testtoken")).thenReturn(accessToken);
		RequestBuilder request = post("/account-requests")
				.header("Authorization", "Bearer testtoken")
				.contentType("application/json")
				.content(mapper.writeValueAsString(accountRequest));

		mockMvc.perform(request)
				.andExpect(status().isOk());
	}

	@Test
	void failedPostAccountRequestTest() throws Exception {
		IncomingAccountRequest accountRequest = new IncomingAccountRequest();
		Set<Permission> permissions = new TreeSet<>(Arrays.asList(Permission.ReadAccountsBasic, Permission.ReadAccountsDetail));
		accountRequest.setId("testAccountRequestId");
		accountRequest.setPermissions(Arrays.asList("wrongPermission"));
		AccessToken accessToken = new AccessToken("testClientId","Bearer", 100L, AccessToken.Grant.CLIENT_CREDENTIALS, accountRequest.getId());
		when(accessTokenRepository.get("testtoken")).thenReturn(accessToken);
		RequestBuilder request = post("/account-requests")
				.header("Authorization", "Bearer testtoken")
				.contentType("application/json")
				.content(mapper.writeValueAsString(accountRequest));

		mockMvc.perform(request)
				.andExpect(status().is(400));
	}

	@Test
	void getAllAccountRequestsTest() throws Exception {
		AccessToken accessToken = new AccessToken("testClient", "Bearer", 100L,
				AccessToken.Grant.CLIENT_CREDENTIALS, "testId");
		when(accessTokenRepository.contains(anyString())).thenReturn(true);
		when(accessTokenRepository.get(anyString())).thenReturn(accessToken);

		TPPClient tppClient = new TPPClient(new Credentials("tppclient", "tppsecret"), "http://testuri.com/", null);
		tppClient.addAccountRequestResponse(new AccountRequest());
		tppClient.addAccountRequestResponse(new AccountRequest());
		when(tppManager.getTPPClient(anyString())).thenReturn(tppClient);

		RequestBuilder request = get("/account-requests")
				.header("Authorization", "Bearer testtoken");

		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"));

	}

	@Test
	void failGetAllAccountRequestsTest() throws Exception {
		AccessToken accessToken = new AccessToken("testClient", "Bearer", 100L,
				AccessToken.Grant.CLIENT_CREDENTIALS, "testId");
		when(accessTokenRepository.contains(anyString())).thenReturn(false);
		when(accessTokenRepository.get(anyString())).thenReturn(accessToken);

		TPPClient tppClient = new TPPClient(new Credentials("tppclient", "tppsecret"), "http://testuri.com/", null);
		tppClient.addAccountRequestResponse(new AccountRequest());
		tppClient.addAccountRequestResponse(new AccountRequest());
		when(tppManager.getTPPClient(anyString())).thenReturn(tppClient);

		RequestBuilder request = get("/account-requests")
				.header("Authorization", "Bearer testtoken");

		mockMvc.perform(request)
				.andExpect(status().is(400));

	}


	@Test
	void getSingleAccountRequestsTest() throws Exception {

		AccountRequest accountRequest = new AccountRequest();
		accountRequest.setAccountRequestId("testId");
		accountRequest.setClientId("testClient");
		when(accountRequestRepository.contains(anyString())).thenReturn(true);
		when(accountRequestRepository.get(anyString())).thenReturn(accountRequest);

		RequestBuilder request = get("/account-requests/testId");

		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"));

	}

	@Test
	void failGetSingleAccountRequestsTest() throws Exception {

		AccountRequest accountRequest = new AccountRequest();
		accountRequest.setAccountRequestId("testId");
		accountRequest.setClientId("testClient");
		when(accountRequestRepository.contains(anyString())).thenReturn(false);
		when(accountRequestRepository.get(anyString())).thenReturn(accountRequest);

		RequestBuilder request = get("/account-requests/testId");

		mockMvc.perform(request)
				.andExpect(status().is(400));

	}
}
