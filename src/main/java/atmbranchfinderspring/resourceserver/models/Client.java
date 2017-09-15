package atmbranchfinderspring.resourceserver.models;

import com.auth0.jwt.interfaces.DecodedJWT;

import java.net.URI;

public interface Client {
	ClientCredentials getCredentials();
	URI getRedirectUri();
	DecodedJWT getJwt();
}
