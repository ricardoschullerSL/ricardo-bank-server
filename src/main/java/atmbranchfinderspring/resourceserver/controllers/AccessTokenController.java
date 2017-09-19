package atmbranchfinderspring.resourceserver.controllers;

import atmbranchfinderspring.resourceserver.authentication.AuthManager;
import atmbranchfinderspring.resourceserver.models.AccessToken;
import atmbranchfinderspring.resourceserver.repos.AccessTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

/**
 * @author Ricardo Schuller
 * @version 0.1.0
 * @since 0.1.0
 */

@RestController
public class AccessTokenController {

    private AccessTokenRepository accessTokenRepository;
    private AuthManager authManager;
    private static long expirationTime = 24*60*60;

    @Autowired
    public AccessTokenController(AccessTokenRepository accessTokenRepository, AuthManager authManager) {
        this.accessTokenRepository = accessTokenRepository;
        this.authManager = authManager;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/access-token")
    public AccessToken getAccessToken(HttpServletRequest request, HttpServletResponse response) {
        String[] values;
        String authorization = request.getHeader("Authorization");
        try {
	        if (authorization != null && authorization.startsWith("Basic")) {
		        String base64Credentials = authorization.substring("Basic".length()).trim();
		        String credentials = new String(Base64.getDecoder().decode(base64Credentials));
		        values = credentials.split(":", 2);
		        System.out.println(values[0] + " " + values[1]);
		        if (authManager.checkClientCredentials(values[0], values[1])) {
			        AccessToken token = new AccessToken("Bearer", expirationTime);
			        accessTokenRepository.add(token);
			        return token;
		        } else {
			        response.sendError(403, "Incorrect Client Credentials.");
		        }
	        } else {
		        response.sendError(400, "No Authorization headers.");
		        return null;
	        }
        } catch (IOException e) {
        	System.out.println(e);
        }
        return null;
    }

	public static long getExpirationTime() {
		return expirationTime;
	}

	public static void setExpirationTime(long expirationTime) {
		AccessTokenController.expirationTime = expirationTime;
	}
}
