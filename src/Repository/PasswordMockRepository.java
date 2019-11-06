package Repository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class PasswordMockRepository implements IPasswordRepository {

	private Map<String, String> data;
	private final String salt = "MockSalt";
	
	public PasswordMockRepository(){
		data = new HashMap<String, String>();
		data.put("Test1", "pwd");
		
		
		for(Map.Entry<String, String> entry : data.entrySet())
		{
			entry.setValue(entry.getValue() + salt);
		}
	}

	@Override
	public boolean AuthenticateUser(String username, String hashedPassword) throws SQLException {
			if(!data.containsKey(username))
				return false;
			
			
			return data.get(username) == hashedPassword;
	}

	@Override
	public byte[] GetSaltForUser(String username) throws SQLException {
		return salt.getBytes();
	}
}
