package atmbranchfinderspring.resourceserver.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {

    @Id
    private String id;
    private String accountId;
    private String accountType;
    private List<CurrencyTransaction> receivedCurrencyTransactions;
    private List<CurrencyTransaction> sentCurrencyTransactions;
    private double balance;


    public Account() {}

    public Account(String accountId, double balance) {
        this.accountId = accountId;
        this.balance = balance;
        this.receivedCurrencyTransactions = new ArrayList<>();
        this.sentCurrencyTransactions = new ArrayList<>();
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

    public List<CurrencyTransaction> getReceivedCurrencyTransactions() {
        return receivedCurrencyTransactions;
    }

    public void setReceivedCurrencyTransactions(ArrayList<CurrencyTransaction> receivedCurrencyTransactions) {
        this.receivedCurrencyTransactions = receivedCurrencyTransactions;
    }

    public List<CurrencyTransaction> getSentCurrencyTransactions() {
        return sentCurrencyTransactions;
    }

    public void setSentCurrencyTransactions(ArrayList<CurrencyTransaction> sentCurrencyTransactions) {
        this.sentCurrencyTransactions = sentCurrencyTransactions;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
