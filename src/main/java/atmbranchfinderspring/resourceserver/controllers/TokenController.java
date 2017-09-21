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
import java.util.Base64;


@RestController
public class TokenController {

    private AccessTokenRepository accessTokenRepository;
    private static long expirationTime = 60*60;
    private ObjectMapper mapper;

    @Autowired
    public TokenController(AccessTokenRepository accessTokenRepository) {
        this.accessTokenRepository = accessTokenRepository;
        mapper = new ObjectMapper();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/access-token", produces = "application/json")
    @BasicAuthenticated
    public void getAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	String credentials = request.getHeader("Authorization").substring("Basic".length()).trim();
    	String clientId = new String(Base64.getDecoder().decode(credentials)).split(":")[0];
    	AccessToken token = new AccessToken(clientId, "Bearer", expirationTime);
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
