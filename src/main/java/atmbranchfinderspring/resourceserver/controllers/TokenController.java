package atmbranchfinderspring.resourceserver.controllers;

import atmbranchfinderspring.resourceserver.annotations.TPPBasicAuthenticated;
import atmbranchfinderspring.resourceserver.authentication.AuthenticationManager;
import atmbranchfinderspring.resourceserver.repos.AccessTokenRepository;
import atmbranchfinderspring.resourceserver.validation.accesstokens.AccessToken;
import atmbranchfinderspring.resourceserver.validation.accesstokens.AccessTokenBuilder;
import atmbranchfinderspring.resourceserver.validation.accountrequests.AccountRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;


@RestController
@RequestMapping("/token")
public class TokenController {

    private AccessTokenRepository accessTokenRepository;
    private AuthenticationManager authenticationManager;
    private static long expirationTime = 3600L;
    private ObjectMapper mapper;


    @Autowired
    public TokenController(AccessTokenRepository accessTokenRepository, AuthenticationManager authenticationManager, Environment env) {
        this.accessTokenRepository = accessTokenRepository;
        this.authenticationManager = authenticationManager;
        this.mapper = new ObjectMapper();
        expirationTime = Long.parseLong(env.getProperty("accesstoken.expirationtime"));
    }

	@CrossOrigin(origins = "http://localhost:8081")
    @RequestMapping(method = RequestMethod.POST, value = "/access-token", produces = MediaType.APPLICATION_JSON_VALUE)
    @TPPBasicAuthenticated
    public AccessToken getAccessTokenClientCredentialGrant(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String clientId = getClientIdFromAuthorizationHeader(request);
    	AccessToken token = new AccessTokenBuilder()
			    .setClientId(clientId)
			    .setTokenType(AccessToken.TokenType.REQUEST)
			    .setIssueDate(LocalDateTime.now())
			    .setExpirationDate(LocalDateTime.now().plusSeconds(expirationTime))
			    .setGrant(AccessToken.Grant.CLIENT_CREDENTIALS)
			    .setAccountRequestId(null)
			    .build();
	    accessTokenRepository.add(token);
	    response.setStatus(201);
	    return token;
    }

	@CrossOrigin(origins = "http://localhost:8081")
	@RequestMapping(method = RequestMethod.POST, value = "/access-token/{authorizationCode}", produces = MediaType.APPLICATION_JSON_VALUE)
	@TPPBasicAuthenticated
	public void getAccessTokenAuthorizationCodeGrant(HttpServletRequest request, HttpServletResponse response, @PathVariable String authorizationCode) throws IOException {
		String clientId = getClientIdFromAuthorizationHeader(request);
		if (authenticationManager.isAuthorizationCodeValid(authorizationCode)) {
			AccountRequest accountRequest = authenticationManager.getAccountRequestFromAuthorizationCode(authorizationCode);
			String accountRequestId = accountRequest.getAccountRequestId();
			AccessToken token = new AccessToken(clientId, AccessToken.TokenType.REFRESH, expirationTime, AccessToken.Grant.AUTHORIZATION_CODE, accountRequestId );
			accessTokenRepository.add(token);
			response.setStatus(201);
			response.setHeader("Content-type","application/json");
			mapper.writer().writeValue(response.getWriter(), token); //Important this happens last, writeValue flushes the message.
		} else {
			response.sendError(403);
		}
	}

	private String getClientIdFromAuthorizationHeader(HttpServletRequest request) {
		String credentials = request.getHeader("Authorization").substring("Basic".length()).trim();
		return new String(Base64.getDecoder().decode(credentials)).split(":")[0];
	}

	@RequestMapping(method = RequestMethod.POST, value = "/test")
	public AccessToken getTestToken() {
    	return new AccessTokenBuilder().setClientId("test").build();
	}

	public static long getExpirationTime() {
		return expirationTime;
	}

	public static void setExpirationTime(long expirationTime) {
		TokenController.expirationTime = expirationTime;
	}
}
