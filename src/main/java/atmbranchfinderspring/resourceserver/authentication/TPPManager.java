package atmbranchfinderspring.resourceserver.authentication;

import atmbranchfinderspring.resourceserver.repos.TPPClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Ricardo Schuller
 * @version 0.1.0
 * @since 0.1.0
 */
@Component
public class TPPManager {

    private TPPClientRepository tppClientRepository;

    @Autowired
    public TPPManager(TPPClientRepository tppClientRepository) {
        this.tppClientRepository = tppClientRepository;
    }

    public void registerTPPClient(TPPClient client) {
        tppClientRepository.add(client);
    }

    public Boolean isTPPClientRegistered(String clientId) {
        return (tppClientRepository.findByClientId(clientId) != null);
    }

    public Boolean areCredentialsCorrect(String clientId, String clientSecret) {
        TPPClient client = tppClientRepository.findByClientId(clientId);
        return !(client == null) && client.getCredentials().getClientSecret().equals(clientSecret);
    }
}
