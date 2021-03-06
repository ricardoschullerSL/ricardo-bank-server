package atmbranchfinderspring.resourceserver.repos;

import atmbranchfinderspring.resourceserver.models.TPPClient;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;

@Component
public class TPPClientRepository implements Repository<TPPClient> {


	private HashMap<String, TPPClient> tppClients;

	public TPPClientRepository() {
		this.tppClients = new HashMap<>();
	}

	public void add(TPPClient entity) {
		tppClients.put(entity.getCredentials().getId(), entity);
	}

	public Collection<String> getAllIds() {
		return tppClients.keySet();
	}

	public void delete(TPPClient entity) {
		tppClients.remove(entity.getCredentials().getId());
	}

	public void delete(String clientId) {
		tppClients.remove(clientId);
	}

	public boolean contains(String clientId) {
		return tppClients.get(clientId) != null;
	}

	public TPPClient get(String clientId) {
		return tppClients.get(clientId);
	}
}

