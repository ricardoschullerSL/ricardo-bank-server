package atmbranchfinderspring.resourceserver.authentication;

import atmbranchfinderspring.resourceserver.models.TPPClient;
import atmbranchfinderspring.resourceserver.validation.accountrequests.AccountRequest;
import atmbranchfinderspring.resourceserver.validation.accountrequests.Permission;
import com.auth0.jwt.JWTVerifier;

import java.util.Set;

/**
 * AuthenticationManager class is the central object in the security of the OAuth2 protocol and Basic Authentication.
 * Each secured controller has a reference to the singleton, which has access to different repositories to cross-check
 * if incoming requests are valid.
 */

public interface AuthenticationManager {
	/**
	 * Returns JWT verifier from Auth0 package. TPPs send in JWTs which are signed with ES256, the verifier makes sure
	 * the JWT is signed properly.
	 * @return  Auth0 JWTVerifier
	 * @see JWTVerifier
	 */
	JWTVerifier getJWTVerifier();

	/**
	 * Checks if incoming access token exists, has the right 'grant' and is not expired. RequestTokens are
	 * just AccessTokens that are only used to POST an account-request. These are not valid for resource retrieval.
	 * @param token ID string
	 * @return Boolean
	 * @see atmbranchfinderspring.resourceserver.validation.accesstokens.AccessToken
	 */
	boolean isRequestTokenValid(String token);

	/**
	 * Checks if in coming access token exists in the repository, has the right 'grant', permissions, and is not expired.
	 * These tokens can be used to access protected resources.
	 * @param token ID string
	 * @param requiredPermission Permission needed for endpoint data.
	 * @return Boolean
	 */
	boolean isAccessTokenValid(String token, Set<Permission> requiredPermission);

	/**
	 * Checks if incoming TPP client credentials are currently registered in the system.
	 * @param clientId client ID
	 * @param clientSecret client secret
	 * @return Boolean
	 */
	boolean areClientCredentialsValid(String clientId, String clientSecret);

	/**
	 * Checks if incoming admin credentials are currently registered in the system.
	 * @param adminId admin ID
	 * @param adminSecret client secret
	 * @return Boolean
	 */
	boolean areAdminCredentialsValid(String adminId, String adminSecret);

	/**
	 * Checks if incoming user credentials are currently registered in the system. The user secret
	 * is only saved hashed with a salt.
	 * @param userId user ID
	 * @param userSecret user secret
	 * @return Boolean
	 */
	boolean areUserCredentialsValid(String userId, String userSecret);

	/**
	 * Checks if the authorization code exists and returns a boolean.
	 * @param authorizationCode unique string that can be exchanged for an access token.
	 * @return Boolean
	 */
	boolean isAuthorizationCodeValid(String authorizationCode);

	/**
	 * Checks if accountRequestId exists and returns a boolean.
	 * @param accountRequestId ID associated with an AccountRequest
	 * @return Boolean
	 */
	boolean isAccountRequestIdValid(String accountRequestId);

	/**
	 * Returns the AccountRequest object associated with the accountRequestId.
	 * @param accountRequestId ID associated with an AccountRequest
	 * @return AccountRequest
	 * @see AccountRequest
	 */
	AccountRequest getAccountRequest(String accountRequestId);

	/**
	 * Exchange authorization code for AccountRequest.
	 * @param authorizationCode
	 * @return
	 */
	AccountRequest getAccountRequestFromAuthorizationCode(String authorizationCode);

	/**
	 * Gets the Third Party Provider client associated with clientId
	 * @param clientId Third Party Provider client ID
	 * @return TPPClient
	 * @see TPPClient
	 */
	TPPClient getTPPClient(String clientId);


	EncryptionManager getEncryptionManager();
	void addAdmin(String id, String secret);
}

