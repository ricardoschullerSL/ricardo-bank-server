package atmbranchfinderspring.resourceserver.authentication;

import com.auth0.jwt.interfaces.DecodedJWT;

import java.net.URI;

/**
 * @author Ricardo Schuller
 * @version 0.1.0
 * @since 0.1.0
 */

public class TPPClient {
    private ClientCredentials credentials;
    private URI redirectUri;
    private DecodedJWT jwt;

    public TPPClient(ClientCredentials credentials, URI redirectUri, DecodedJWT jwt) {
        this.credentials = credentials;
        this.redirectUri = redirectUri;
        this.jwt = jwt;
    }

    public ClientCredentials getCredentials() {
        return credentials;
    }

    public URI getRedirectUri() {
        return redirectUri;
    }

    public DecodedJWT getJwt() {
        return jwt;
    }

    public static class TPPClientBuilder {

        private ClientCredentials credentials;
        private URI redirectUri;
        private DecodedJWT jwt1;

        public TPPClientBuilder() {

        }


        public TPPClientBuilder setClientCredentials(ClientCredentials credentials) {
            this.credentials = credentials;
            return this;
        }

        public TPPClientBuilder setRedirectUri(URI uri) {
            this.redirectUri = uri;
            return this;
        }

        public TPPClientBuilder setSSA(DecodedJWT jwt) {
            this.jwt1 = jwt;
            return this;
        }


        public TPPClient build() {
            return new TPPClient(credentials, redirectUri, jwt1);
        }

    }
}
