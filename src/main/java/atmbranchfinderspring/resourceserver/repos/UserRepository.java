package atmbranchfinderspring.resourceserver.repos;

import atmbranchfinderspring.resourceserver.models.User;
import org.springframework.data.repository.CrudRepository;


@org.springframework.stereotype.Repository
public interface UserRepository extends CrudRepository<User, Long>{

}
