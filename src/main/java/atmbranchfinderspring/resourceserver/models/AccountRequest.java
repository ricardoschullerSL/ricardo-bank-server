package atmbranchfinderspring.resourceserver.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountRequest {
    private String id;
    private List<String> permissions;
    private LocalDateTime transactionFromDateTime;
    private LocalDateTime transactionToDateTime;

	public AccountRequest() {

    };

    public AccountRequest(String id, List<String> permissions, LocalDateTime transactionFromDateTime, LocalDateTime transactionToDateTime) {
        this.id = id;
        this.permissions = permissions;
        this.transactionFromDateTime = transactionFromDateTime;
        this.transactionToDateTime = transactionToDateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public LocalDateTime getTransactionFromDateTime() {
        return transactionFromDateTime;
    }

    public void setTransactionFromDateTime(LocalDateTime transactionFromDateTime) {
        this.transactionFromDateTime = transactionFromDateTime;
    }

    public LocalDateTime getTransactionToDateTime() {
        return transactionToDateTime;
    }

    public void setTransactionToDateTime(LocalDateTime transactionToDateTime) {
        this.transactionToDateTime = transactionToDateTime;
    }

}
