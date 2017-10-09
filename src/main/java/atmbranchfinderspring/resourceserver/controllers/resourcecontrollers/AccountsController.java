package atmbranchfinderspring.resourceserver.controllers.resourcecontrollers;

import atmbranchfinderspring.resourceserver.annotations.AccessTokenAuthenticated;
import atmbranchfinderspring.resourceserver.models.User;
import atmbranchfinderspring.resourceserver.repos.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
	private ObjectMapper mapper;

	public AccountsController(UserRepository userRepository) {
		this.userRepository = userRepository;
		this.mapper = new ObjectMapper();
	}

	@RequestMapping("/{accountId}")
	@AccessTokenAuthenticated
	public void getAccount(HttpServletRequest request, HttpServletResponse response, @PathVariable int accountId) throws IOException {
		User user = userRepository.findByAccountAccountId(accountId);
		if (user != null) {
			mapper.writer().writeValue(response.getWriter(), user.getAccount());
		} else {
			response.sendError(400);
		}
	}

	@RequestMapping("/{accountId/transactions}")
	@AccessTokenAuthenticated
	public void getAccountTransactions(HttpServletRequest request, HttpServletResponse response, @PathVariable int accountId) throws IOException {
		User user = userRepository.findByAccountAccountId(accountId);
		if (user != null) {
			mapper.writer().writeValue(response.getWriter(), user.getAccount().getCurrencyTransactions());
		} else {
			response.sendError(400);
		}
	}



}
