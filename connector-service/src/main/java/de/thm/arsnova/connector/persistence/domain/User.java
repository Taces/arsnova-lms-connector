package de.thm.arsnova.connector.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.openjpa.persistence.jdbc.Unique;

@Entity
@Table(name = "user")
public class User {

	@Id
	@Unique
	@Column(name = "user_id")
	private String userId;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "is_admin")
	private boolean isAdmin;
	
	@Unique
	@Column(name = "auth_token")
	private String authToken;

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAdmin() {
		return isAdmin;
	}
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	
	public String getAuthToken() {
		return authToken;
	}

	@Override
	public int hashCode() {
		return (this.userId + this.isAdmin).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj instanceof User) {
			User other = (User) obj;
			if (
					(this.userId != null && this.userId.equals(other.getUserId()))
					&& (this.isAdmin == other.isAdmin())
					) {
				return true;
			}
		}
		return false;
	}
}
