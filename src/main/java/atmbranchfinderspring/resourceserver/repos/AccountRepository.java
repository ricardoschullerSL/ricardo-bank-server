package atmbranchfinderspring.resourceserver.repos;

import atmbranchfinderspring.resourceserver.models.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Collection;
/**
 * @author Ricardo Schuller
 * @version 0.1.0
 * @since 0.1.0
 */
@RepositoryRestResource(collectionResourceRel = "account", path = "account")
public interface AccountRepository extends MongoRepository<Account, String> {

    public Account findByAccountId(@Param(value = "accountId") String accountId);
    public Collection<Account> findByAccountType(@Param(value = "accountType") String accountType);
}
