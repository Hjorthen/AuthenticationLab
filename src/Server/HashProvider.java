package Server;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Base64.Encoder;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class HashProvider {
	private SecretKeyFactory hashFactory;
	private Encoder base64Encoder;
	private static final int iterationCount = 10000;
	
	public HashProvider() throws NoSuchAlgorithmException
	{
		hashFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		base64Encoder = Base64.getEncoder();
	}
	
	String GetHash(String password, byte[] salt) throws InvalidKeySpecException
	{
		PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterationCount);
		byte[] hash = hashFactory.generateSecret(spec).getEncoded();
		return new String(base64Encoder.encode(hash));
	}
}
