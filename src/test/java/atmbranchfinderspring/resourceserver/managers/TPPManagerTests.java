package atmbranchfinderspring.resourceserver.managers;

import atmbranchfinderspring.resourceserver.authentication.TPPManager;
import atmbranchfinderspring.resourceserver.models.ClientCredentials;
import atmbranchfinderspring.resourceserver.models.TPPClient;
import atmbranchfinderspring.resourceserver.repos.TPPClientRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;


public class TPPManagerTests {

	private TPPManager tppManager;
	private TPPClientRepository tppClientRepository;

	@BeforeEach
	void setup() {
		tppClientRepository = new TPPClientRepository();
		tppManager = new TPPManager(tppClientRepository);
	}

	@AfterEach
	void cleanup() {
		tppClientRepository = null;
		tppManager = null;
	}


	@Test
	@DisplayName("registerClient adds TPPClient to repository")
	void registerTPPClientTest() {
		try {
			ClientCredentials credentials = new ClientCredentials("clientId", "clientSecret");
			TPPClient tppClient = new TPPClient.TPPClientBuilder()
					.setClientCredentials(credentials)
					.setRedirectUri(new URI("http://test.com/redirect"))
					.setSSA(null).build();

			tppManager.registerClient(tppClient);
			assertThat(tppClientRepository.getAllIds().size()).isEqualTo(1);
		} catch (URISyntaxException e) {
			System.out.println(e);
		}
	}

	@Test
	@DisplayName("Check if tppManager returns true if client is registered.")
	void clientIsRegisteredTest() {
		try {
			ClientCredentials credentials = new ClientCredentials("testClient", "testSecret");
			TPPClient client = new TPPClient(credentials, new URI("https://testuri.com/redirect"), null);
			tppClientRepository.add(client);
			assertThat(tppManager.isClientRegistered("testClient")).isEqualTo(true);

		} catch (URISyntaxException e) {
			System.out.println(e);
		}
	}

	@Test
	@DisplayName("Check if tppManager returns false if client is not registered")
	void clientIsNotRegisteredTest() {
		try {
			ClientCredentials credentials = new ClientCredentials("testClient", "testSecret");
			TPPClient client = new TPPClient(credentials, new URI("https://testuri.com/redirect"), null);
			tppClientRepository.add(client);
			assertThat(tppManager.isClientRegistered("wrongClient")).isEqualTo(false);

		} catch (URISyntaxException e) {
			System.out.println(e);
		}
	}

	@Test
	@DisplayName("areCredentialsCorrect returns true when credentials are correct")
	void credentialsAreCorrect() {
		try {
			ClientCredentials credentials = new ClientCredentials("testClient", "testSecret");
			TPPClient client = new TPPClient(credentials, new URI("https://testuri.com/redirect"), null);
			tppClientRepository.add(client);
			assertThat(tppManager.areCredentialsCorrect(credentials.getClientId(), credentials.getClientSecret())).isEqualTo(true);
		} catch (URISyntaxException e) {
			System.out.println(e);
		}
	}

	@Test
	@DisplayName("areCredentialsCorrect returns false when clientId is wrong")
	void clientIdIsNotCorrect() {
		try {
			ClientCredentials credentials = new ClientCredentials("testClient", "testSecret");
			TPPClient client = new TPPClient(credentials, new URI("https://testuri.com/redirect"), null);
			tppClientRepository.add(client);
			assertThat(tppManager.areCredentialsCorrect("wrongClientId", credentials.getClientSecret())).isEqualTo(false);
		} catch (URISyntaxException e) {
			System.out.println(e);
		}
	}

	@Test
	@DisplayName("areCredentialsCorrect returns false when clientSecret is wrong")
	void clientSecretIsNotCorrect() {
		try {
			ClientCredentials credentials = new ClientCredentials("testClient", "testSecret");
			TPPClient client = new TPPClient(credentials, new URI("https://testuri.com/redirect"), null);
			tppClientRepository.add(client);
			assertThat(tppManager.areCredentialsCorrect(credentials.getClientId(), "wrongSecret")).isEqualTo(false);
		} catch (URISyntaxException e) {
			System.out.println(e);
		}
	}

}
