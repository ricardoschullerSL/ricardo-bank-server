package atmbranchfinderspring.resourceserver.authentication;

import atmbranchfinderspring.resourceserver.models.AccountRequestResponse;
import atmbranchfinderspring.resourceserver.models.TPPClient;
import com.auth0.jwt.JWTVerifier;

public interface AuthenticationManager {

	JWTVerifier getJWTVerifier();
	Boolean isAccessTokenValid(String token);
	Boolean checkClientCredentials(String clientId, String clientSecret);
	Boolean checkAdminCredentials(String adminId, String adminSecret);
	Boolean checkUserCredentials(String userId, String userSecret);
	Boolean checkAuthorizationCode(String authorizationCode);
	AccountRequestResponse getAccountRequest(String accountRequestId);
	TPPClient getTPPClient(String clientId);
}

