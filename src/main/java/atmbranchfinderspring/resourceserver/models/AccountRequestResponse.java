package atmbranchfinderspring.resourceserver.models;

import java.util.Date;
import java.util.List;
/**
 * @author Ricardo Schuller
 * @version 0.1.0
 * @since 0.1.0
 */

public class AccountRequestResponse implements ResponseObject{

    private String accountRequestId;
    private Date creationDateTime;
    private Date expirationDateTime;
    private List<String> permissions;
    private Date transactionFromDateTime;
    private Date transactionToDateTime;
    private AccountRequestStatus status;

    public enum AccountRequestStatus {
        AUTHORIZED, AWAITINGAUTHORIZATION, REJECTED, REVOKED
    }

    public AccountRequestResponse() {}

    public AccountRequestResponse(String accountRequestId, Date creationDateTime, Date expirationDateTime,
                                  List<String> permissions, Date transactionFromDateTime, Date transactionToDateTime, AccountRequestStatus status) {
        this.accountRequestId = accountRequestId;
        this.creationDateTime = creationDateTime;
        this.expirationDateTime = expirationDateTime;
        this.permissions = permissions;
        this.transactionFromDateTime = transactionFromDateTime;
        this.transactionToDateTime = transactionToDateTime;
        this.status = status;
    }

    public AccountRequestResponse(AccountRequest accountRequest, AccountRequestStatus status, Date creationDateTime, Date expirationDateTime) {
        this.accountRequestId = accountRequest.getId();
        this.transactionFromDateTime = accountRequest.getTransactionFromDateTime();
        this.transactionToDateTime = accountRequest.getTransactionToDateTime();
        this.permissions = accountRequest.getPermissions();
        this.status = status;
        this.creationDateTime = creationDateTime;
        this.expirationDateTime = expirationDateTime;
    }

    public String getAccountRequestId() {
        return accountRequestId;
    }

    public Date getCreationDateTime() {
        return creationDateTime;
    }

    public Date getExpirationDateTime() {
        return expirationDateTime;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public Date getTransactionFromDateTime() {
        return transactionFromDateTime;
    }

    public Date getTransactionToDateTime() {
        return transactionToDateTime;
    }

    public AccountRequestStatus getStatus() {
        return status;
    }
}
