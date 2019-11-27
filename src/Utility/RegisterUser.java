package Utility;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Scanner;

import Repository.IPolicyRepository;
import Repository.PasswordRepository;
import Server.Config;
import Server.HashProvider;

class RegisterUser {
	public static final String CONFIG_PATH = "config.properties";
	private static Connection connection;
	private static Encoder base64Encoder = Base64.getEncoder();
	
	private static Boolean RegisterUser(String username, byte[] salt, String password, String role) throws SQLException, Exception
	{
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		HashProvider hasher = new HashProvider();

		String passwordHash = new String(md.digest(password.getBytes()));
		passwordHash = hasher.GetHash(passwordHash, salt);
		System.out.println("Expected client password: " + passwordHash);
		System.out.println("Password: " + passwordHash + ", salt: " + new String(base64Encoder.encode(salt)));
		
		CallableStatement call = connection.prepareCall("{CALL RegisterAccount(?, ?, ?, ?, ?)}");
		call.setString(1, username);
		call.setString(2, passwordHash);
		call.setString(3, new String(base64Encoder.encode(salt)));
		call.setString(4, role);
		call.registerOutParameter(5, java.sql.Types.INTEGER);
		return call.execute();
	}
	
	private static Boolean VerifyRole(String role) throws SQLException
	{
		String query = "select COUNT(*) as count from Role where Title = " + role + ";";
		Statement statement = connection.createStatement();
		ResultSet result = statement.executeQuery(query);
		if(result.next())
		{
			return result.getBoolean(1);
		}
		return false;
	}
	public static void main(String[] args) throws Exception {
		String url = "jdbc:mysql://localhost:3306/";
		String uname = "root";
		String pwd = "authlab19";
		PasswordRepository passwordRepo = new PasswordRepository(url, uname, pwd);
		connection = DriverManager.getConnection(url, uname, pwd);
		connection.setCatalog("AuthenticationLab");
		Boolean repeat = true;
		Scanner sc = new Scanner(System.in);
		byte[] salt = "testSalt".getBytes("UTF-8");
		
		RegisterUser("Alice", salt, "pswd", "Admin");
		RegisterUser("Bob", salt, "pswd", "ServiceTechnician");
		RegisterUser("Cecilia", salt, "pswd", "poweruser");
		RegisterUser("David", salt, "pswd", "User");
		RegisterUser("Erica", salt, "pswd", "User");
		RegisterUser("Fred", salt, "pswd", "User");
		RegisterUser("George", salt, "pswd", "User");
		
		/*while(repeat)
		{
			String username, password, role;
			System.out.println("Enter Username:");
			username = sc.next();
			System.out.println("Enter Password:");
			password = sc.next();
			boolean validRole;
			do
			{
				System.out.println("Enter Role:");
				role = sc.next();
				validRole = VerifyRole(role);
				if(!validRole)
					System.out.println("Invalid Role. Try again.");
			}while(!validRole);
			
			if(RegisterUser(username, salt, password, role))
			{
				System.out.println("Registration Success");
			}
			else
			{
				System.out.println("Registration Failed");
			}
		}*/
		
	}

}
