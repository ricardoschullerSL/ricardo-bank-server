package atmbranchfinderspring.resourceserver.repos;

import atmbranchfinderspring.resourceserver.validation.accesstokens.AccessToken;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AccessTokenRepository implements Repository<AccessToken> {

    private ConcurrentHashMap<String, AccessToken> accessTokens;

    public AccessTokenRepository() {
        this.accessTokens = new ConcurrentHashMap<>();
    }

    public AccessToken get(String token) {
        return accessTokens.get(token);
    }

    public boolean contains(String token) {
        return accessTokens.containsKey(token);
    }

    public void add(AccessToken entity) {
        accessTokens.put(entity.getAccessToken(), entity);
    }

    public Collection<String> getAllIds() {
        return accessTokens.keySet();
    }

    public void delete(AccessToken entity) {
        accessTokens.remove(entity.getAccessToken());
    }

    public void delete(String token) { accessTokens.remove(token); }
}
