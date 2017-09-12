package atmbranchfinderspring.resourceserver.repos;

import atmbranchfinderspring.resourceserver.models.AccessToken;

import java.util.Collection;
import java.util.HashMap;

/**
 * @author Ricardo Schuller
 * @version 0.1.0
 * @since 0.1.0
 */
@org.springframework.stereotype.Repository
public class AccessTokenRepository implements Repository<AccessToken> {

    private HashMap<String, AccessToken> accessTokens;

    public AccessTokenRepository() {
        this.accessTokens = new HashMap<>();
    }

    public synchronized  AccessToken get(String token) {
        if (accessTokens.containsKey(token)) {
            return accessTokens.get(token);
        }
        return null;
    }

    public synchronized Boolean contains(String token) {
        return accessTokens.containsKey(token);
    }

    @Override
    public synchronized void add(AccessToken entity) {
        accessTokens.put(entity.getAccessToken(), entity);
    }

    @Override
    public Collection<AccessToken> getAll() {
        return null;
    }

    @Override
    public synchronized void delete(AccessToken entity) {
        accessTokens.remove(entity.getAccessToken());
    }

    public synchronized void delete(String token) { accessTokens.remove(token); }
}
