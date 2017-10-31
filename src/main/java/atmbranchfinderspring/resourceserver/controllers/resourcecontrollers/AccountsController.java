package atmbranchfinderspring.resourceserver.controllers.resourcecontrollers;

import atmbranchfinderspring.resourceserver.annotations.AccessTokenAuthenticated;
import atmbranchfinderspring.resourceserver.controllers.ResponseBodyWriter;
import atmbranchfinderspring.resourceserver.models.User;
import atmbranchfinderspring.resourceserver.repos.UserRepository;
import atmbranchfinderspring.resourceserver.validation.accountrequests.Permission;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AccountsController is the main entry point for TPPs to access protected resources.
 */

@Controller
@RequestMapping("/accounts")
public class AccountsController {

	private UserRepository userRepository;
	private ResponseBodyWriter responseBodyWriter;

	public AccountsController(UserRepository userRepository, ResponseBodyWriter responseBodyWriter) {
		this.userRepository = userRepository;
		this.responseBodyWriter = responseBodyWriter;
	}

	@RequestMapping("/{accountId}")
	@AccessTokenAuthenticated(requiredPermission = {Permission.ReadAccountsBasic, Permission.ReadAccountsDetail})
	public void getAccount(HttpServletRequest request, HttpServletResponse response, @PathVariable int accountId) throws IOException {
		User user = userRepository.findByAccountAccountId(accountId);
		if (user != null) {
			responseBodyWriter.writeResponse(request, response, user.getAccount());
		} else {
			response.sendError(400);
		}
	}

	@RequestMapping("/{accountId}/transactions}")
	@AccessTokenAuthenticated(requiredPermission = {Permission.ReadTransactionsBasic, Permission.ReadAccountsDetail})
	public void getAccountTransactionsBasic(HttpServletRequest request, HttpServletResponse response, @PathVariable int accountId) throws IOException {
		User user = userRepository.findByAccountAccountId(accountId);
		if (user != null && user.getAccount() != null) {
			responseBodyWriter.writeResponse(request, response, user.getAccount().getCurrencyTransactions());
		} else {
			response.sendError(400);
		}
	}

}
