package atmbranchfinderspring.resourceserver.models;

import javax.persistence.*;

@Entity
@Table(name="USER")
public class User {

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name="USERNAME", unique = true)
	private String userName;
	@Column(name="HASHEDSECRET")
	private byte[] hashedSecret;
	@Column(name="SALT")
	private String salt;
	@Column(name="ACCOUNTID", unique = true)
	private long accountId;

	public User() {

	}

	public User(String userName, byte[] hashedSecret, String salt, long accountId) {
		this.userName = userName;
		this.hashedSecret = hashedSecret;
		this.salt = salt;
		this.accountId = accountId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	@Override
	public String toString() {
		return "username: " + userName + ", id: " + id;
	}
}
