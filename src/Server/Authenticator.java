package Server;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.SignedObject;

import javax.naming.AuthenticationException;

public class Authenticator {
	
	private KeyPair keys;
	private Signature signature;
	private PasswordRepository passwordRepo;
	
	public Authenticator(PasswordRepository passwordRepository) {
		passwordRepo = passwordRepository;
		KeyPairGenerator keyGenerator;
		
		try {
			keyGenerator = KeyPairGenerator.getInstance("DSA"); //TODO: Decide on algorithm
			keys = keyGenerator.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			signature = Signature.getInstance("SHA1withDSA"); //TODO: Decide on algorithm
			signature.initSign(keys.getPrivate());
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public SignedObject AuthenticateUser(String username, String hashedPassword) throws AuthenticationException {
		if(passwordRepo.CheckCredentials(username, hashedPassword)) {
			//Authenticated -> return a signed token
			//https://wiki.sei.cmu.edu/confluence/display/java/SER02-J.+Sign+then+seal+objects+before+sending+them+outside+a+trust+boundary
			AccessToken token = new AccessToken();
			return SignToken(token);
		}
		else {
			throw new AuthenticationException("No user found with that username or password");
		}
	}
	
	public boolean VerifyToken(SignedObject token) {
		try {
			if(token.verify(keys.getPublic(), signature)) {
				//TODO: Check token contents
				return true;
			}
		} catch (InvalidKeyException | SignatureException e) {
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
