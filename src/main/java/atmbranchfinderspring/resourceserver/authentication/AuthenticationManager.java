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
	boolean checkClientCredentials(String clientId, String clientSecret);
	boolean checkAdminCredentials(String adminId, String adminSecret);
	boolean checkUserCredentials(String userId, String userSecret);
	boolean checkAuthorizationCode(String authorizationCode);
	AccountRequest getAccountRequest(String accountRequestId);
	TPPClient getTPPClient(String clientId);
}

