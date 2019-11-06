package Repository;

import java.sql.SQLException;

public interface IPasswordRepository {

	boolean AuthenticateUser(String username, String hashedPassword) throws SQLException;
	byte[] GetSaltForUser(String username) throws SQLException;
}