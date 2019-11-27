package Repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

import Server.Policy;

public class RBPolicyRepository implements IPolicyRepository {

	private Connection connection;
	public RBPolicyRepository(String url, String username, String password) throws ClassNotFoundException, SQLException {
		connection = DriverManager.getConnection(url, username, password);
		connection.setCatalog("AuthenticationLab");
	}
	@Override
	public Policy GetPermissions(String subject) {
		CallableStatement call;
		try {
			call = connection.prepareCall("{CALL GetUserGroupPermissions(?)}");
		
		call.setString(1, subject);
		call.execute();
		
		ResultSet result = call.getResultSet();
		final java.sql.ResultSetMetaData meta = call.getMetaData();
		
		boolean first = result.first();
		HashSet<String> grants = new HashSet<String>();
		if(first) {
			// Skip the first two columns as they are not policies
			for (int i = 3; i <= meta.getColumnCount(); i++) {
				String grant = (meta.getColumnLabel(i));
				boolean permission = result.getBoolean(i);
				if(permission)
					grants.add(grant);
			}
			
			// Query the subject from the database for those permissions to verify that we are indeed using the right data
			String targetedSubject = result.getString("Subject");
			return new Policy(grants, targetedSubject);
		}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
