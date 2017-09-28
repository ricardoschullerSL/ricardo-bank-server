package atmbranchfinderspring.resourceserver.repos;

import atmbranchfinderspring.resourceserver.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{
	User findByUserName(String username);
}
