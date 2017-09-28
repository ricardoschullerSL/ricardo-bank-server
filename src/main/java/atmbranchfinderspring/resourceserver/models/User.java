package atmbranchfinderspring.resourceserver.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

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
	@JoinColumn(name= "accountId", unique = true, nullable = false)
	@JsonManagedReference
	private Account account;

	public User() {

	}

	public User(long accountId) {
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

	@Override
	public String toString() {
		return "username: " + userName + ", id: " + id;
	}
}
