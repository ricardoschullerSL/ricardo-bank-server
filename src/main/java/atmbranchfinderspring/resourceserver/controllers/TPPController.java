package atmbranchfinderspring.resourceserver.controllers;

import atmbranchfinderspring.resourceserver.authentication.AuthManager;
import atmbranchfinderspring.resourceserver.authentication.ClientCredentials;
import atmbranchfinderspring.resourceserver.authentication.TPPClient;
import atmbranchfinderspring.resourceserver.authentication.TPPManager;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

/**
 * @author Ricardo Schuller
 * @version 0.1.0
 * @since 0.1.0
 */

@RestController
public class TPPController {

    private AuthManager authManager;
    private TPPManager tppManager;
    private ObjectMapper mapper;


    @Autowired
    public TPPController(TPPManager tppManager, AuthManager authManager) {
        this.mapper = new ObjectMapper();
        this.tppManager = tppManager;
        this.authManager = authManager;
    }


    @RequestMapping(value="/register", produces = "application/jwt", method = RequestMethod.POST)
    public void registerTPPClient(@RequestBody String token, HttpServletResponse response) {
        try {
            DecodedJWT jwt = authManager.getVerifier().verify(token);
            String clientId = jwt.getClaim("software_id").asString();
            URI redirectUri = new URI(jwt.getClaim("redirect_uri").asString());
            if (tppManager.isTPPClientRegistered(clientId)) {
                response.sendError(400, "Client already registered.");

            } else {
                ClientCredentials credentials = new ClientCredentials(clientId, UUID.randomUUID().toString());
                TPPClient client = new TPPClient(credentials, redirectUri, jwt);
                tppManager.registerTPPClient(client);
                String responseString = mapper.writeValueAsString(credentials);
                response.getWriter().write(responseString);
            }

        } catch (JWTVerificationException e) {
            // Invalid token.
            System.out.println("Couldn't verify token from register request.");
            System.out.println(e);
            try {
                response.sendError(400, "Invalid token.");
            } catch (IOException ioe) {
                System.out.println(ioe);
            }
        } catch (IOException | URISyntaxException e )  {
            //Response error handler.
            System.out.println(e);
        }
    }
}
