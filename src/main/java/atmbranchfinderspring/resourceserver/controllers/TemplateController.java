package atmbranchfinderspring.resourceserver.controllers;

import atmbranchfinderspring.resourceserver.authentication.AuthenticationManager;
import atmbranchfinderspring.resourceserver.authentication.AuthenticationManagerImpl;
import atmbranchfinderspring.resourceserver.models.AccountRequestResponse;
import atmbranchfinderspring.resourceserver.models.Credentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

	@RequestMapping("/login/{accountRequestId}")
	public String login(@PathVariable String accountRequestId, Model model, @ModelAttribute Credentials credentials) {
		model.addAttribute("accountRequestId",accountRequestId);
		model.addAttribute("credentials",credentials);
		return "LogInPage";
	}

	@RequestMapping("/authorizeApp/{accountRequestId}")
	public String authorize(@PathVariable String accountRequestId) {
		System.out.println(accountRequestId);
		return "Authorize";
	}

	@RequestMapping("/authenticate/{accountRequestId}")
	public void authenticate(HttpServletRequest request, HttpServletResponse response,
	                                         @ModelAttribute(value="credentials") Credentials credentials, @PathVariable String accountRequestId) throws IOException {
		String username = credentials.getId();
		String password = credentials.getSecret();

		System.out.println("Validating account request id: " + accountRequestId + " for " + username + " " + password);

		try {
			if (isNotNull(username) && isNotNull(accountRequestId)) {

				if (authenticationManager.checkUserCredentials(username, password)) {
					AccountRequestResponse accountRequestResponse = authenticationManager.getAccountRequest(accountRequestId);
					System.out.println("User authenticated yeah boiii");
					response.sendRedirect("/authorizeApp/" + accountRequestId);
				} else {
					response.sendError(403, "Incorrect credentials boiii");
				}
			} else {
				response.sendError(400, "No username found boiii");
			}
		} catch (Throwable e) {
			System.out.println(e);
			response.sendError(500, "sad boiii");
		}

	}

	private Boolean isNotNull(Object o) {
		return o != null;
	}
}
