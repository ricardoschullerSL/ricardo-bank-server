package atmbranchfinderspring.resourceserver.authentication;

import atmbranchfinderspring.resourceserver.models.Admin;
import atmbranchfinderspring.resourceserver.models.TPPClient;
import atmbranchfinderspring.resourceserver.repos.AccessTokenRepository;
import atmbranchfinderspring.resourceserver.repos.AdminRepository;
import atmbranchfinderspring.resourceserver.repos.TPPClientRepository;
import com.auth0.jwt.JWTVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;

/**
 * @author Ricardo Schuller
 * @version 0.1.0
 * @since 0.1.0
 */


@Component
public class AuthManager implements AuthenticationManager {


    private AccessTokenRepository accessTokenRepository;
    private TPPClientRepository tppClientRepository;
    private AdminRepository adminRepository;
    private EncryptionManager encryptionManager;

    @Autowired
    public AuthManager(AccessTokenRepository accessTokenRepository, TPPClientRepository tppClientRepository,
                       AdminRepository adminRepository, EncryptionManager encryptionManager) {
        this.adminRepository = adminRepository;
        this.tppClientRepository = tppClientRepository;
        this.accessTokenRepository = accessTokenRepository;
		this.encryptionManager = encryptionManager;
    }


    public JWTVerifier getJWTVerifier() {
        return encryptionManager.getJwtVerifier();
    }

    public EncryptionManager getEncryptionManager() {
        return encryptionManager;
    }

    public Boolean isAccessTokenValid(String token) {
        if (accessTokenRepository.contains(token)) {
            accessTokenRepository.delete(token);
            return true;
        } else {
            return false;
        }
    }

    public Boolean checkClientCredentials(String clientId, String clientSecret) {
        TPPClient client = (TPPClient) tppClientRepository.get(clientId);
        return !(client == null) && client.getCredentials().getClientSecret().equals(clientSecret);
    }

    public Boolean checkAdminCredentials(String adminId, String adminSecret) {
        Admin admin = adminRepository.get(adminId);
        if (admin != null) {
        	System.out.println("Checking hashed secret for " + admin.getAdminId());
            String saltedSecret = adminSecret + admin.getSalt();
            byte[] hash = encryptionManager.SHA256(saltedSecret);
            return Arrays.equals(hash, admin.getHashedSecret());
        } else {
            return false;
        }
    }

    public void addAdmin(String adminId, String adminSecret) {
        String salt = UUID.randomUUID().toString();
        byte[] hashedSecret = encryptionManager.SHA256(adminSecret + salt);
        Admin admin = new Admin(adminId, hashedSecret, salt);
        adminRepository.add(admin);
    }

}
