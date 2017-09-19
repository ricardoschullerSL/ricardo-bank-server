package atmbranchfinderspring.resourceserver.repos;

import atmbranchfinderspring.resourceserver.models.AccessToken;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;


@org.springframework.stereotype.Repository
public class AccessTokenRepository implements Repository<AccessToken> {

    private HashMap<String, AccessToken> accessTokens;

    public AccessTokenRepository() {
        this.accessTokens = new HashMap<>();
    }

    public synchronized  AccessToken get(String token) {
        return accessTokens.get(token);
    }

    public synchronized Boolean contains(String token) {
        return accessTokens.containsKey(token);
    }

    public synchronized void add(AccessToken entity) {
        accessTokens.put(entity.getAccessToken(), entity);
    }

    public Collection<String> getAllIds() {
        return accessTokens.keySet();
    }

    public synchronized void delete(AccessToken entity) {
        accessTokens.remove(entity.getAccessToken());
    }

    public synchronized void delete(String token) { accessTokens.remove(token); }
}
