package atmbranchfinderspring.resourceserver.controllers;

import atmbranchfinderspring.resourceserver.authentication.AuthManager;
import atmbranchfinderspring.resourceserver.models.ClientCredentials;
import atmbranchfinderspring.resourceserver.models.TPPClient;
import atmbranchfinderspring.resourceserver.authentication.TPPManager;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
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

    @RequestMapping(value="/getjwt", method = RequestMethod.GET)
    public String getJWT(HttpServletRequest request, HttpServletResponse response) {
        try {
            String[] values;
            String authorization = request.getHeader("Authorization");
            if (authorization != null && authorization.startsWith("Basic")) {
                String base64Credentials = authorization.substring("Basic".length()).trim();
                String credentials = new String(Base64.getDecoder().decode(base64Credentials));
                values = credentials.split(":", 2);
                if (authManager.checkAdminCredentials(values[0], values[1])) {
	                System.out.println("Creating and returning JWT.");
	                String jwt = JWT.create().withIssuer("Open Banking")
	                    .withClaim("software_id","Scotty")
	                    .withClaim("aud","Scott Logic Bank")
	                    .withClaim("redirect_uri","http://localhost:8081/redirect")
	                    .withClaim("software_statement","testsoftwarestatement")
	                    .withJWTId("jwtId")
	                    .sign(authManager.getEncryptionManager().getPemManager().getAlgorithm());

	                response.setStatus(200);
	                return jwt;
                } else {
                	response.sendError(403, "Incorrect credentials.");
                }
            } else {
                response.sendError(400, "Bad Request");
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return null;

    }


    @RequestMapping(value="/register", produces = "application/jwt", method = RequestMethod.POST)
    public void registerTPPClient(@RequestBody String token, HttpServletResponse response) {
        try {

	        JWTVerifier verifier = authManager.getJWTVerifier();
            DecodedJWT jwt = verifier.verify(token);
            String clientId = jwt.getClaim("software_id").asString();
            URI redirectUri = new URI(jwt.getClaim("redirect_uri").asString());
            if (tppManager.isClientRegistered(clientId)) {
                System.out.println("Incoming request is for client that's already registered.");
                response.sendError(400, "Client already registered.");
            } else {
                ClientCredentials credentials = new ClientCredentials(clientId, UUID.randomUUID().toString());
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
        } catch (IOException | URISyntaxException e )  {
            //Response error handler.
            System.out.println(e);
        }
    }
}
