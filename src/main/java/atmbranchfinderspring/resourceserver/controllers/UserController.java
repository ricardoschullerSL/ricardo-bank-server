package atmbranchfinderspring.resourceserver.controllers;

import atmbranchfinderspring.resourceserver.authentication.EncryptionManager;
import atmbranchfinderspring.resourceserver.models.Account;
import atmbranchfinderspring.resourceserver.models.User;
import atmbranchfinderspring.resourceserver.repos.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping(path ="/users")
public class UserController {

	private EncryptionManager encryptionManager;
	private UserRepository userRepository;
	private SessionFactory sessionFactory;

	@Autowired
	public UserController(EncryptionManager encryptionManager, UserRepository userRepository, SessionFactory sessionFactory) {
		this.encryptionManager = encryptionManager;
		this.userRepository = userRepository;
		this.sessionFactory = sessionFactory;
	}


	@GetMapping(path="/add")
	public @ResponseBody String addNewUser(@RequestParam String username,@RequestParam String password, @RequestParam long accountId) {
		Account account = new Account(accountId);
		User user = new User();
		String salt = UUID.randomUUID().toString();
		byte[] hashedSecret = encryptionManager.SHA256(password + salt);
		user.setUserName(username);
		user.setHashedSecret(hashedSecret);
		user.setSalt(salt);
		user.setAccount(account);
		userRepository.save(user);
		return "Saved";
	}

	@GetMapping(path="/all")
	public @ResponseBody Iterable<User> getAllUsers() {
		return userRepository.findAll();
	}

	@PostMapping(path="/update")
	public @ResponseBody String updateUser(@RequestBody Map<String, Object> body) {
		for (Object entry: body.values()) {
			System.out.println(entry.toString());
		}
		return "Something happened";
	}

}
