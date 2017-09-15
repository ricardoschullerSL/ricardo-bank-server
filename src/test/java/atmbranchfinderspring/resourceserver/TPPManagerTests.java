package atmbranchfinderspring.resourceserver;

import static org.assertj.core.api.Assertions.assertThat;
import atmbranchfinderspring.resourceserver.authentication.AuthManager;
import atmbranchfinderspring.resourceserver.authentication.TPPManager;
import atmbranchfinderspring.resourceserver.models.ClientCredentials;
import atmbranchfinderspring.resourceserver.models.TPPClient;
import atmbranchfinderspring.resourceserver.repos.TPPClientRepository;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TPPManagerTests {

	@Autowired
	private TPPManager tppManager;

	@Autowired
	private TPPClientRepository tppClientRepository;

	@Autowired
	private AuthManager authManager;

	@Test
	public void registerTPPClientTest() {
		try {
			DecodedJWT decodedJWT = authManager.getJWTVerifier().verify("eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiJ9.eyJzb2Z0d2FyZV9pZCI6IlNjb3R0eSIsImF1ZCI6IlNjb3R0IExvZ2ljIEJhbmsiLCJzb2Z0d2FyZV9zdGF0ZW1lbnQiOiJ0ZXN0c29mdHdhcmVzdGF0ZW1lbnQiLCJpc3MiOiJPcGVuIEJhbmtpbmciLCJyZWRpcmVjdF91cmkiOiJodHRwOi8vbG9jYWxob3N0OjgwODEvcmVkaXJlY3QiLCJqdGkiOiJqd3RJZCJ9.MEUCIQD5O-L-2hIzIVZ4BlB94AN-zObshw9cHhOhrM3uAy89QgIgaSMEUTKgD9ztYOj6SgQ-vYLIGH4RK6o7NSEPSeqC-yI");
			ClientCredentials credentials = new ClientCredentials("clientId", "clientSecret");
			TPPClient tppClient = new TPPClient.TPPClientBuilder()
					.setClientCredentials(credentials)
					.setRedirectUri(new URI("http://test.com/redirect"))
					.setSSA(decodedJWT).build();

			tppManager.registerClient(tppClient);
			assertThat(tppClientRepository.getAllIds().size()).isEqualTo(1);
		} catch (URISyntaxException e) {
			System.out.println(e);
		}
	}

}
