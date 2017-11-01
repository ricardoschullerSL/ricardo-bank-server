package atmbranchfinderspring.resourceserver.repos;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@org.springframework.stereotype.Repository
public class AuthorizationCodeRepository{

	private Map<String, String> authorizationCodes;

	public AuthorizationCodeRepository() {
		this.authorizationCodes = new HashMap<>();
	}


	public void add(String authorizationCode, String accountRequestId) {
		authorizationCodes.put(authorizationCode, accountRequestId);
	}

	public String get(String authorizationCode) {
		return authorizationCodes.get(authorizationCode);
	}


	public Collection<String> getAllIds() {
		return authorizationCodes.keySet();
	}


	public boolean contains(String authorizationCode) {
		return authorizationCodes.keySet().contains(authorizationCode);
	}

	public void delete(String entity) {
		authorizationCodes.remove(entity);
	}
}
