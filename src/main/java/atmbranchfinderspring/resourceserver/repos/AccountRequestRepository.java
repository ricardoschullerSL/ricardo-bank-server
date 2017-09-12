package atmbranchfinderspring.resourceserver.repos;

import atmbranchfinderspring.resourceserver.models.AccountRequest;
import atmbranchfinderspring.resourceserver.models.AccountRequestResponse;

import java.util.*;

/**
 * @author Ricardo Schuller
 * @version 0.1.0
 * @since 0.1.0
 */
@org.springframework.stereotype.Repository
public class AccountRequestRepository implements Repository<AccountRequestResponse>{

    private int lowerLimit = 0;
    private int upperLimit = Integer.MAX_VALUE;
    private long expirationTime = 31 * 24 * 60 * 60 * 1000;
    private HashMap<String, AccountRequestResponse> accountRequests;
    private Random random = new Random();

    public AccountRequestRepository() {
        accountRequests = new HashMap<>();
    }

    public AccountRequestResponse createAccountRequestResponse(AccountRequest accountRequest) {
        String randomId = UUID.randomUUID().toString();
        accountRequest.setId(randomId);
        Date now = new Date();
        Date expiration = new Date();
        expiration.setTime(now.getTime() + expirationTime);
        AccountRequestResponse newRequest = new AccountRequestResponse(accountRequest, AccountRequestResponse.AccountRequestStatus.AWAITINGAUTHORIZATION,
                now, expiration);
        accountRequests.put(newRequest.getAccountRequestId(), newRequest);
        return newRequest;
    }



    public AccountRequestResponse get(String id) {
        return accountRequests.get(id);
    }

    @Override
    public void add(AccountRequestResponse entity) {
        accountRequests.put(entity.getAccountRequestId(), entity);
    }

    @Override
    public Collection<AccountRequestResponse> getAll() {
        return null;
    }


    public void delete(int id) {
        accountRequests.remove(id);
    }

    @Override
    public void delete(AccountRequestResponse entity) {
        accountRequests.remove(entity);
    }

    @Override
    public void delete(String accountRequestId) { accountRequests.remove(accountRequestId); }
}
