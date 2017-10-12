package atmbranchfinderspring.resourceserver.controllers;

import atmbranchfinderspring.resourceserver.authentication.AuthenticationManagerImpl;
import atmbranchfinderspring.resourceserver.authentication.TPPManager;
import atmbranchfinderspring.resourceserver.repos.AdminRepository;
import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;

@RestController
public class AdminController {

	private AuthenticationManagerImpl authenticationManagerImpl;
	private TPPManager tppManager;
	private AdminRepository adminRepository;


	@Autowired
	public AdminController(TPPManager tppManager, AuthenticationManagerImpl authenticationManagerImpl, AdminRepository adminRepository) {
		this.tppManager = tppManager;
		this.authenticationManagerImpl = authenticationManagerImpl;
		this.adminRepository = adminRepository;
	}



	@RequestMapping(value="/getjwt", method = RequestMethod.POST)
	public String getJWT(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, String> body) {
		try {
			String[] values;
			String authorization = request.getHeader("Authorization");
			if (authorization != null && authorization.startsWith("Basic")) {
				String base64Credentials = authorization.substring("Basic".length()).trim();
				String credentials = new String(Base64.getDecoder().decode(base64Credentials));
				values = credentials.split(":", 2);
				System.out.println(values[0] + " " + values[1]);
				if (authenticationManagerImpl.checkAdminCredentials(values[0], values[1])) {
					System.out.println("Creating and returning JWT.");
					String jwt = JWT.create().withIssuer("Open Banking")
							.withClaim("software_id", body.get("software_id"))
							.withClaim("aud","Ricardo Bank")
							.withClaim("redirect_uri", body.get("redirect_uri"))
							.withClaim("software_statement","testsoftwarestatement")
							.withJWTId("jwtId")
							.sign(authenticationManagerImpl.getEncryptionManager().getPemManagerImpl().getAlgorithm());

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

	@RequestMapping(value="/addAdmin", method = RequestMethod.POST)
	public void addAdmin(@RequestBody Map<String, String> body, HttpServletResponse response) {
		try {
			System.out.println(body.get("adminId"));
			authenticationManagerImpl.addAdmin(body.get("adminId"), body.get("adminSecret"));
			if (adminRepository.persistData()) {
				response.setStatus(200);
			} else {
				adminRepository.delete(body.get("adminId"));
				response.sendError(500, "Could not persist data.");
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	@RequestMapping(value="/persistAdminData", method = RequestMethod.POST)
	public void persistAdminData(HttpServletResponse response) {
		try {
			if (adminRepository.persistData()) {
				response.setStatus(200);
				response.getWriter().write("Succesfully saved admin data.");
			} else {
				response.sendError(500);
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
