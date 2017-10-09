package atmbranchfinderspring.resourceserver.authentication;

import atmbranchfinderspring.resourceserver.models.AccountRequest;
import atmbranchfinderspring.resourceserver.models.TPPClient;
import com.auth0.jwt.JWTVerifier;

public interface AuthenticationManager {

	JWTVerifier getJWTVerifier();
	Boolean isRequestTokenValid(String token);
	Boolean checkClientCredentials(String clientId, String clientSecret);
	Boolean checkAdminCredentials(String adminId, String adminSecret);
	Boolean checkUserCredentials(String userId, String userSecret);
	Boolean checkAuthorizationCode(String authorizationCode);
	AccountRequest getAccountRequest(String accountRequestId);
	TPPClient getTPPClient(String clientId);
}

