package atmbranchfinderspring.resourceserver.repos;

import atmbranchfinderspring.resourceserver.models.IncomingAccountRequest;
import atmbranchfinderspring.resourceserver.models.AccountRequest;
import atmbranchfinderspring.resourceserver.models.Permission;

import java.time.LocalDateTime;
import java.util.*;


@org.springframework.stereotype.Repository
public class AccountRequestRepository implements Repository<AccountRequest>{

    private long expirationTime = 31 * 24 * 60 * 60 * 1000;
    private HashMap<String, AccountRequest> accountRequests;

    public AccountRequestRepository() {
        accountRequests = new HashMap<>();
    }

    public AccountRequest createAccountRequestResponse(IncomingAccountRequest incomingAccountRequest, List<Permission> permissions, String clientId) {
        String randomId = UUID.randomUUID().toString();
        incomingAccountRequest.setId(randomId);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiration = now.plusSeconds(expirationTime);
        AccountRequest response = new AccountRequest(incomingAccountRequest, permissions, clientId, AccountRequest.AccountRequestStatus.AWAITINGAUTHORIZATION,
                now, expiration);
        accountRequests.put(response.getAccountRequestId(), response);
        return response;
    }



    public AccountRequest get(String id) {
        return accountRequests.get(id);
    }

    public void add(AccountRequest entity) {
        accountRequests.put(entity.getAccountRequestId(), entity);
    }

    public boolean contains(String id) {
        return accountRequests.containsKey(id);
    }

    public Collection<String> getAllIds() {
        return accountRequests.keySet();
    }

    public void delete(AccountRequest entity) {
        accountRequests.remove(entity.getAccountRequestId());
    }

    public void delete(String accountRequestId) { accountRequests.remove(accountRequestId); }
}
