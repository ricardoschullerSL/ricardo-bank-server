package atmbranchfinderspring.resourceserver.authentication;

import atmbranchfinderspring.resourceserver.models.TPPClient;
import atmbranchfinderspring.resourceserver.repos.AccessTokenRepository;
import atmbranchfinderspring.resourceserver.repos.TPPClientRepository;
import com.auth0.jwt.JWTVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Ricardo Schuller
 * @version 0.1.0
 * @since 0.1.0
 */


@Component
public class AuthManager implements AuthenticationManager {


    private AccessTokenRepository accessTokenRepository;
    private TPPClientRepository tppClientRepository;
    private EncryptionManager encryptionManager;

    @Autowired
    public AuthManager(AccessTokenRepository accessTokenRepository, TPPClientRepository tppClientRepository, EncryptionManager encryptionManager) {
        this.tppClientRepository = tppClientRepository;
        this.accessTokenRepository = accessTokenRepository;
		this.encryptionManager = encryptionManager;
    }


    public JWTVerifier getJWTVerifier() {
        return encryptionManager.getJwtVerifier();
    }

    public Boolean isAccessTokenValid(String token) {
        if (accessTokenRepository.contains(token)) {
            accessTokenRepository.delete(token);
            return true;
        } else {
            return false;
        }
    }

    public Boolean areCredentialsCorrect(String clientId, String clientSecret) {
        TPPClient client = (TPPClient) tppClientRepository.get(clientId);
        return !(client == null) && client.getCredentials().getClientSecret().equals(clientSecret);
    }
}
