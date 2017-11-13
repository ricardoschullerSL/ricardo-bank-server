package atmbranchfinderspring.resourceserver.controllers;

import atmbranchfinderspring.resourceserver.authentication.TPPManager;
import atmbranchfinderspring.resourceserver.models.Credentials;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * TPPController exposes an endpoint where Trusted Third Party clients can register with their signed JWTs.
 * This will return the client-credentials that can be used for the OAuth2 protocol.
 */

@RestController
@RequestMapping("/tpp")
public class TPPController {


    private TPPManager tppManager;
    private ObjectMapper mapper;



    @Autowired
    public TPPController(TPPManager tppManager) {
        this.mapper = new ObjectMapper();
        this.tppManager = tppManager;
    }



	@CrossOrigin(origins = "http://localhost:8081")
    @PostMapping(value="/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public Credentials registerTPPClient(@RequestBody String clientJwt, HttpServletResponse response) throws IOException {
        Credentials credentials = tppManager.registerTPPClientAndReturnCredentials(clientJwt);
        if (credentials == null) {
	        response.sendError(400, "Bad JWT");
	        return null;
        } else {
	        response.setStatus(201);
	        return credentials;
        }
    }
}
