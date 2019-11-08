package Repository;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

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

public class PasswordRepository implements IPasswordRepository {
	
	private Connection connection;

	
	public PasswordRepository(String url, String username, String password) throws ClassNotFoundException, SQLException {
		connection = DriverManager.getConnection(url, username, password);
		connection.setCatalog("AuthenticationLab");
	}

	@Override
	public boolean AuthenticateUser(String username, String hashedPassword) throws SQLException {
			CallableStatement call = connection.prepareCall("{CALL AuthenticateUser(?, ?)}");
			call.setString(1, username);
			call.setString(2, hashedPassword);
			call.execute();
			
			ResultSet result = call.getResultSet();
			boolean first = result.first();
			
			if(first && result.getBoolean(1)) {
				return true;
			}
			else {
				return false;
			}
	}

	@Override
	public byte[] GetSaltForUser(String username) throws SQLException {
			CallableStatement call = connection.prepareCall("{CALL LookupSalt(?)}");
			call.setString(1,  username);
			//call.registerOutParameter(2, Types.VARCHAR);
			call.execute();
			
			
			ResultSet result = call.getResultSet();
			boolean first = result.first();
			if(!first) {
				return null;
			}
			String salt = result.getString(1);
			if(salt == null)
				return null;
			
			Decoder base64Decoder = Base64.getDecoder();
			return base64Decoder.decode(salt);
	}
}
