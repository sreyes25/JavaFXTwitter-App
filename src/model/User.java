package model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class User implements Serializable {

	private Account account;
	private String username;
	private String password;
	private String email;
	private String id;

	public User(String username, String password, String email) {
		super();
		account = new Account(username);
		this.username = username;
		this.password = password;
		this.email = email;
		this.id = null;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "User [account=" + account + ", username=" + username + ", password=" + password + ", email=" + email
				+ ", id=" + id + "]";
	}
}
