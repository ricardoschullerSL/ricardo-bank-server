package atmbranchfinderspring.resourceserver.controllers;

import atmbranchfinderspring.resourceserver.authentication.AuthenticationManager;
import atmbranchfinderspring.resourceserver.repos.AccessTokenRepository;
import atmbranchfinderspring.resourceserver.validation.accesstokens.AccessToken;
import atmbranchfinderspring.resourceserver.validation.accountrequests.AccountRequest;
import atmbranchfinderspring.resourceserver.validation.accountrequests.Permission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class TokenControllerTests {

	private TokenController tokenController;
	private AccessTokenRepository accessTokenRepository;
	private AuthenticationManager authenticationManager;
	private MockEnvironment env;
	private String baseUrl = "/open-banking/v1.1";
	private MockMvc mockMvc;


	@BeforeEach
	void setup() {
		env = new MockEnvironment();
		env.setProperty("accesstoken.expirationtime", "60");
		env.setProperty("refreshtoken.expirationtime", "60");
		authenticationManager = mock(AuthenticationManager.class);
		accessTokenRepository = mock(AccessTokenRepository.class);
		tokenController = new TokenController(accessTokenRepository, authenticationManager, env);
		mockMvc = MockMvcBuilders.standaloneSetup(tokenController).build();
	}

	@Test
	void getAccessTokenThroughClientCredentialsTest() throws Exception {
		String authorization = "Basic " + Base64.getEncoder().encodeToString("testClient:testSecret".getBytes());
		String json = "{\"permissions\":[\"ReadAccountsBasic\",\"ReadAccountsDetail\"],\"redirect_uri\":\"http://google.com/\" }";
		RequestBuilder request = post(baseUrl + "/access-token")
				.header("Authorization", authorization)
				.content(json);

		mockMvc.perform(request)
				.andExpect(status().is(201))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(content().json("{\"tokenType\":\"REQUEST\", \"clientId\":\"testClient\"}"));
	}

	@Test
	void getAccessTokenThroughAuthorizationCode() throws Exception {
		when(authenticationManager.isAuthorizationCodeValid(anyString())).thenReturn(true);
		AccountRequest accountRequest = new AccountRequest();
		Set<Permission> permissions = new HashSet<>();
		permissions.add(Permission.ReadAccountsBasic);
		accountRequest.setPermissions(permissions);
		when(authenticationManager.getAccountRequestFromAuthorizationCode(anyString())).thenReturn(accountRequest);
		String authorization = "Basic " + Base64.getEncoder().encodeToString("testClient:testSecret".getBytes());
		String json = "{\"permissions\":[\"ReadAccountsBasic\",\"ReadAccountsDetail\"],\"redirect_uri\":\"http://google.com/\" }";
		RequestBuilder request = post(baseUrl + "/access-token/1234")
				.header("Authorization", authorization)
				.content(json);

		mockMvc.perform(request)
				.andExpect(status().is(201))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(content().json("{\"tokenType\":\"REFRESH\", \"clientId\":\"testClient\"}"));

	}

	@Test
	void failGetAccessTokenThroughAuthorizationCode() throws Exception {
		when(authenticationManager.isAuthorizationCodeValid(anyString())).thenReturn(false);
		String authorization = "Basic " + Base64.getEncoder().encodeToString("testClient:testSecret".getBytes());
		String json = "{\"permissions\":[\"ReadAccountsBasic\",\"ReadAccountsDetail\"],\"redirect_uri\":\"http://google.com/\" }";
		RequestBuilder request = post(baseUrl + "/access-token/1234")
				.header("Authorization", authorization)
				.content(json);

		mockMvc.perform(request)
				.andExpect(status().is(403));
	}

	@Test
	void getAccessTokenFromRefreshTokenTest() throws Exception {
		String authorization = "Basic " + Base64.getEncoder().encodeToString("testClient:testSecret".getBytes());
		AccessToken accessToken = new AccessToken("abcd1234", "testClient", AccessToken.TokenType.REFRESH, LocalDateTime.now(), LocalDateTime.now().plusSeconds(60L), AccessToken.Grant.CLIENT_CREDENTIALS,"testAccountRequestId");
		when(accessTokenRepository.get("abcd1234")).thenReturn(accessToken);
		RequestBuilder request = post(baseUrl + "/access-token/refresh/abcd1234")
				.header("Authorization", authorization);

		mockMvc.perform(request)
				.andExpect(status().is(201))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(content().json("{\"tokenType\":\"ACCESS\", \"clientId\":\"testClient\"}"));

	}

	@Test
	void getAccessTokenFromRefreshTokenWithWrongClientIdTest() throws Exception {
		String authorization = "Basic " + Base64.getEncoder().encodeToString("testClient:testSecret".getBytes());
		AccessToken accessToken = new AccessToken("abcd1234", "wrongClient", AccessToken.TokenType.REFRESH, LocalDateTime.now(), LocalDateTime.now().plusSeconds(60L), AccessToken.Grant.CLIENT_CREDENTIALS,"testAccountRequestId");
		when(accessTokenRepository.get("abcd1234")).thenReturn(accessToken);
		RequestBuilder request = post(baseUrl + "/access-token/refresh/abcd1234")
				.header("Authorization", authorization);

		mockMvc.perform(request)
				.andExpect(status().is(403));
	}
}
