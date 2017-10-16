package atmbranchfinderspring.resourceserver.authentication;

import atmbranchfinderspring.resourceserver.authentication.accesstokenvalidation.*;
import atmbranchfinderspring.resourceserver.models.*;
import atmbranchfinderspring.resourceserver.repos.*;
import com.auth0.jwt.JWTVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Default implementation of the AuthenticationManager interface
 */

@Component
public class AuthenticationManagerImpl implements AuthenticationManager {


    private AccessTokenRepository accessTokenRepository;
    private AccessTokenValidator accessTokenValidator;
    private AccountRequestRepository accountRequestRepository;
    private AuthorizationCodeRepository authorizationCodeRepository;
    private TPPClientRepository tppClientRepository;
    private UserRepository userRepository;
    private AdminRepository adminRepository;
    private EncryptionManager encryptionManager;

    @Autowired
    public AuthenticationManagerImpl(AccessTokenRepository accessTokenRepository, AccessTokenValidator accessTokenValidator,
                                     AccountRequestRepository accountRequestRepository,
                                     AuthorizationCodeRepository authorizationCodeRepository,
                                     TPPClientRepository tppClientRepository, UserRepository userRepository,
                                     AdminRepository adminRepository, EncryptionManager encryptionManager) {
        this.adminRepository = adminRepository;
        this.accountRequestRepository = accountRequestRepository;
        this.accessTokenValidator = accessTokenValidator;
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

    public boolean isRequestTokenValid(String token) {
	    List<TokenValidator> validatorList = Arrays.asList(new TokenIsNotExpired(), new GrantValidator(AccessToken.Grant.CLIENT_CREDENTIALS));
	    return accessTokenValidator.accessTokenIsValid(token, validatorList);
    }

	public boolean isAccessTokenValid(String token) {
		List<TokenValidator> validatorList = Arrays.asList(new TokenIsNotExpired(), new GrantValidator(AccessToken.Grant.AUTHORIZATION_CODE), new ScopeValidator(AccessToken.Scope.ACCOUNTS));
		return accessTokenValidator.accessTokenIsValid(token, validatorList);
	}


	public boolean checkClientCredentials(String clientId, String clientSecret) {
        TPPClient client = (TPPClient) tppClientRepository.get(clientId);
        return !(client == null) && client.getCredentials().getSecret().equals(clientSecret);
    }

    public boolean checkAdminCredentials(String adminId, String adminSecret) {
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

    public boolean checkUserCredentials(String username, String userSecret) {
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

    public boolean checkAuthorizationCode(String authorizationCode) {
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
