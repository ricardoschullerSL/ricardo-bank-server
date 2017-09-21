package atmbranchfinderspring.resourceserver.repos;

import atmbranchfinderspring.resourceserver.models.AccountRequest;
import atmbranchfinderspring.resourceserver.models.AccountRequestResponse;

import java.time.LocalDateTime;
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
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiration = now.plusSeconds(expirationTime);
        AccountRequestResponse response = new AccountRequestResponse(accountRequest, AccountRequestResponse.AccountRequestStatus.AWAITINGAUTHORIZATION,
                now, expiration);
        accountRequests.put(response.getAccountRequestId(), response);
        return response;
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
        accountRequests.remove(entity.getAccountRequestId());
    }

    public void delete(String accountRequestId) { accountRequests.remove(accountRequestId); }
}
