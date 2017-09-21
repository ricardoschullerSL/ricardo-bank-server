package atmbranchfinderspring.resourceserver.authentication;

import atmbranchfinderspring.resourceserver.models.AccountRequestResponse;
import atmbranchfinderspring.resourceserver.models.TPPClient;
import atmbranchfinderspring.resourceserver.repos.TPPClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


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
        return !(client == null) && client.getCredentials().getClientSecret().equals(clientSecret);
    }

    public void addAccountRequestToClient(String clientId, AccountRequestResponse accountRequestResponse) {
        TPPClient client = tppClientRepository.get(clientId);
        client.addAccountRequestResponse(accountRequestResponse);
    }

    public TPPClient getTPPClient(String clientId) {
    	return tppClientRepository.get(clientId);
    }
}
