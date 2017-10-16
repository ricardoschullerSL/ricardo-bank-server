package atmbranchfinderspring.resourceserver.repos;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@org.springframework.stereotype.Repository
public class AuthorizationCodeRepository implements Repository<String>{

	private List<String> authorizationCodes;

	public AuthorizationCodeRepository() {
		this.authorizationCodes = new ArrayList<>();
	}

	@Override
	public void add(String entity) {
		authorizationCodes.add(entity);
	}

	@Override
	public String get(String id) {
		return null;
	}

	@Override
	public Collection<String> getAllIds() {
		return authorizationCodes;
	}

	@Override
	public boolean contains(String id) {
		return authorizationCodes.contains(id);
	}

	@Override
	public void delete(String entity) {
		authorizationCodes.remove(entity);
	}
}
