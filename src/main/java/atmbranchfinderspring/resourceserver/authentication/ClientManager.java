package atmbranchfinderspring.resourceserver.authentication;

import atmbranchfinderspring.resourceserver.models.Client;

public interface ClientManager {

	void registerClient(Client client);
	Boolean isClientRegistered(String clientId);

}
