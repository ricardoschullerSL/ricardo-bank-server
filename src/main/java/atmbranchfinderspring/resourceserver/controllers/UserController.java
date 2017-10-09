package atmbranchfinderspring.resourceserver.controllers;

import atmbranchfinderspring.resourceserver.annotations.AdminBasicAuthenticated;
import atmbranchfinderspring.resourceserver.authentication.EncryptionManager;
import atmbranchfinderspring.resourceserver.models.Account;
import atmbranchfinderspring.resourceserver.models.User;
import atmbranchfinderspring.resourceserver.repos.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 * UsersController endpoint is an endpoint where admins can add and view 'bank clients'. These are users with a bank
 * account attached.
 */

@RestController
@RequestMapping(path ="/users")
public class UserController {

	private EncryptionManager encryptionManager;
	private UserRepository userRepository;
	private ObjectMapper mapper;

	@Autowired
	public UserController(EncryptionManager encryptionManager, UserRepository userRepository, SessionFactory sessionFactory) {
		this.encryptionManager = encryptionManager;
		this.userRepository = userRepository;
		this.mapper = new ObjectMapper();
	}


	@GetMapping(path="/add")
	@AdminBasicAuthenticated
	public void addNewUser(HttpServletRequest request, HttpServletResponse response, @RequestParam String username,
	                         @RequestParam String password, @RequestParam int accountId) throws IOException {
		Account account = new Account(accountId);
		User user = new User();
		String salt = UUID.randomUUID().toString();
		byte[] hashedSecret = encryptionManager.SHA256(password + salt);
		user.setUserName(username);
		user.setHashedSecret(hashedSecret);
		user.setSalt(salt);
		user.setAccount(account);
		userRepository.save(user);
		response.getWriter().write( "Saved");
	}

	@GetMapping(path="/all", produces = "application/json")
	@AdminBasicAuthenticated
	public void getAllUsers(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println(userRepository.findAll().size());
		mapper.writer().writeValue(response.getWriter(), userRepository.findAll());
	}

	@PostMapping(path="/update")
	@AdminBasicAuthenticated
	public String updateUser(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> body) {
		for (Object entry: body.values()) {
			System.out.println(entry.toString());
		}
		return "Something happened";
	}

}
