package atmbranchfinderspring.resourceserver.models;


import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "accounts")
public class Account {

	@Id
	@Column(name = "accountId")
    private int accountId;

	@Column(name="account_type")
    private String accountType = "Basic Current Account";

	@OneToMany
	@ElementCollection
	@JoinColumn(name="currency_transactions")
    private List<CurrencyTransaction> CurrencyTransactions;

	private long balanceInCents;

	@OneToOne(mappedBy = "account", optional = false)
	@JsonBackReference
	private User user;


    public Account() {}

    public Account(int accountId) {
    	this.accountId = accountId;
    }

    public Account(int accountId,String accountType, long balanceInCents) {
        this.accountId = accountId;
        this.accountType = accountType;
        this.balanceInCents = balanceInCents;
        this.CurrencyTransactions = new ArrayList<>();
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public List<CurrencyTransaction> getCurrencyTransactions() {
        return CurrencyTransactions;
    }

    public void setCurrencyTransactions(List<CurrencyTransaction> currencyTransactions) {
        this.CurrencyTransactions = currencyTransactions;
    }

	public long getBalanceInCents() {
        return balanceInCents;
    }

    public void setBalanceInCents(long balanceInCents) {
        this.balanceInCents = balanceInCents;
    }


	public User getUser() {
		return user;
	}

	public void setUsername(User user) {
		this.user = user;
	}
}
