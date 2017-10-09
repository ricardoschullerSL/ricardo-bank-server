package atmbranchfinderspring.resourceserver.authentication;

import atmbranchfinderspring.resourceserver.models.AccountRequest;
import atmbranchfinderspring.resourceserver.models.TPPClient;
import atmbranchfinderspring.resourceserver.repos.TPPClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Default TPPClientManager implementation.
 */

@Component
public class TPPManager implements TPPClientManager{

    private TPPClientRepository tppClientRepository;

    @Autowired
    public TPPManager(TPPClientRepository tppClientRepository) {
        this.tppClientRepository = tppClientRepository;
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
}
