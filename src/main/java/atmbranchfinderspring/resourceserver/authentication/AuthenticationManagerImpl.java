package atmbranchfinderspring.resourceserver.authentication;

import atmbranchfinderspring.resourceserver.models.Admin;
import atmbranchfinderspring.resourceserver.models.TPPClient;
import atmbranchfinderspring.resourceserver.models.User;
import atmbranchfinderspring.resourceserver.repos.*;
import atmbranchfinderspring.resourceserver.validation.accesstokens.*;
import atmbranchfinderspring.resourceserver.validation.accountrequests.AccountRequest;
import atmbranchfinderspring.resourceserver.validation.accountrequests.Permission;
import com.auth0.jwt.JWTVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
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
    private ExpiredTokenCollector expiredTokenCollector;
    private TPPClientRepository tppClientRepository;
    private UserRepository userRepository;
    private AdminRepository adminRepository;
    private EncryptionManager encryptionManager;

    @Autowired
    public AuthenticationManagerImpl(AccessTokenRepository accessTokenRepository, AccessTokenValidator accessTokenValidator,
                                     AccountRequestRepository accountRequestRepository, AuthorizationCodeRepository authorizationCodeRepository,
                                     ExpiredTokenCollector expiredTokenCollector,
                                     TPPClientRepository tppClientRepository, UserRepository userRepository,
                                     AdminRepository adminRepository, EncryptionManager encryptionManager) {
        this.adminRepository = adminRepository;
        this.accountRequestRepository = accountRequestRepository;
        this.accessTokenValidator = accessTokenValidator;
        this.authorizationCodeRepository = authorizationCodeRepository;
        this.expiredTokenCollector = expiredTokenCollector;
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
	    try {
		    AccessToken accessToken = accessTokenRepository.get(token);
		    List<TokenValidator> validatorList = Arrays.asList(
		    		new TokenIsNotExpired(),
				    new GrantValidator(AccessToken.Grant.CLIENT_CREDENTIALS),
				    new TokenTypeValidator(AccessToken.TokenType.REQUEST)
		    );
		    return accessTokenValidator.accessTokenIsValid(accessToken, validatorList);
	    } catch (NullPointerException e) {
		    return false;
	    }
    }

    @Deprecated
	public boolean isAccessTokenValid(String token, Set<Permission> requiredPermissions) {
		try {
			AccessToken accessToken = accessTokenRepository.get(token);
			AccountRequest accountRequest = accountRequestRepository.get(accessToken.getAccountRequestId());
			List<TokenValidator> validatorList = Arrays.asList(new TokenIsNotExpired(), new GrantValidator(AccessToken.Grant.AUTHORIZATION_CODE), new PermissionValidator(requiredPermissions, accountRequest.getPermissions()));
			return accessTokenValidator.accessTokenIsValid(accessToken, validatorList);
		} catch (NullPointerException e) {
			return false;
		}
	}

	public boolean isAccessTokenValid(HttpServletRequest request, String token, Set<Permission> requiredPermissions,
	                                  AccessToken.TokenType requiredTokenType) {
		try {
			switch (requiredTokenType) {
				case ACCESS: return accessTokenIsValid(request, token, requiredPermissions, requiredTokenType);
				case REQUEST: return isRequestTokenValid(token);
				case REFRESH: return refreshTokenIsValid(request, token);
				default: return false;
			}
		} catch (NullPointerException e) {
			return false;
		}
	}

	private boolean accessTokenIsValid(HttpServletRequest request, String token, Set<Permission> requiredPermissions,
	                                  AccessToken.TokenType requiredTokenType) {

		String[] urlArray = request.getRequestURI().split("/");
		int accountId = Integer.parseInt(urlArray[2]);
		AccessToken accessToken = accessTokenRepository.get(token);
		AccountRequest accountRequest = accountRequestRepository.get(accessToken.getAccountRequestId());
		List<TokenValidator> validatorList = Arrays.asList(new TokenIsNotExpired(),
				new PermissionValidator(requiredPermissions, accountRequest.getPermissions()),
				new AccountRequestAuthorizationValidator(accountRequest, accountId),
				new TokenTypeValidator(requiredTokenType));
		return accessTokenValidator.accessTokenIsValid(accessToken, validatorList);
	}

	private boolean refreshTokenIsValid(HttpServletRequest request, String token) {
    	AccessToken accessToken = accessTokenRepository.get(token);
    	List<TokenValidator> validatorList = Arrays.asList(new TokenIsNotExpired(),
			    new TokenTypeValidator(AccessToken.TokenType.REFRESH));
    	return accessTokenValidator.accessTokenIsValid(accessToken, validatorList);
	}

	public boolean areClientCredentialsValid(String clientId, String clientSecret) {
        TPPClient client = (TPPClient) tppClientRepository.get(clientId);
        return !(client == null) && client.getCredentials().getSecret().equals(clientSecret);
    }

    public boolean areAdminCredentialsValid(String adminId, String adminSecret) {
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

    public boolean areUserCredentialsValid(String username, String userSecret) {
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

    public boolean isAccountRequestIdValid(String accountRequestId) {
    	return accountRequestRepository.contains(accountRequestId);
    }

    public boolean isAuthorizationCodeValid(String authorizationCode) {
    	return authorizationCodeRepository.contains(authorizationCode);
    }

    public AccountRequest getAccountRequest(String accountRequestId) {
    	return accountRequestRepository.get(accountRequestId);
    }

    public AccountRequest getAccountRequestFromAuthorizationCode(String authorizationCode) {
    	if (authorizationCodeRepository.contains(authorizationCode)) {
    		String accountRequestId = authorizationCodeRepository.get(authorizationCode);
    		authorizationCodeRepository.delete(authorizationCode);
    		return accountRequestRepository.get(accountRequestId);
	    } else {
    		return null;
	    }
    }

	@Override
    public TPPClient getTPPClient(String clientId) {
        return tppClientRepository.get(clientId);
    }



}
