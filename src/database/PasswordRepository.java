package database;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class PasswordRepository {
	private Connection connection;
	public PasswordRepository() throws ClassNotFoundException, SQLException {
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "authlab19");
		
	}
	
	
	
}
