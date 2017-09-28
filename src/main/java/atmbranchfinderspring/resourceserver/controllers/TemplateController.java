package atmbranchfinderspring.resourceserver.controllers;

import atmbranchfinderspring.resourceserver.authentication.AuthenticationManager;
import atmbranchfinderspring.resourceserver.authentication.AuthenticationManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class TemplateController {

	private AuthenticationManager authenticationManager;

	@Autowired
	public TemplateController(AuthenticationManagerImpl authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@RequestMapping("/login")
	public String login() {
		return "LogInPage";
	}

	@RequestMapping("/authorizeApp")
	public String authorize() {
		return "Authorize";
	}

	@RequestMapping("/authenticate/{accountRequestId}")
	public void authenticate(HttpServletRequest request, HttpServletResponse response, @PathVariable String accountRequestId) throws IOException {
		String username = request.getParameter("_username_");
		String password = request.getParameter("_password_");

		System.out.println("Validating account request id: " + accountRequestId + " for " + username + " " + password);

		try {
			if (username != null && accountRequestId != null) {

				if (authenticationManager.checkUserCredentials(username, password)) {
					System.out.println("User authenticated yeah boiii");
				} else {
					response.sendError(403, "Incorrect credentials");
				}
			} else {
				response.sendError(400, "No username found");
			}
		} catch (Throwable e) {
			System.out.println(e);
			response.sendError(500);
		}

	}
}
