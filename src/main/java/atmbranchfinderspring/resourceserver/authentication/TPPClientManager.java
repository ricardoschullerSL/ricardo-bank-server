package atmbranchfinderspring.resourceserver.authentication;

import atmbranchfinderspring.resourceserver.models.Client;
import atmbranchfinderspring.resourceserver.models.TPPClient;

public interface TPPClientManager {

	void registerClient(TPPClient client);
	Boolean isClientRegistered(String clientId);

}
