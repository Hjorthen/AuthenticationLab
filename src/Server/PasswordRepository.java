package Server;

import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.AuthenticationException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.SignedObject;
import java.sql.CallableStatement;
import java.sql.Connection;

public class PasswordRepository {
	
	private Connection connection;
	
	public PasswordRepository(String url, String username, String password) throws ClassNotFoundException, SQLException {
		connection = DriverManager.getConnection(url, username, password);
	}

	public boolean CheckCredentials(String username, String hashedPassword) {
		try {
			CallableStatement call = connection.prepareCall("{CALL AuthenticateUser(?, ?)}");
			call.setString(1, username);
			call.setString(2, hashedPassword);
			call.execute();
			
			if(call.getResultSet().getBoolean(0)) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
