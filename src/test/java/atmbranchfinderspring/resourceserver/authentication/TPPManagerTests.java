package atmbranchfinderspring.resourceserver.authentication;

import atmbranchfinderspring.resourceserver.models.Credentials;
import atmbranchfinderspring.resourceserver.models.TPPClient;
import atmbranchfinderspring.resourceserver.repos.TPPClientRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;


public class TPPManagerTests {

	private TPPManager tppManager;
	private AuthenticationManager authenticationManager;
	private TPPClientRepository tppClientRepository;

	@BeforeEach
	void setup() {
		authenticationManager = mock(AuthenticationManager.class);
		tppClientRepository = new TPPClientRepository();
		tppManager = new TPPManager(tppClientRepository, authenticationManager);
	}

	@AfterEach
	void cleanup() {
		tppClientRepository = null;
		tppManager = null;
	}


	@Test
	@DisplayName("registerClient adds TPPClient to repository")
	void registerTPPClientTest() {

		Credentials credentials = new Credentials("clientId", "clientSecret");
		TPPClient tppClient = new TPPClient.TPPClientBuilder()
				.setClientCredentials(credentials)
				.setRedirectUri("http://test.com/redirect")
				.setSSA(null).build();

		tppManager.registerClient(tppClient);
		assertThat(tppClientRepository.getAllIds().size()).isEqualTo(1);
	}

	@Test
	@DisplayName("Check if tppManager returns true if client is registered.")
	void clientIsRegisteredTest() {

		Credentials credentials = new Credentials("testClient", "testSecret");
		TPPClient client = new TPPClient(credentials, "https://testuri.com/redirect", null);
		tppClientRepository.add(client);
		assertThat(tppManager.isClientRegistered("testClient")).isEqualTo(true);
	}

	@Test
	@DisplayName("Check if tppManager returns false if client is not registered")
	void clientIsNotRegisteredTest() {

		Credentials credentials = new Credentials("testClient", "testSecret");
		TPPClient client = new TPPClient(credentials, "https://testuri.com/redirect", null);
		tppClientRepository.add(client);
		assertThat(tppManager.isClientRegistered("wrongClient")).isEqualTo(false);
	}

	@Test
	@DisplayName("areCredentialsCorrect returns true when credentials are correct")
	void credentialsAreCorrect() {

		Credentials credentials = new Credentials("testClient", "testSecret");
		TPPClient client = new TPPClient(credentials,"https://testuri.com/redirect", null);
		tppClientRepository.add(client);
		assertThat(tppManager.areCredentialsCorrect(credentials.getId(), credentials.getSecret())).isEqualTo(true);

	}

	@Test
	@DisplayName("areCredentialsCorrect returns false when clientId is wrong")
	void clientIdIsNotCorrect() {

		Credentials credentials = new Credentials("testClient", "testSecret");
		TPPClient client = new TPPClient(credentials,"https://testuri.com/redirect", null);
		tppClientRepository.add(client);
		assertThat(tppManager.areCredentialsCorrect("wrongClientId", credentials.getSecret())).isEqualTo(false);

	}

	@Test
	@DisplayName("areCredentialsCorrect returns false when clientSecret is wrong")
	void clientSecretIsNotCorrect() {

		Credentials credentials = new Credentials("testClient", "testSecret");
		TPPClient client = new TPPClient(credentials, "https://testuri.com/redirect", null);
		tppClientRepository.add(client);
		assertThat(tppManager.areCredentialsCorrect(credentials.getId(), "wrongSecret")).isEqualTo(false);

	}

}
