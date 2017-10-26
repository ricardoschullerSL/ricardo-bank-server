package atmbranchfinderspring.resourceserver.controllers;

import atmbranchfinderspring.resourceserver.authentication.AuthenticationManager;
import atmbranchfinderspring.resourceserver.authentication.TPPManager;
import atmbranchfinderspring.resourceserver.models.Credentials;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TPPControllerTests {
	private TPPController tppController;
	private ObjectMapper mapper;
	private TPPManager tppManager;
	private MockMvc mockMvc;

	@BeforeEach
	void setup() {
		tppManager = mock(TPPManager.class);
		mapper = new ObjectMapper();
		tppController = new TPPController(tppManager);

		mockMvc = MockMvcBuilders.standaloneSetup(tppController).build();
	}

	@Test
	void registerTPPClientTest() throws Exception {
		Credentials credentials = new Credentials("testId","testSecret");
		when(tppManager.registerTPPClientAndReturnCredentials("testJwt")).thenReturn(credentials);

		RequestBuilder request = post("/tpp/register")
				.content("testJwt")
				.accept("application/jwt");

		mockMvc.perform(request)
				.andExpect(status().isCreated())
				.andExpect(content().json("{\"id\":\"testId\", \"secret\":\"testSecret\"}"));

	}

	@Test
	void failRegisterTPPClientTest() throws Exception {
		Credentials credentials = new Credentials("testId","testSecret");
		when(tppManager.registerTPPClientAndReturnCredentials("testJwt")).thenReturn(null);

		RequestBuilder request = post("/tpp/register")
				.content("testJwt")
				.accept("application/jwt");

		mockMvc.perform(request)
				.andExpect(status().is4xxClientError());
	}
}
