package atmbranchfinderspring.resourceserver.authentication;

import atmbranchfinderspring.resourceserver.models.AccessToken;
import atmbranchfinderspring.resourceserver.models.AccountRequest;
import atmbranchfinderspring.resourceserver.models.TPPClient;
import com.auth0.jwt.JWTVerifier;

/**
 * AuthenticationManager class is the central object in the security of the OAuth2 protocol and Basic Authentication.
 * Each secured controller has a reference to the singleton, which has access to different repositories to cross-check
 * if incoming requests are valid.
 */

public interface AuthenticationManager {

	JWTVerifier getJWTVerifier();
	boolean isRequestTokenValid(String token);
	boolean isAccessTokenValid(String token);
	boolean areClientCredentialsValid(String clientId, String clientSecret);
	boolean areAdminCredentialsValid(String adminId, String adminSecret);
	boolean areUserCredentialsValid(String userId, String userSecret);
	boolean isAuthorizationCodeValid(String authorizationCode);
	boolean isAccountRequestIdValid(String accountRequestId);
	AccountRequest getAccountRequest(String accountRequestId);
	TPPClient getTPPClient(String clientId);
	EncryptionManager getEncryptionManager();
	void addAdmin(String id, String secret);
}

