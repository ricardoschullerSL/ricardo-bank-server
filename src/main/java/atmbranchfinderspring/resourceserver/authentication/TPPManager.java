package atmbranchfinderspring.resourceserver.authentication;

import atmbranchfinderspring.resourceserver.models.AccountRequest;
import atmbranchfinderspring.resourceserver.models.Credentials;
import atmbranchfinderspring.resourceserver.models.TPPClient;
import atmbranchfinderspring.resourceserver.repos.TPPClientRepository;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Default TPPClientManager implementation.
 */

@Component
public class TPPManager implements TPPClientManager{

    private TPPClientRepository tppClientRepository;
    private AuthenticationManager authenticationManager;

    @Autowired
    public TPPManager(TPPClientRepository tppClientRepository, AuthenticationManager authenticationManager) {
        this.tppClientRepository = tppClientRepository;
        this.authenticationManager = authenticationManager;
    }

    public void registerClient(TPPClient client) {
        tppClientRepository.add(client);
    }

    public Boolean isClientRegistered(String clientId) {
        return tppClientRepository.contains(clientId);
    }

    public Boolean areCredentialsCorrect(String clientId, String clientSecret) {
        TPPClient client = tppClientRepository.get(clientId);
        return !(client == null) && client.getCredentials().getSecret().equals(clientSecret);
    }

    public void addAccountRequestToClient(String clientId, AccountRequest accountRequest) {
        TPPClient client = tppClientRepository.get(clientId);
        client.addAccountRequestResponse(accountRequest);
    }

    public TPPClient getTPPClient(String clientId) {
    	return tppClientRepository.get(clientId);
    }

	@Override
	public Credentials registerTPPClientAndReturnCredentials(String clientJwt) {
		try {
			DecodedJWT jwt = authenticationManager.getJWTVerifier().verify(clientJwt);
			String clientId = jwt.getClaim("software_id").asString();
			String redirectUri = jwt.getClaim("redirect_uri").asString();
			if (isClientRegistered(clientId)) {
				System.out.println("Incoming request is for client that's already registered.");
			}

			Credentials credentials = new Credentials(clientId, UUID.randomUUID().toString());
			TPPClient client = new TPPClient(credentials, redirectUri, jwt);

			registerClient(client);

			return credentials;
		} catch (JWTVerificationException e) {
			return null;
		}
	}
}
