package atmbranchfinderspring.resourceserver.models;

import java.time.LocalDateTime;
import java.util.List;


public class AccountRequestResponse implements ResponseObject{

    private String accountRequestId;
    private LocalDateTime creationDateTime;
    private LocalDateTime expirationDateTime;
    private List<Permission> permissions;
    private LocalDateTime transactionFromDateTime;
    private LocalDateTime transactionToDateTime;
    private AccountRequestStatus status;

    public enum AccountRequestStatus {
        AUTHORIZED, AWAITINGAUTHORIZATION, REJECTED, REVOKED
    }

    public AccountRequestResponse() {}

    public AccountRequestResponse(String accountRequestId, LocalDateTime creationDateTime, LocalDateTime expirationDateTime,
                                  List<Permission> permissions, LocalDateTime transactionFromDateTime, LocalDateTime transactionToDateTime, AccountRequestStatus status) {
        this.accountRequestId = accountRequestId;
        this.creationDateTime = creationDateTime;
        this.expirationDateTime = expirationDateTime;
        this.permissions = permissions;
        this.transactionFromDateTime = transactionFromDateTime;
        this.transactionToDateTime = transactionToDateTime;
        this.status = status;
    }

    public AccountRequestResponse(AccountRequest accountRequest, List<Permission> permissions, AccountRequestStatus status, LocalDateTime creationDateTime, LocalDateTime expirationDateTime) {
        this.accountRequestId = accountRequest.getId();
        this.transactionFromDateTime = accountRequest.getTransactionFromDateTime();
        this.transactionToDateTime = accountRequest.getTransactionToDateTime();
        this.permissions = permissions;
        this.status = status;
        this.creationDateTime = creationDateTime;
        this.expirationDateTime = expirationDateTime;
    }

    public String getAccountRequestId() {
        return accountRequestId;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public LocalDateTime getExpirationDateTime() {
        return expirationDateTime;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public LocalDateTime getTransactionFromDateTime() {
        return transactionFromDateTime;
    }

    public LocalDateTime getTransactionToDateTime() {
        return transactionToDateTime;
    }

    public AccountRequestStatus getStatus() {
        return status;
    }
}
