package Server;

import java.io.Serializable;
import java.time.Duration;
import java.util.Date;

public class AccessToken implements Serializable{

	private long timestamp;
	private long expiration;
	private String issuer;
	private String holder;
	private Role role;
	
	public AccessToken(String issuer, String holder, long lifeSpan, Role role) {
		timestamp = new Date().getTime();
		expiration = timestamp + Duration.ofHours(lifeSpan).toMillis();
		this.issuer = issuer;
		this.holder = holder;
		this.role = role;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public long getExpiration() {
		return expiration;
	}

	public String getIssuer() {
		return issuer;
	}

	public String getHolder() {
		return holder;
	}

	public Role getRole() {
		return role;
	}
}
