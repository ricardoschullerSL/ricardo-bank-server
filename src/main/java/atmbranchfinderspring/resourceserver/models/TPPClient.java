package atmbranchfinderspring.resourceserver.models;

import atmbranchfinderspring.resourceserver.validation.accountrequests.AccountRequest;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Collection;
import java.util.HashMap;


public class TPPClient implements Client{

    private Credentials credentials;
    private String redirectUri;
    private DecodedJWT jwt;
    private HashMap<String, AccountRequest> accountRequestResponses;

    public TPPClient(Credentials credentials, String redirectUri, DecodedJWT jwt) {
        this.credentials = credentials;
        this.redirectUri = redirectUri;
        this.jwt = jwt;
        this.accountRequestResponses = new HashMap<>();
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public DecodedJWT getJwt() {
        return jwt;
    }

    public void addAccountRequestResponse(AccountRequest accountRequest) {
    	accountRequestResponses.put(accountRequest.getAccountRequestId(), accountRequest);
    }

    public Collection<String> getAllAccountRequestIds() {
    	return accountRequestResponses.keySet();
    }


    public static class TPPClientBuilder {

        private Credentials credentials;
        private String redirectUri;
        private DecodedJWT jwt1;

        public TPPClientBuilder() {

        }


        public TPPClientBuilder setClientCredentials(Credentials credentials) {
            this.credentials = credentials;
            return this;
        }

        public TPPClientBuilder setRedirectUri(String uri) {
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
