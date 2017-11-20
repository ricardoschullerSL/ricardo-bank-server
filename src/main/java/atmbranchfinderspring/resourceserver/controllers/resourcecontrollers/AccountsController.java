package atmbranchfinderspring.resourceserver.controllers.resourcecontrollers;

import atmbranchfinderspring.resourceserver.annotations.AccessTokenAuthenticated;
import atmbranchfinderspring.resourceserver.controllers.OpenBankingBaseController;
import atmbranchfinderspring.resourceserver.controllers.ResponseBodyWriter;
import atmbranchfinderspring.resourceserver.models.User;
import atmbranchfinderspring.resourceserver.repos.UserRepository;
import atmbranchfinderspring.resourceserver.validation.accesstokens.AccessToken;
import atmbranchfinderspring.resourceserver.validation.accountrequests.Permission;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * AccountsController is the main entry point for TPPs to access protected resources.
 */

@RestController
public class AccountsController extends OpenBankingBaseController {

	private UserRepository userRepository;
	private ResponseBodyWriter responseBodyWriter;

	public AccountsController(UserRepository userRepository, ResponseBodyWriter responseBodyWriter) {
		this.userRepository = userRepository;
		this.responseBodyWriter = responseBodyWriter;
	}

	@RequestMapping(value = "/accounts/{accountId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@AccessTokenAuthenticated(requiredPermission = {Permission.ReadAccountsBasic, Permission.ReadAccountsDetail},
			tokenType = AccessToken.TokenType.ACCESS)
	public Map<String,Object> getAccount(HttpServletRequest request, HttpServletResponse response, @PathVariable int accountId) throws IOException {
		User user = userRepository.findByAccountAccountId(accountId);
		return responseBodyWriter.writeResponse(request, user.getAccount());
	}

	@RequestMapping("/accounts/{accountId}/transactions}")
	@AccessTokenAuthenticated(requiredPermission = {Permission.ReadTransactionsBasic, Permission.ReadAccountsDetail},
			tokenType = AccessToken.TokenType.ACCESS)
	public Map<String, Object> getAccountTransactionsBasic(HttpServletRequest request, HttpServletResponse response, @PathVariable int accountId) throws IOException {
		User user = userRepository.findByAccountAccountId(accountId);
		if (user != null && user.getAccount() != null) {
			return responseBodyWriter.writeResponse(request, user.getAccount().getCurrencyTransactions());
		} else {
			return null;
		}
	}
}
