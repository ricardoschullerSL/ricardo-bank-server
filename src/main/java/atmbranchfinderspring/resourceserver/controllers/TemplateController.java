package atmbranchfinderspring.resourceserver.controllers;

import atmbranchfinderspring.resourceserver.authentication.AuthenticationManager;
import atmbranchfinderspring.resourceserver.authentication.AuthenticationManagerImpl;
import atmbranchfinderspring.resourceserver.models.AccountRequestResponse;
import atmbranchfinderspring.resourceserver.models.Credentials;
import atmbranchfinderspring.resourceserver.models.Permission;
import atmbranchfinderspring.resourceserver.repos.AccountRequestRepository;
import atmbranchfinderspring.resourceserver.repos.AuthorizationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

@Controller
public class TemplateController {

	private AuthenticationManager authenticationManager;
	private AccountRequestRepository accountRequestRepository;
	private AuthorizationCodeRepository authorizationCodeRepository;

	@Autowired
	public TemplateController(AuthenticationManagerImpl authenticationManager, AccountRequestRepository accountRequestRepository,
	                          AuthorizationCodeRepository authorizationCodeRepository) {
		this.authenticationManager = authenticationManager;
		this.accountRequestRepository = accountRequestRepository;
		this.authorizationCodeRepository = authorizationCodeRepository;
	}

	@RequestMapping("/login/{accountRequestId}")
	public String login(@PathVariable String accountRequestId, Model model, @ModelAttribute Credentials credentials) {
		model.addAttribute("accountRequestId",accountRequestId);
		model.addAttribute("credentials",credentials);
		return "LogInPage";
	}

	@RequestMapping("/authorizeApp/{accountRequestId}")
	public String authorize(@PathVariable String accountRequestId, Model model) {
		AccountRequestResponse accountRequestResponse = accountRequestRepository.get(accountRequestId);
		List<Permission> permissions = accountRequestResponse.getPermissions();
		model.addAttribute("permissions", permissions);
		model.addAttribute("accountRequestId", accountRequestId);
		return "Authorize";
	}

	@RequestMapping("/authorizeApp/{accountRequestId}/{authorization}")
	public void commitAuthorization(HttpServletRequest request, HttpServletResponse response,
	                                @PathVariable String accountRequestId, @PathVariable int authorization) throws IOException, URISyntaxException {
		AccountRequestResponse accountRequestResponse = accountRequestRepository.get(accountRequestId);
		if ( authorization == 1) {
			accountRequestResponse.setStatus(AccountRequestResponse.AccountRequestStatus.AUTHORIZED);
			String authorization_code = UUID.randomUUID().toString();
			authorizationCodeRepository.add(authorization_code);
			response.sendRedirect( authenticationManager.getTPPClient(accountRequestResponse.getClientId()).getRedirectUri() + "/" + authorization_code);
		} else {
			accountRequestResponse.setStatus(AccountRequestResponse.AccountRequestStatus.REJECTED);
			response.sendRedirect(authenticationManager.getTPPClient(accountRequestResponse.getClientId()).getRedirectUri());
		}
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
