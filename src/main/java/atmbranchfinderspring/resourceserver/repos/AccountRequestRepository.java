package atmbranchfinderspring.resourceserver.repos;

import atmbranchfinderspring.resourceserver.models.AccountRequest;
import atmbranchfinderspring.resourceserver.models.AccountRequestResponse;

import java.util.*;


@org.springframework.stereotype.Repository
public class AccountRequestRepository implements Repository<AccountRequestResponse>{

    private long expirationTime = 31 * 24 * 60 * 60 * 1000;
    private HashMap<String, AccountRequestResponse> accountRequests;

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

    public void add(AccountRequestResponse entity) {
        accountRequests.put(entity.getAccountRequestId(), entity);
    }

    public Boolean contains(String id) {
        return accountRequests.containsKey(id);
    }

    public Collection<String> getAllIds() {
        return accountRequests.keySet();
    }

    public void delete(AccountRequestResponse entity) {
        accountRequests.remove(entity);
    }

    public void delete(String accountRequestId) { accountRequests.remove(accountRequestId); }
}
