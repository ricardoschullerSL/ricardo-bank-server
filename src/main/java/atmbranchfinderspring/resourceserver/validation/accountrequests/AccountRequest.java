package atmbranchfinderspring.resourceserver.validation.accountrequests;

import atmbranchfinderspring.resourceserver.models.ResponseObject;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "account-requests")
public class AccountRequest implements ResponseObject {

	@Id
	@JoinColumn(name="accountRequestId")
    private String accountRequestId;
    @Column(nullable = false)
	private String clientId;
    @Column(nullable = false)
    private LocalDateTime creationDateTime;
    @Column(nullable = false)
    private LocalDateTime expirationDateTime;
    @Column(nullable = false)
    @ElementCollection(targetClass = Permission.class)
    private Set<Permission> permissions;
    @Column
    private LocalDateTime transactionFromDateTime;
    @Column
    private LocalDateTime transactionToDateTime;
    @Column(nullable = false)
    private AccountRequestStatus status;

	public enum AccountRequestStatus {
        AUTHORIZED, AWAITINGAUTHORIZATION, REJECTED, REVOKED
    }

    public AccountRequest() {}

    public AccountRequest(String accountRequestId, String clientId, LocalDateTime creationDateTime, LocalDateTime expirationDateTime,
                          Set<Permission> permissions, LocalDateTime transactionFromDateTime,
                          LocalDateTime transactionToDateTime, AccountRequestStatus status) {
        this.accountRequestId = accountRequestId;
        this.clientId = clientId;
        this.creationDateTime = creationDateTime;
        this.expirationDateTime = expirationDateTime;
        this.permissions = permissions;
        this.transactionFromDateTime = transactionFromDateTime;
        this.transactionToDateTime = transactionToDateTime;
        this.status = status;
    }

    public AccountRequest(IncomingAccountRequest incomingAccountRequest, Set<Permission> permissions, String clientId,
                          AccountRequestStatus status, LocalDateTime creationDateTime, LocalDateTime expirationDateTime) {
        this.accountRequestId = incomingAccountRequest.getId();
        this.transactionFromDateTime = incomingAccountRequest.getTransactionFromDateTime();
        this.transactionToDateTime = incomingAccountRequest.getTransactionToDateTime();
        this.permissions = permissions;
        this.clientId = clientId;
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

    public Set<Permission> getPermissions() {
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

    public void setStatus(AccountRequestStatus status) {
    	this.status = status;
    }

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setAccountRequestId(String accountRequestId) {
		this.accountRequestId = accountRequestId;
	}

	public void setCreationDateTime(LocalDateTime creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	public void setExpirationDateTime(LocalDateTime expirationDateTime) {
		this.expirationDateTime = expirationDateTime;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	public void setTransactionFromDateTime(LocalDateTime transactionFromDateTime) {
		this.transactionFromDateTime = transactionFromDateTime;
	}

	public void setTransactionToDateTime(LocalDateTime transactionToDateTime) {
		this.transactionToDateTime = transactionToDateTime;
	}
}
