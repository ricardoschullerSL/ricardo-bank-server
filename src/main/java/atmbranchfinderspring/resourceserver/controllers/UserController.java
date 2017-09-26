package atmbranchfinderspring.resourceserver.controllers;

import atmbranchfinderspring.resourceserver.authentication.EncryptionManager;
import atmbranchfinderspring.resourceserver.models.User;
import atmbranchfinderspring.resourceserver.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

@Controller
@RequestMapping(path ="/users")
public class UserController {

	private EncryptionManager encryptionManager;
	private UserRepository userRepository;

	@Autowired
	public UserController(EncryptionManager encryptionManager, UserRepository userRepository) {
		this.encryptionManager = encryptionManager;
		this.userRepository = userRepository;
	}


	@GetMapping(path="/add")
	public @ResponseBody String addNewUser(@RequestParam String username,@RequestParam String password, @RequestParam long accountId) {
		User user = new User();
		String salt = UUID.randomUUID().toString();
		byte[] hashedSecret = encryptionManager.SHA256(password + salt);
		user.setUserName(username);
		user.setHashedSecret(hashedSecret);
		user.setSalt(salt);
		user.setAccountId(accountId);
		userRepository.save(user);
		return "Saved";
	}

	@GetMapping(path="/all")
	public @ResponseBody Iterable<User> getAllUsers() {
		return userRepository.findAll();
	}
}
