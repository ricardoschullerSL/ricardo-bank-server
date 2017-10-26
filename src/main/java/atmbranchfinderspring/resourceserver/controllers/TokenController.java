package atmbranchfinderspring.resourceserver.controllers;

import atmbranchfinderspring.resourceserver.annotations.TPPBasicAuthenticated;
import atmbranchfinderspring.resourceserver.authentication.AuthenticationManager;
import atmbranchfinderspring.resourceserver.models.AccessToken;
import atmbranchfinderspring.resourceserver.repos.AccessTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Properties;


@RestController
@RequestMapping("/token")
public class TokenController {

    private AccessTokenRepository accessTokenRepository;
    private AuthenticationManager authenticationManager;
    private static long expirationTime = 3600L;
    private ObjectMapper mapper;


    @Autowired
    public TokenController(AccessTokenRepository accessTokenRepository, AuthenticationManager authenticationManager, long expirationTime) {
        this.accessTokenRepository = accessTokenRepository;
        this.authenticationManager = authenticationManager;
        this.mapper = new ObjectMapper();
        TokenController.setExpirationTime(expirationTime);
    }

	@CrossOrigin(origins = "http://localhost:8081")
    @RequestMapping(method = RequestMethod.POST, value = "/access-token", produces = "application/json")
    @TPPBasicAuthenticated
    public void getAccessTokenClientCredentialGrant(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String clientId = getClientIdFromAuthorizationHeader(request);
    	AccessToken token = new AccessToken(clientId, "Bearer", expirationTime, AccessToken.Grant.CLIENT_CREDENTIALS, new ArrayList<AccessToken.Scope>());
	    accessTokenRepository.add(token);
	    response.setStatus(201);
	    response.setHeader("Content-type","application/json");
	    mapper.writer().writeValue(response.getWriter(), token);
    }

	@CrossOrigin(origins = "http://localhost:8081")
	@RequestMapping(method = RequestMethod.POST, value = "/access-token/{authorizationCode}", produces = "application/json")
	@TPPBasicAuthenticated
	public void getAccessTokenAuthorizationCodeGrant(HttpServletRequest request, HttpServletResponse response, @PathVariable String authorizationCode) throws IOException {
		String clientId = getClientIdFromAuthorizationHeader(request);
		if (authenticationManager.isAuthorizationCodeValid(authorizationCode)) {
			AccessToken token = new AccessToken(clientId, "Bearer", expirationTime, AccessToken.Grant.AUTHORIZATION_CODE, new ArrayList<AccessToken.Scope>());
			accessTokenRepository.add(token);
			response.setStatus(201);
			response.setHeader("Content-type","application/json");
			mapper.writer().writeValue(response.getWriter(), token);
		} else {
			response.sendError(403);
		}

	}

	private String getClientIdFromAuthorizationHeader(HttpServletRequest request) {
		String credentials = request.getHeader("Authorization").substring("Basic".length()).trim();
		return new String(Base64.getDecoder().decode(credentials)).split(":")[0];
	}

	public static long getExpirationTime() {
		return expirationTime;
	}

	public static void setExpirationTime(long expirationTime) {
		TokenController.expirationTime = expirationTime;
	}
}
