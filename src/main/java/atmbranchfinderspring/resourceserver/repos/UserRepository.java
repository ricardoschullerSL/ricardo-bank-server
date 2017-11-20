package atmbranchfinderspring.resourceserver.repos;

import atmbranchfinderspring.resourceserver.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface UserRepository extends JpaRepository<User, Long>{
	User findByUserName(String username);
	User findByAccountAccountId(int accountId);
}
