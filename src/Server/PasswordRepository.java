package Server;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class PasswordRepository {
	private Connection connection;
	public PasswordRepository(String url, String username, String password) throws ClassNotFoundException, SQLException {
		connection = DriverManager.getConnection(url, username, password);
	}

	
}
