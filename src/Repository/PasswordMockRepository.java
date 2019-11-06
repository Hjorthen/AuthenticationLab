package Repository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class PasswordMockRepository implements IPasswordRepository {

	private Map<String, String> data;
	private Map<String, byte[]> salts;
	
	public PasswordMockRepository(){
		data = new HashMap<String, String>();
		salts = new HashMap<String, byte[]>();
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
