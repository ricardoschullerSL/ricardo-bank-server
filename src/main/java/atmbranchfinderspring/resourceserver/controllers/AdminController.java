package atmbranchfinderspring.resourceserver.controllers;

import atmbranchfinderspring.resourceserver.annotations.AdminBasicAuthenticated;
import atmbranchfinderspring.resourceserver.authentication.AuthenticationManager;
import atmbranchfinderspring.resourceserver.authentication.TPPManager;
import atmbranchfinderspring.resourceserver.repos.AdminRepository;
import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
public class AdminController {

	private AuthenticationManager authenticationManager;
	private TPPManager tppManager;
	private AdminRepository adminRepository;
	private ObjectMapper mapper;


	@Autowired
	public AdminController(TPPManager tppManager, AuthenticationManager authenticationManager, AdminRepository adminRepository) {
		this.tppManager = tppManager;
		this.authenticationManager = authenticationManager;
		this.adminRepository = adminRepository;
		this.mapper = new ObjectMapper();
	}



	@RequestMapping(value="/getjwt", method = RequestMethod.POST)
	@AdminBasicAuthenticated
	public void getJWT(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, String> body) throws IOException {
		System.out.println("Creating and returning JWT.");
		String jwt = JWT.create().withIssuer("Open Banking")
				.withClaim("software_id", body.get("software_id"))
				.withClaim("aud", "Ricardo Bank")
				.withClaim("redirect_uri", body.get("redirect_uri"))
				.withClaim("software_statement", "testsoftwarestatement")
				.withJWTId("jwtId")
				.sign(authenticationManager.getEncryptionManager().getAlgorithm());

		response.setStatus(200);
		mapper.writer().writeValue(response.getWriter(), jwt);
	}

	@RequestMapping(value="/addAdmin", method = RequestMethod.POST)
	@AdminBasicAuthenticated
	public void addAdmin(HttpServletRequest request, HttpServletResponse response,
	                     @RequestBody Map<String, String> body) throws IOException {
		try {

			System.out.println(body.get("adminId"));
			authenticationManager.addAdmin(body.get("adminId"), body.get("adminSecret"));
			adminRepository.persistData();
			response.setStatus(200);

		} catch (IOException e) {
			System.out.println(e);
			response.sendError(500, "Could not persist data.");
		}
	}

	@RequestMapping(value="/persistAdminData", method = RequestMethod.POST)
	@AdminBasicAuthenticated
	public void persistAdminData(HttpServletResponse response) throws IOException {

		try {
			adminRepository.persistData();
			response.setStatus(200);
			response.getWriter().write("Succesfully saved admin data.");
		} catch (IOException e) {
			System.out.println(e);
			response.sendError(500);
		}
	}
}
