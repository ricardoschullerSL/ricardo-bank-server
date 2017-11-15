package atmbranchfinderspring.resourceserver.repos;

import atmbranchfinderspring.resourceserver.validation.accountrequests.AccountRequest;
import atmbranchfinderspring.resourceserver.validation.accountrequests.IncomingAccountRequest;
import atmbranchfinderspring.resourceserver.validation.accountrequests.Permission;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;


@org.springframework.stereotype.Repository
public class AccountRequestRepository implements Repository<AccountRequest>{

    private long expirationTime = 31 * 24 * 60 * 60 * 1000;
    private HashMap<String, AccountRequest> accountRequests;

    public AccountRequestRepository() {
        accountRequests = new HashMap<>();
    }

    public AccountRequest createAccountRequestResponse(IncomingAccountRequest incomingAccountRequest, Set<Permission> permissions, String clientId, int accountId) {
        String randomId = UUID.randomUUID().toString();
        incomingAccountRequest.setId(randomId);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiration = now.plusSeconds(expirationTime);
        AccountRequest accountRequest = new AccountRequest(incomingAccountRequest, permissions, clientId, accountId, AccountRequest.AccountRequestStatus.AWAITINGAUTHORIZATION,
                now, expiration);
        accountRequests.put(accountRequest.getAccountRequestId(), accountRequest);
        return accountRequest;
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
