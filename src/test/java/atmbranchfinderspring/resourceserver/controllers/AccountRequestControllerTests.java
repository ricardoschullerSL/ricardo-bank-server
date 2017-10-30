package atmbranchfinderspring.resourceserver.controllers;

import atmbranchfinderspring.resourceserver.validation.accountrequests.AccountRequestValidator;
import atmbranchfinderspring.resourceserver.authentication.TPPManager;
import atmbranchfinderspring.resourceserver.models.*;
import atmbranchfinderspring.resourceserver.repos.AccessTokenRepository;
import atmbranchfinderspring.resourceserver.repos.AccountRequestRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
		List<String> permissions = Arrays.asList("ReadAccountsBasic", "ReadAccountsDetail");
		accountRequest.setId("testAccountRequestId");
		accountRequest.setPermissions(permissions);
		List<AccessToken.Scope> scopes = Arrays.asList(AccessToken.Scope.ACCOUNTS);
		AccessToken accessToken = new AccessToken("testClientId","Bearer", 100L, AccessToken.Grant.CLIENT_CREDENTIALS, scopes);
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
		List<String> permissions = Arrays.asList("WrongReadAccountsBasic", "ReadAccountsDetail");
		accountRequest.setId("testAccountRequestId");
		accountRequest.setPermissions(permissions);
		List<AccessToken.Scope> scopes = Arrays.asList(AccessToken.Scope.ACCOUNTS);
		AccessToken accessToken = new AccessToken("testClientId","Bearer", 100L, AccessToken.Grant.CLIENT_CREDENTIALS, scopes);
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
		AccessToken accessToken = new AccessToken("testClient", "Bearer", 100L, AccessToken.Grant.CLIENT_CREDENTIALS, Arrays.asList(AccessToken.Scope.ACCOUNTS));
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
		AccessToken accessToken = new AccessToken("testClient", "Bearer", 100L, AccessToken.Grant.CLIENT_CREDENTIALS, Arrays.asList(AccessToken.Scope.ACCOUNTS));
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
