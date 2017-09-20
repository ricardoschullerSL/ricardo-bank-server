package atmbranchfinderspring.resourceserver.controllers;

import atmbranchfinderspring.resourceserver.annotations.BasicAuthenticated;
import atmbranchfinderspring.resourceserver.authentication.AuthenticationManagerImpl;
import atmbranchfinderspring.resourceserver.models.AccessToken;
import atmbranchfinderspring.resourceserver.repos.AccessTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
public class TokenController {

    private AccessTokenRepository accessTokenRepository;
    private AuthenticationManagerImpl authenticationManagerImpl;
    private static long expirationTime = 60*60;
    private ObjectMapper mapper;

    @Autowired
    public TokenController(AccessTokenRepository accessTokenRepository, AuthenticationManagerImpl authenticationManagerImpl) {
        this.accessTokenRepository = accessTokenRepository;
        this.authenticationManagerImpl = authenticationManagerImpl;
        mapper = new ObjectMapper();
    }

	@BasicAuthenticated
    @RequestMapping(method = RequestMethod.POST, value = "/access-token", produces = "application/json")
    public void getAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	AccessToken token = new AccessToken("Bearer", expirationTime);
	    accessTokenRepository.add(token);
	    response.setStatus(201);
	    response.setHeader("Content-type","application/json");
	    mapper.writer().writeValue(response.getWriter(), token);
    }

	public static long getExpirationTime() {
		return expirationTime;
	}

	public static void setExpirationTime(long expirationTime) {
		TokenController.expirationTime = expirationTime;
	}
}
