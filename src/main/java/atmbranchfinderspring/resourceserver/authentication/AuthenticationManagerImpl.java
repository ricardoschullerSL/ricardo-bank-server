package atmbranchfinderspring.resourceserver.authentication;

import atmbranchfinderspring.resourceserver.models.AccessToken;
import atmbranchfinderspring.resourceserver.models.Admin;
import atmbranchfinderspring.resourceserver.models.TPPClient;
import atmbranchfinderspring.resourceserver.repos.AccessTokenRepository;
import atmbranchfinderspring.resourceserver.repos.AdminRepository;
import atmbranchfinderspring.resourceserver.repos.TPPClientRepository;
import com.auth0.jwt.JWTVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;


@Component
public class AuthenticationManagerImpl implements AuthenticationManager {


    private AccessTokenRepository accessTokenRepository;
    private TPPClientRepository tppClientRepository;
    private AdminRepository adminRepository;
    private EncryptionManager encryptionManager;

    @Autowired
    public AuthenticationManagerImpl(AccessTokenRepository accessTokenRepository, TPPClientRepository tppClientRepository,
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
            if (accessTokenIsNotExpired(accessTokenRepository.get(token))) {
                return true;
            } else {
                accessTokenRepository.delete(token);
                return false;
            }
        } else {
            return false;
        }
    }

    public Boolean accessTokenIsNotExpired(AccessToken token) {
        return token.getExpirationDate().isAfter(LocalDateTime.now());
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
