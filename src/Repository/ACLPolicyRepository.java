package Repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ACLPolicyRepository implements IPolicyRepository {

	private Connection connection;
	public ACLPolicyRepository(String url, String username, String password) throws ClassNotFoundException, SQLException {
		connection = DriverManager.getConnection(url, username, password);
		connection.setCatalog("AuthenticationLab");
	}
	@Override
	public boolean IsSubjectAuthorized(String subject, String resource) {
		CallableStatement call;
		try {
			call = connection.prepareCall("{CALL IsAuthorized_ACL(?, ?)}");
		
		call.setString(1, subject);
		call.setString(2, resource);
		call.execute();
		
		ResultSet result = call.getResultSet();
		boolean first = result.first();
		
		if(first && result.getBoolean(1)) {
			return true;
		}
		return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

}
