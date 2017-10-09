package atmbranchfinderspring.resourceserver.controllers;

import atmbranchfinderspring.resourceserver.authentication.AuthenticationManagerImpl;
import atmbranchfinderspring.resourceserver.models.Credentials;
import atmbranchfinderspring.resourceserver.models.TPPClient;
import atmbranchfinderspring.resourceserver.authentication.TPPManager;
import com.auth0.jwt.JWTVerifier;
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
 * TPPController exposes an endpoint where Trusted Third Party clients can register with their signed JWTs.
 * This will return the client-credentials that can be used for the OAuth2 protocol.
 */

@RestController
@RequestMapping("/tpp")
public class TPPController {

    private AuthenticationManagerImpl authenticationManagerImpl;
    private TPPManager tppManager;
    private ObjectMapper mapper;


    @Autowired
    public TPPController(TPPManager tppManager, AuthenticationManagerImpl authenticationManagerImpl) {
        this.mapper = new ObjectMapper();
        this.tppManager = tppManager;
        this.authenticationManagerImpl = authenticationManagerImpl;
    }




    @RequestMapping(value="/register", produces = "application/jwt", method = RequestMethod.POST)
    public void registerTPPClient(@RequestBody String token, HttpServletResponse response) {
        try {

	        JWTVerifier verifier = authenticationManagerImpl.getJWTVerifier();
            DecodedJWT jwt = verifier.verify(token);
            String clientId = jwt.getClaim("software_id").asString();
            String redirectUri = jwt.getClaim("redirect_uri").asString();
            if (tppManager.isClientRegistered(clientId)) {
                System.out.println("Incoming request is for client that's already registered.");
                response.sendError(400, "Client already registered.");
            } else {
                Credentials credentials = new Credentials(clientId, UUID.randomUUID().toString());
                TPPClient client = new TPPClient(credentials, redirectUri, jwt);
                tppManager.registerClient(client);
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
        } catch (IOException e )  {
            //Response error handler.
            System.out.println(e);
        }
    }
}
