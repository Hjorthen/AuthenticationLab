package Server;

import java.io.Serializable;
import java.time.Duration;
import java.util.Date;

public class AccessToken implements Serializable{

	long timestamp;
	long expiration;
	String issuer;
	String holder;
	
	public AccessToken(String issuer, String holder, long lifeSpan) {
		timestamp = new Date().getTime();
		expiration = timestamp + Duration.ofHours(lifeSpan).toMillis();
		this.issuer = issuer;
		this.holder = holder;
	}
}
