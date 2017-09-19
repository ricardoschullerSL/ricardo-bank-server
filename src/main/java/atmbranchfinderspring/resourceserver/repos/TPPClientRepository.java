package atmbranchfinderspring.resourceserver.repos;

import atmbranchfinderspring.resourceserver.models.Client;

import java.util.Collection;
import java.util.HashMap;

@org.springframework.stereotype.Repository
public class TPPClientRepository implements Repository<Client> {


    private HashMap<String, Client> tppClients;

    public TPPClientRepository() {
        this.tppClients = new HashMap<>();
    }

    public void add(Client entity) {
        tppClients.put(entity.getCredentials().getClientId(), entity);
    }

    public Collection<String> getAllIds() {
        return tppClients.keySet();
    }

    public void delete(Client entity) {
        tppClients.remove(entity);
    }

    public void delete(String clientId) { tppClients.remove(clientId); }

    public Boolean contains(String clientId) {
        return tppClients.get(clientId) != null;
    }

    public Client get(String clientId) {
        return tppClients.get(clientId);
    }
}
