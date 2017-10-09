package atmbranchfinderspring.resourceserver.authentication;

import atmbranchfinderspring.resourceserver.models.*;
import atmbranchfinderspring.resourceserver.repos.*;
import com.auth0.jwt.JWTVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

/**
 * Default implementation of the AuthenticationManager interface
 */

@Component
public class AuthenticationManagerImpl implements AuthenticationManager {


    private AccessTokenRepository accessTokenRepository;
    private AccountRequestRepository accountRequestRepository;
    private AuthorizationCodeRepository authorizationCodeRepository;
    private TPPClientRepository tppClientRepository;
    private UserRepository userRepository;
    private AdminRepository adminRepository;
    private EncryptionManager encryptionManager;

    @Autowired
    public AuthenticationManagerImpl(AccessTokenRepository accessTokenRepository, AccountRequestRepository accountRequestRepository,
                                     AuthorizationCodeRepository authorizationCodeRepository,
                                     TPPClientRepository tppClientRepository, UserRepository userRepository,
                                     AdminRepository adminRepository, EncryptionManager encryptionManager) {
        this.adminRepository = adminRepository;
        this.accountRequestRepository = accountRequestRepository;
        this.authorizationCodeRepository = authorizationCodeRepository;
        this.tppClientRepository = tppClientRepository;
        this.userRepository = userRepository;
        this.accessTokenRepository = accessTokenRepository;
		this.encryptionManager = encryptionManager;
    }


    public JWTVerifier getJWTVerifier() {
        return encryptionManager.getJwtVerifier();
    }

    public EncryptionManager getEncryptionManager() {
        return encryptionManager;
    }

    public Boolean isRequestTokenValid(String token) {

        if (accessTokenRepository.contains(token)) {
	        if (accessTokenIsNotExpired(accessTokenRepository.get(token))) {
		        return true;
	        } else {
		        accessTokenRepository.delete(token);
	        }
        }
	    return false;
    }

	public Boolean isAccessTokenValid(String token) {
		if (accessTokenRepository.contains(token)) {
			AccessToken accessToken = accessTokenRepository.get(token);
			if (accessTokenIsNotExpired(accessToken)) {
				if (accessTokenHasAuthorizationGrant(accessToken)) {
					return true;
				}
			} else {
				accessTokenRepository.delete(token);
			}
		}
		return false;
	}

    public Boolean accessTokenIsNotExpired(AccessToken token) {
        return token.getExpirationDate().isAfter(LocalDateTime.now());
    }

    public Boolean accessTokenHasAuthorizationGrant(AccessToken token) {
    	return token.getGrant().equals(AccessToken.Grant.AUTHORIZATION_CODE);
    }

	public Boolean tokenHasCorrectScope(AccessToken token, AccessToken.Scope requiredScope) {
		return token.getScopes().contains(requiredScope);
	}

	public Boolean checkClientCredentials(String clientId, String clientSecret) {
        TPPClient client = (TPPClient) tppClientRepository.get(clientId);
        return !(client == null) && client.getCredentials().getSecret().equals(clientSecret);
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

    public Boolean checkUserCredentials(String username, String userSecret) {
	    User user = userRepository.findByUserName(username);
	    if (user != null) {
            System.out.println("Checking hashed secret for " + user.getUserName());
            String saltedSecret = userSecret + user.getSalt();
            byte[] hashedSecret = encryptionManager.SHA256(saltedSecret);
	    	return Arrays.equals(hashedSecret, user.getHashedSecret());
	    } else {
	    	return false;
	    }
    }

    public Boolean checkAuthorizationCode(String authorizationCode) {
    	if (authorizationCodeRepository.contains(authorizationCode)) {
    		authorizationCodeRepository.delete(authorizationCode);
    		return true;
	    } else {
    		return false;
	    }
    }

    public AccountRequest getAccountRequest(String accountRequestId) {
    	return accountRequestRepository.get(accountRequestId);
    }

    @Override
    public TPPClient getTPPClient(String clientId) {
        return tppClientRepository.get(clientId);
    }
}
