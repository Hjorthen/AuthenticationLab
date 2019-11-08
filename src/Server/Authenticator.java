package Server;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.SignedObject;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.Date;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import Repository.IPasswordRepository;
import rmi.AuthenticationException;

public class Authenticator {
	
	private KeyPair keys;
	private Signature signature;
	private HashProvider hashProvider;
	private IPasswordRepository passwordRepo;
	private int tokenExpirationHours;
	private String serverName;
	
	public Authenticator(IPasswordRepository passwordRepository, String serverName, int tokenExpirationHours) throws NoSuchAlgorithmException {
		passwordRepo = passwordRepository;
		this.tokenExpirationHours = tokenExpirationHours;
		this.serverName = serverName;
		KeyPairGenerator keyGenerator;
		hashProvider = new HashProvider();
				
		try {
			keyGenerator = KeyPairGenerator.getInstance("DSA"); 
			keys = keyGenerator.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			signature = Signature.getInstance("SHA256withDSA");
            signature.initSign(keys.getPrivate());
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public SignedObject AuthenticateUser(String username, String userPassword) throws AuthenticationException, InvalidKeySpecException, SQLException {
		byte[] salt = passwordRepo.GetSaltForUser(username);
		if(salt != null && passwordRepo.AuthenticateUser(username, hashProvider.GetHash(userPassword, salt))) {
			//Authenticated -> return a signed token
			//https://wiki.sei.cmu.edu/confluence/display/java/SER02-J.+Sign+then+seal+objects+before+sending+them+outside+a+trust+boundary
			AccessToken token = new AccessToken(serverName, username, tokenExpirationHours);
			return SignToken(token);
		}
		else
		{
			throw new AuthenticationException("No user found with that username or password");
		}
	}
	
	public boolean VerifyToken(SignedObject token) {
			if(token == null)
				return false;
		try {
			if(token.verify(keys.getPublic(), signature)) {
				AccessToken accessToken = (AccessToken)token.getObject();
				long currentTime = new Date().getTime();
				//Check if token is expired
				if(currentTime > accessToken.timestamp && currentTime < accessToken.expiration) {
					return true;
				}
			}
		} catch (InvalidKeyException | SignatureException | ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private SignedObject SignToken(AccessToken token) {
		try {			
			return new SignedObject(token, keys.getPrivate(), signature);
		} catch (InvalidKeyException | SignatureException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
