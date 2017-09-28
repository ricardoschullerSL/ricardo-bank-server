package atmbranchfinderspring.resourceserver.authentication;

import com.auth0.jwt.JWTVerifier;

public interface AuthenticationManager {

	JWTVerifier getJWTVerifier();
	Boolean isAccessTokenValid(String token);
	Boolean checkClientCredentials(String clientId, String clientSecret);
	Boolean checkAdminCredentials(String adminId, String adminSecret);
	Boolean checkUserCredentials(String userId, String userSecret);
}

