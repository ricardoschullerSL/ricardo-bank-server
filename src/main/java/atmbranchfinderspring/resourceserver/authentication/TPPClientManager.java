package atmbranchfinderspring.resourceserver.authentication;

import atmbranchfinderspring.resourceserver.models.Credentials;
import atmbranchfinderspring.resourceserver.models.TPPClient;

/**
 * TTPClientManager is the class that takes care of registering and validating Trusted Third Party clients.
 */

public interface TPPClientManager {

	void registerClient(TPPClient client);
	Boolean isClientRegistered(String clientId);
	Credentials registerTPPClientAndReturnCredentials(String clientJwt);

}
