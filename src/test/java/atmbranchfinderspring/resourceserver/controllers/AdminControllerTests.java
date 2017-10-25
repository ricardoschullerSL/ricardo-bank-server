package atmbranchfinderspring.resourceserver.controllers;

import atmbranchfinderspring.resourceserver.authentication.AuthenticationManager;
import atmbranchfinderspring.resourceserver.authentication.AuthenticationManagerImpl;
import atmbranchfinderspring.resourceserver.authentication.TPPManager;
import atmbranchfinderspring.resourceserver.models.Admin;
import atmbranchfinderspring.resourceserver.repos.AdminRepository;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureGenerationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class AdminControllerTests {

	private MockMvc mockMvc;
	private AdminController adminController;
	private AuthenticationManager authenticationManager;
	private TPPManager tppManager;
	private AdminRepository adminRepository;

	@BeforeEach
	void setup() {

		authenticationManager = mock(AuthenticationManagerImpl.class);
		tppManager = mock(TPPManager.class);
		adminRepository = mock(AdminRepository.class);

		adminController = new AdminController(tppManager, authenticationManager, adminRepository);
		mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
	}

	@Test
	void getJWTTest() throws Exception {
		Algorithm algorithm = new Algorithm("SHA-256", "Stub") {
			@Override
			public void verify(DecodedJWT jwt) throws SignatureVerificationException {

			}

			@Override
			public byte[] sign(byte[] contentBytes) throws SignatureGenerationException {
				return new byte[0];
			}
		};
		when(authenticationManager.getEncryptionManager().getAlgorithm()).thenReturn(algorithm);
		RequestBuilder request = post("/getjwt")
				.content("{\"software_id\": \"testId\", \"redirect_uri\": \"test.uri\"}")
				.contentType("application/json");

		mockMvc.perform(request)
				.andDo(print());
	}

}
