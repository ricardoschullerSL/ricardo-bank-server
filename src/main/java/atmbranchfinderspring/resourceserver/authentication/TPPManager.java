package atmbranchfinderspring.resourceserver.authentication;

import atmbranchfinderspring.resourceserver.models.Client;
import atmbranchfinderspring.resourceserver.models.TPPClient;
import atmbranchfinderspring.resourceserver.repos.TPPClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Ricardo Schuller
 * @version 0.1.0
 * @since 0.1.0
 */
@Component
public class TPPManager implements ClientManager{

    private TPPClientRepository tppClientRepository;

    @Autowired
    public TPPManager(TPPClientRepository tppClientRepository) {
        this.tppClientRepository = tppClientRepository;
    }

    public void registerClient(Client client) {
        tppClientRepository.add(client);
    }

    public Boolean isClientRegistered(String clientId) {
        return tppClientRepository.contains(clientId);
    }

    public Boolean areCredentialsCorrect(String clientId, String clientSecret) {
        TPPClient client = (TPPClient) tppClientRepository.get(clientId);
        return !(client == null) && client.getCredentials().getClientSecret().equals(clientSecret);
    }
}
