package Server;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SealedObject;
import javax.naming.AuthenticationException;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;

public class PasswordRepository {
	private Connection connection;
	public PasswordRepository(String url, String username, String password) throws ClassNotFoundException, SQLException {
		connection = DriverManager.getConnection(url, username, password);
	}

	
	public void AuthenticateUser(String username, String hashedPassword) throws AuthenticationException {
		try {
			CallableStatement call = connection.prepareCall("{CALL AuthenticateUser(?, ?)}");
			call.setString(1, username);
			call.setString(2, hashedPassword);
			call.execute();
			
			if(call.getResultSet().getBoolean(0)) {
				//Authenticated
				AccessToken token = new AccessToken();
				try {
					SealedObject so = new SealedObject(token, null);
				} catch (IllegalBlockSizeException | IOException e) {
					e.printStackTrace();
				}
			}
			else {
				throw new AuthenticationException("No user found with that username or password");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
