package atmbranchfinderspring.resourceserver.controllers;

import atmbranchfinderspring.resourceserver.authentication.AuthenticationManager;
import atmbranchfinderspring.resourceserver.authentication.AuthenticationManagerImpl;
import atmbranchfinderspring.resourceserver.models.AccountRequest;
import atmbranchfinderspring.resourceserver.models.Credentials;
import atmbranchfinderspring.resourceserver.validation.accountrequests.Permission;
import atmbranchfinderspring.resourceserver.repos.AccountRequestRepository;
import atmbranchfinderspring.resourceserver.repos.AuthorizationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

/**
 * TemplateController returns the Login and Authentication page. It uses Thymeleaf as template-engine.
 */

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
		AccountRequest accountRequest = accountRequestRepository.get(accountRequestId);
		List<Permission> permissions = accountRequest.getPermissions();
		model.addAttribute("permissions", permissions);
		model.addAttribute("accountRequestId", accountRequestId);
		return "Authorize";
	}

	@RequestMapping("/authorizeApp/{accountRequestId}/{authorization}")
	public void commitAuthorization(HttpServletRequest request, HttpServletResponse response,
	                                @PathVariable String accountRequestId,
	                                @PathVariable int authorization) throws IOException, URISyntaxException {
		if (authenticationManager.isAccountRequestIdValid(accountRequestId) == false) {
			response.sendError(500, "Something went wrong...");
		}
		AccountRequest accountRequest = authenticationManager.getAccountRequest(accountRequestId);
		if ( authorization == 1) {
			accountRequest.setStatus(AccountRequest.AccountRequestStatus.AUTHORIZED);
			String authorization_code = UUID.randomUUID().toString();
			authorizationCodeRepository.add(authorization_code);
			response.sendRedirect( authenticationManager.getTPPClient(accountRequest.getClientId()).getRedirectUri() + "/" + authorization_code);
		} else {
			accountRequest.setStatus(AccountRequest.AccountRequestStatus.REJECTED);
			response.sendRedirect(authenticationManager.getTPPClient(accountRequest.getClientId()).getRedirectUri());
		}
	}

	@RequestMapping("/authenticate/{accountRequestId}")
	public void authenticate(HttpServletRequest request, HttpServletResponse response,
	                         @ModelAttribute(value="credentials") Credentials credentials,
	                         @PathVariable String accountRequestId) throws IOException {

		String username = credentials.getId();
		String password = credentials.getSecret();

		System.out.println("Validating account request id: " + accountRequestId + " for " + username);

		try {
			if (isNotNull(username) && isNotNull(accountRequestId)) {

				if (authenticationManager.areUserCredentialsValid(username, password) && authenticationManager.isAccountRequestIdValid(accountRequestId) ) {
					AccountRequest accountRequest = authenticationManager.getAccountRequest(accountRequestId);
					System.out.println("User authenticated yeah boiii");
					response.sendRedirect("/authorizeApp/" + accountRequestId);
				} else {
					response.sendError(403, "Invalid credentials.");
				}
			} else {
				response.sendError(403, "Invalid credentials.");
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
