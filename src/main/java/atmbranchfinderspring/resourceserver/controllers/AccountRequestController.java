package atmbranchfinderspring.resourceserver.controllers;

import atmbranchfinderspring.resourceserver.annotations.RequestTokenAuthenticated;
import atmbranchfinderspring.resourceserver.authentication.TPPManager;
import atmbranchfinderspring.resourceserver.models.AccountRequest;
import atmbranchfinderspring.resourceserver.models.IncomingAccountRequest;
import atmbranchfinderspring.resourceserver.models.TPPClient;
import atmbranchfinderspring.resourceserver.repos.AccessTokenRepository;
import atmbranchfinderspring.resourceserver.repos.AccountRequestRepository;
import atmbranchfinderspring.resourceserver.validation.accountrequests.AccountRequestValidator;
import atmbranchfinderspring.resourceserver.validation.accountrequests.Permission;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * AccountRequestController is the API endpoint where TPPs can POST new account-requests, and GET existing ones.
 */

@RestController
public class AccountRequestController {

	private AccountRequestRepository accountRequestRepository;
	private AccountRequestValidator accountRequestValidator;
	private AccessTokenRepository accessTokenRepository;
	private TPPManager tppManager;
	private ObjectMapper mapper;

	@Autowired
	public AccountRequestController(AccountRequestRepository accountRequestRepository, AccountRequestValidator accountRequestValidator, AccessTokenRepository accessTokenRepository,
	                                TPPManager tppManager) {
		this.accountRequestRepository = accountRequestRepository;
		this.accountRequestValidator = accountRequestValidator;
		this.accessTokenRepository = accessTokenRepository;
		this.tppManager = tppManager;
		this.mapper = new ObjectMapper();
	}

	@CrossOrigin(origins = "http://localhost:8081")
	@RequestMapping(method = RequestMethod.POST, value = "/account-requests")
	@RequestTokenAuthenticated
	public void postAccountRequest(HttpServletRequest request, HttpServletResponse response, @RequestBody IncomingAccountRequest incomingAccountRequest) throws IOException {
		if (accountRequestValidator.checkPermissionList(incomingAccountRequest.getPermissions())) {
			List<Permission> permissions = accountRequestValidator.convertPermissions(incomingAccountRequest.getPermissions());
			String token = request.getHeader("Authorization").substring("Bearer".length()).trim();
			String clientId = accessTokenRepository.get(token).getClientId();
			AccountRequest accountRequest = accountRequestRepository.createAccountRequestResponse(incomingAccountRequest, permissions, clientId);
			tppManager.addAccountRequestToClient(clientId, accountRequest);
			response.setHeader("Content-type", "application/json");
			response.setStatus(200);
			mapper.writer().writeValue(response.getWriter(), accountRequest);
		} else {
			response.sendError(400, "Bad Request");
		}

	}

	@RequestMapping(method = RequestMethod.GET, value= "/account-requests")
	@RequestTokenAuthenticated
	public void getAllAccountRequests(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String token = request.getHeader("Authorization").substring("Bearer".length()).trim();
		if (accessTokenRepository.contains(token)) {
			String clientId = accessTokenRepository.get(token).getClientId();
			TPPClient client = tppManager.getTPPClient(clientId);
			Collection<String> responseObjects = client.getAllAccountRequestIds();
			response.setHeader("Content-type", "application/json");
			mapper.writer().writeValue(response.getWriter(), responseObjects);
		} else {
			response.sendError(400);
		}

	}

	@RequestMapping(method = RequestMethod.GET, value = "/account-requests/{accountRequestId}", produces = "application/json")
	@RequestTokenAuthenticated
	public void getAccountRequest(HttpServletRequest request, HttpServletResponse response, @PathVariable("accountRequestId") String accountRequestId) throws IOException {

		if (accountRequestRepository.contains(accountRequestId)) {
			AccountRequest requestResponse = accountRequestRepository.get(accountRequestId);
			response.setHeader("Content-type", "application/json");
			mapper.writer().writeValue(response.getWriter(), requestResponse);
		} else {
			response.sendError(400);
		}

	}


}
