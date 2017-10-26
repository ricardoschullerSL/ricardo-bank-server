package atmbranchfinderspring.resourceserver.controllers;

import atmbranchfinderspring.resourceserver.authentication.AuthenticationManager;
import atmbranchfinderspring.resourceserver.repos.AccessTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Base64;

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

	private MockMvc mockMvc;


	@BeforeEach
	void setup() {
		authenticationManager = mock(AuthenticationManager.class);
		accessTokenRepository = mock(AccessTokenRepository.class);
		tokenController = new TokenController(accessTokenRepository, authenticationManager);
		mockMvc = MockMvcBuilders.standaloneSetup(tokenController).build();
	}

	@Test
	void getAccessTokenThroughClientCredentialsTest() throws Exception {
		String authorization = "Basic " + Base64.getEncoder().encodeToString("testClient:testSecret".getBytes());
		String json = "{\"permissions\":[\"ReadAccountsBasic\",\"ReadAccountsDetail\"],\"redirect_uri\":\"http://google.com/\" }";
		RequestBuilder request = post("/token/access-token")
				.header("Authorization", authorization)
				.content(json);

		mockMvc.perform(request)
				.andExpect(status().is(201))
				.andExpect(content().contentType("application/json"))
				.andExpect(content().json("{\"tokenType\":\"Bearer\", \"clientId\":\"testClient\"}"));
	}

	@Test
	void getAccessTokenThroughAuthorizationCode() throws Exception {
		when(authenticationManager.isAuthorizationCodeValid(anyString())).thenReturn(true);
		String authorization = "Basic " + Base64.getEncoder().encodeToString("testClient:testSecret".getBytes());
		String json = "{\"permissions\":[\"ReadAccountsBasic\",\"ReadAccountsDetail\"],\"redirect_uri\":\"http://google.com/\" }";
		RequestBuilder request = post("/token/access-token/1234")
				.header("Authorization", authorization)
				.content(json);

		mockMvc.perform(request)
				.andExpect(status().is(201))
				.andExpect(content().contentType("application/json"))
				.andExpect(content().json("{\"tokenType\":\"Bearer\", \"clientId\":\"testClient\"}"));

	}

	@Test
	void failGetAccessTokenThroughAuthorizationCode() throws Exception {
		when(authenticationManager.isAuthorizationCodeValid(anyString())).thenReturn(false);
		String authorization = "Basic " + Base64.getEncoder().encodeToString("testClient:testSecret".getBytes());
		String json = "{\"permissions\":[\"ReadAccountsBasic\",\"ReadAccountsDetail\"],\"redirect_uri\":\"http://google.com/\" }";
		RequestBuilder request = post("/token/access-token/1234")
				.header("Authorization", authorization)
				.content(json);

		mockMvc.perform(request)
				.andExpect(status().is(403));
	}
}
