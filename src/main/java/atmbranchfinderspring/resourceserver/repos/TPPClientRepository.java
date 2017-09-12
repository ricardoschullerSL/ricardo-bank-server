package atmbranchfinderspring.resourceserver.repos;

import atmbranchfinderspring.resourceserver.authentication.TPPClient;

import java.util.Collection;
import java.util.HashMap;
/**
 * @author Ricardo Schuller
 * @version 0.1.0
 * @since 0.1.0
 */
@org.springframework.stereotype.Repository
public class TPPClientRepository implements Repository<TPPClient> {

    private HashMap<String, TPPClient> tppClients;

    public TPPClientRepository() {
        this.tppClients = new HashMap<>();
    }

    @Override
    public void add(TPPClient entity) {
        tppClients.put(entity.getCredentials().getClientId(), entity);
    }

    @Override
    public Collection<TPPClient> getAll() {
        return null;
    }

    @Override
    public void delete(TPPClient entity) {
        tppClients.remove(entity);
    }

    @Override
    public void delete(String clientId) { tppClients.remove(clientId); }

    public TPPClient findByClientId(String clientId) {
        if (tppClients.containsKey(clientId)) {
            return tppClients.get(clientId);
        }
        return null;
    }
}
