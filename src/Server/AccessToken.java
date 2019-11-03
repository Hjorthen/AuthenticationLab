package Server;

import java.io.Serializable;
import java.util.Date;

public class AccessToken implements Serializable{
	String receipient;
	String issuer;
	Date expiration;
	
	public AccessToken() {
		
	}
}
