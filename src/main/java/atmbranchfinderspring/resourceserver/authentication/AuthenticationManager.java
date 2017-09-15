package atmbranchfinderspring.resourceserver.authentication;

import com.auth0.jwt.JWTVerifier;

public interface AuthenticationManager {

	JWTVerifier getJWTVerifier();
	Boolean isAccessTokenValid(String token);
	Boolean areCredentialsCorrect(String clientId, String clientSecret);

}

