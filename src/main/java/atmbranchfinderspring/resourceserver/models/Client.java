package atmbranchfinderspring.resourceserver.models;

import com.auth0.jwt.interfaces.DecodedJWT;

public interface Client {
	Credentials getCredentials();
	String getRedirectUri();
	DecodedJWT getJwt();
}
