package atmbranchfinderspring.resourceserver.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users")
public class User {

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name="username", unique = true)
	private String userName;
	@Column(name="hashed_secret")
	private byte[] hashedSecret;
	@Column(name="salt")
	private String salt;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false, targetEntity = Account.class)
	@JoinColumn(name= "account", unique = true, nullable = false)
	@JsonManagedReference
	private Account account;

	@JsonIgnore
	@OneToMany(targetEntity = AccountRequest.class)
	@JoinColumn(name = "accountRequests")
	private List<AccountRequest> accountRequests;

	public User() {
		this.accountRequests = new ArrayList<>();
	}

	public User(int accountId) {
		this.account = new Account(accountId);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public byte[] getHashedSecret() {
		return hashedSecret;
	}

	public void setHashedSecret(byte[] hashedSecret) {
		this.hashedSecret = hashedSecret;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public List<AccountRequest> getAccountRequests() {
		return accountRequests;
	}

	public void setAccountRequests(List<AccountRequest> accountRequests) {
		this.accountRequests = accountRequests;
	}

	public void addAccountRequest(AccountRequest accountRequest) {
		this.accountRequests.add(accountRequest);
	}

	@Override
	public String toString() {
		return "username: " + userName + ", id: " + id;
	}
}
