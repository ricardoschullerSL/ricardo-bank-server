package atmbranchfinderspring.resourceserver.controllers;

import atmbranchfinderspring.resourceserver.annotations.TPPBasicAuthenticated;
import atmbranchfinderspring.resourceserver.authentication.AuthenticationManager;
import atmbranchfinderspring.resourceserver.repos.AccessTokenRepository;
import atmbranchfinderspring.resourceserver.validation.accesstokens.AccessToken;
import atmbranchfinderspring.resourceserver.validation.accesstokens.AccessTokenBuilder;
import atmbranchfinderspring.resourceserver.validation.accountrequests.AccountRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;


@RestController
public class TokenController extends OpenBankingBaseController {

    private AccessTokenRepository accessTokenRepository;
    private AuthenticationManager authenticationManager;
    private static long refreshTokenExpirationTime = 3600L;
    private static long accessTokenExpirationTime = 600L;


    @Autowired
    public TokenController(AccessTokenRepository accessTokenRepository, AuthenticationManager authenticationManager, Environment env) {
        this.accessTokenRepository = accessTokenRepository;
        this.authenticationManager = authenticationManager;
        accessTokenExpirationTime = Long.parseLong(env.getProperty("accesstoken.expirationtime"));
        refreshTokenExpirationTime = Long.parseLong(env.getProperty("refreshtoken.expirationtime"));
    }

	@CrossOrigin
    @RequestMapping(method = RequestMethod.POST, value = "/access-token", produces = MediaType.APPLICATION_JSON_VALUE)
    @TPPBasicAuthenticated
	@ResponseStatus(value = HttpStatus.CREATED)
    public AccessToken getAccessTokenClientCredentialGrant(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String clientId = getClientIdFromAuthorizationHeader(request);
    	AccessToken token = new AccessTokenBuilder()
			    .setClientId(clientId)
			    .setTokenType(AccessToken.TokenType.REQUEST)
			    .setIssueDate(LocalDateTime.now())
			    .setExpirationDate(LocalDateTime.now().plusSeconds(accessTokenExpirationTime))
			    .setGrant(AccessToken.Grant.CLIENT_CREDENTIALS)
			    .setAccountRequestId(null)
			    .build();
	    accessTokenRepository.add(token);
	    return token;
    }

	@CrossOrigin
	@RequestMapping(method = RequestMethod.POST, value = "/access-token/{authorizationCode}", produces = MediaType.APPLICATION_JSON_VALUE)
	@TPPBasicAuthenticated
	@ResponseStatus(value = HttpStatus.CREATED)
	public AccessToken getAccessTokenAuthorizationCodeGrant(HttpServletRequest request, HttpServletResponse response, @PathVariable String authorizationCode) throws IOException {
		if (authenticationManager.isAuthorizationCodeValid(authorizationCode)) {
			String clientId = getClientIdFromAuthorizationHeader(request);
			AccountRequest accountRequest = authenticationManager.getAccountRequestFromAuthorizationCode(authorizationCode);
			String accountRequestId = accountRequest.getAccountRequestId();
			AccessToken token = new AccessTokenBuilder()
					.setClientId(clientId)
					.setTokenType(AccessToken.TokenType.REFRESH)
					.setIssueDate(LocalDateTime.now())
					.setExpirationDate(LocalDateTime.now().plusSeconds(refreshTokenExpirationTime))
					.setGrant(AccessToken.Grant.AUTHORIZATION_CODE)
					.setAccountRequestId(accountRequestId)
					.build();
			accessTokenRepository.add(token);
			return token;
		} else {
			response.sendError(403);
			return null;
		}
	}

	@CrossOrigin
	@RequestMapping(method = RequestMethod.POST, value = "/access-token/refresh/{token}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@TPPBasicAuthenticated
	@ResponseStatus(value = HttpStatus.CREATED)
	public AccessToken getAccessTokenFromRefreshToken(HttpServletRequest request, HttpServletResponse response, @PathVariable String token) throws IOException {
    	String clientId = getClientIdFromAuthorizationHeader(request);
		AccessToken refreshToken = accessTokenRepository.get(token);
		if (refreshToken != null && refreshToken.getClientId().equals(clientId)) {
			AccessToken accessToken = new AccessTokenBuilder()
					.setClientId(clientId)
					.setTokenType(AccessToken.TokenType.ACCESS)
					.setIssueDate(LocalDateTime.now())
					.setGrant(AccessToken.Grant.AUTHORIZATION_CODE)
					.setExpirationDate(LocalDateTime.now().plusSeconds(accessTokenExpirationTime))
					.setAccountRequestId(refreshToken.getAccountRequestId())
					.build();
			accessTokenRepository.add(accessToken);
			return accessToken;
		} else {
			response.sendError(403);
			return null;
		}
	}

	private String getClientIdFromAuthorizationHeader(HttpServletRequest request) {
		String credentials = request.getHeader("Authorization").substring("Basic".length()).trim();
		return new String(Base64.getDecoder().decode(credentials)).split(":")[0];
	}


}
