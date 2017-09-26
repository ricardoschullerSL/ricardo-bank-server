package atmbranchfinderspring.resourceserver.controllers;

import atmbranchfinderspring.resourceserver.authentication.AuthenticationManager;
import atmbranchfinderspring.resourceserver.authentication.AuthenticationManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

	@RequestMapping("/authenticate")
	public void authenticate(HttpServletRequest request, HttpServletResponse response) {
		String username = request.getParameter("_username_");
		String password = request.getParameter("_password_");
		System.out.println(username + " " + password);
	}
}
