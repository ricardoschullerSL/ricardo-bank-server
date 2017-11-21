package atmbranchfinderspring.resourceserver.controllers;

import atmbranchfinderspring.resourceserver.annotations.AccessTokenAuthenticated;
import atmbranchfinderspring.resourceserver.authentication.TPPManager;
import atmbranchfinderspring.resourceserver.models.TPPClient;
import atmbranchfinderspring.resourceserver.repos.AccessTokenRepository;
import atmbranchfinderspring.resourceserver.repos.AccountRequestRepository;
import atmbranchfinderspring.resourceserver.validation.accesstokens.AccessToken;
import atmbranchfinderspring.resourceserver.validation.accountrequests.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * AccountRequestController is the API endpoint where TPPs can POST new account-requests, and GET existing ones.
 */

@RestController
public class AccountRequestController extends OpenBankingBaseController {

	private AccountRequestRepository accountRequestRepository;
	private AccountRequestValidator accountRequestValidator;
	private AccessTokenRepository accessTokenRepository;
	private TPPManager tppManager;
	private ResponseBodyWriter responseBodyWriter;

	@Autowired
	public AccountRequestController(AccountRequestRepository accountRequestRepository, AccountRequestValidator accountRequestValidator, AccessTokenRepository accessTokenRepository,
	                                TPPManager tppManager, ResponseBodyWriter responseBodyWriter) {
		this.accountRequestRepository = accountRequestRepository;
		this.accountRequestValidator = accountRequestValidator;
		this.accessTokenRepository = accessTokenRepository;
		this.tppManager = tppManager;
		this.responseBodyWriter = responseBodyWriter;
	}

	@CrossOrigin
	@PostMapping(value = "/account-requests", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@AccessTokenAuthenticated(requiredPermission = {}, tokenType = AccessToken.TokenType.REQUEST)
	@ResponseStatus(HttpStatus.CREATED)
	public Map<String, Object> postAccountRequest(HttpServletRequest request, HttpServletResponse response, @RequestBody IncomingRequestBody incomingRequestBody) throws IOException, NullPointerException {
		IncomingAccountRequest incomingAccountRequest = incomingRequestBody.getData();
		if (accountRequestValidator.checkPermissionList(incomingAccountRequest.getPermissions())) {
			Set<Permission> permissions = accountRequestValidator.convertPermissions(incomingAccountRequest.getPermissions());
			String token = request.getHeader("Authorization").substring("Bearer".length()).trim();
			String clientId = accessTokenRepository.get(token).getClientId();
			AccountRequest accountRequest = accountRequestRepository.createAccountRequestResponse(incomingAccountRequest, permissions, clientId, 1001);
			tppManager.addAccountRequestToClient(clientId, accountRequest);
			return responseBodyWriter.writeResponse(request, accountRequest);
		} else {
			response.sendError(400, "Bad Request");
			return null;
		}
	}

	@CrossOrigin
	@GetMapping(value = "/account-requests", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@AccessTokenAuthenticated(requiredPermission = {}, tokenType = AccessToken.TokenType.REQUEST)
	public Map<String, Object> getAllAccountRequests(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String token = request.getHeader("Authorization").substring("Bearer".length()).trim();
		if (accessTokenRepository.contains(token)) {
			String clientId = accessTokenRepository.get(token).getClientId();
			TPPClient client = tppManager.getTPPClient(clientId);
			Collection<String> responseObjects = client.getAllAccountRequestIds();
			return responseBodyWriter.writeResponse(request, responseObjects);
		} else {
			response.sendError(400);
			return null;
		}
	}

	@CrossOrigin
	@GetMapping(value = "/account-requests/{accountRequestId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@AccessTokenAuthenticated(requiredPermission = {}, tokenType = AccessToken.TokenType.REQUEST)
	public Map<String, Object> getAccountRequest(HttpServletRequest request, HttpServletResponse response, @PathVariable("accountRequestId") String accountRequestId) throws IOException {
		if (accountRequestRepository.contains(accountRequestId)) {
			return responseBodyWriter.writeResponse(request, accountRequestRepository.get(accountRequestId));
		} else {
			response.sendError(400);
			return null;
		}
	}
}
