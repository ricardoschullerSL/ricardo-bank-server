package atmbranchfinderspring.resourceserver.repos;

import atmbranchfinderspring.resourceserver.models.Credentials;
import atmbranchfinderspring.resourceserver.models.TPPClient;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

public class TPPClientRepositoryTests {

	private TPPClientRepository tppClientRepository;

	@BeforeEach
	void setup() throws URISyntaxException {
		tppClientRepository = new TPPClientRepository();
		DecodedJWT jwt = null;
		tppClientRepository.add(new TPPClient(new Credentials("testClientId","testClientSecret"),"http://localhost:8080/testURI", jwt ));
	}

	@AfterEach
	void cleanup() {
		tppClientRepository = null;
	}

	@Test
	void getGetsToken() {
		TPPClient client = tppClientRepository.get("testClientId");
		assertThat(client.getCredentials().getSecret()).isEqualTo("testClientSecret");
	}

	@Test
	void getsReturnNullIfTokenDoesNotExist() {
		TPPClient client = tppClientRepository.get("aaa");
		assertThat(client).isEqualTo(null);
	}

	@Test
	void containsReturnsBooleanTrue() {
		assertThat(tppClientRepository.contains("testClientId")).isEqualTo(true);
	}

	@Test
	void containsReturnsBooleanFalse() {
		assertThat(tppClientRepository.contains("aaa")).isEqualTo(false);
	}

	@Test
	void addAddsToken() throws URISyntaxException {
		int size = tppClientRepository.getAllIds().size();
		TPPClient client = new TPPClient(new Credentials("testClientId2", "testClientSecret2"),"http://localhost:8080/secondURI",null);
		tppClientRepository.add(client);
		assertThat(tppClientRepository.getAllIds().size()).isEqualTo(size + 1);
	}

	@Test
	void deleteDeletesToken() {
		int size = tppClientRepository.getAllIds().size();
		tppClientRepository.delete("testClientId");
		assertThat(tppClientRepository.getAllIds().size()).isEqualTo(size - 1);
	}

	@Test
	void deleteDeletesTokenWithTokenParameter() {
		int size = tppClientRepository.getAllIds().size();
		TPPClient client = tppClientRepository.get("testClientId");
		tppClientRepository.delete(client);
		assertThat(tppClientRepository.getAllIds().size()).isEqualTo(size - 1);
	}
}