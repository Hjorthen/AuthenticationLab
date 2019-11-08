package Repository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import Server.HashProvider;

public class PasswordMockRepository implements IPasswordRepository {

	private Map<String, String> data;
	private Map<String, byte[]> salts;
	
	public PasswordMockRepository() throws NoSuchAlgorithmException, InvalidKeySpecException{
		data = new HashMap<String, String>();
		salts = new HashMap<String, byte[]>();
		HashProvider hasher = new HashProvider();
		byte[] salt = "TestSalt".getBytes();
		
		// Setup a test user which can be used when connecting from client
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		String username = "MockUser";
		String password = "MockPassword";
		String passwordHash = md.digest(password.getBytes()).toString();
		System.out.println("TestPasswordHash: " + passwordHash);
		AddUser(username, hasher.GetHash(passwordHash, salt), salt);
	}
	
	public void AddUser(String username, String hashedPassword, byte[] salt)
	{
		data.put(username, hashedPassword);
		salts.put(username, salt);
	}

	@Override
	public boolean AuthenticateUser(String username, String hashedPassword) throws SQLException {
			if(!data.containsKey(username))
				return false;
					
			return data.get(username).equals(hashedPassword);
	}

	@Override
	public byte[] GetSaltForUser(String username) throws SQLException {
		return salts.get(username);
	}
}
