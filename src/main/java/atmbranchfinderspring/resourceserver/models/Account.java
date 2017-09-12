package atmbranchfinderspring.resourceserver.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ricardo Schuller
 * @version 0.1.0
 * @since 0.1.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {

    @Id
    private String id;
    private String accountId;
    private String accountType;
    private List<Transaction> receivedTransactions;
    private List<Transaction> sentTransactions;
    private Double balance;


    public Account() {}

    public Account(String accountId, Double balance) {
        this.accountId = accountId;
        this.balance = balance;
        this.receivedTransactions = new ArrayList<>();
        this.sentTransactions = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public List<Transaction> getReceivedTransactions() {
        return receivedTransactions;
    }

    public void setReceivedTransactions(ArrayList<Transaction> receivedTransactions) {
        this.receivedTransactions = receivedTransactions;
    }

    public List<Transaction> getSentTransactions() {
        return sentTransactions;
    }

    public void setSentTransactions(ArrayList<Transaction> sentTransactions) {
        this.sentTransactions = sentTransactions;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
