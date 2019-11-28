package Tests;

import static org.junit.Assert.*;

import java.util.Map;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignedObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Server.Server;
import rmi.AuthenticationException;

public class AuthenticationTests {
	Server server;
	@Before
	public void setUp() throws Exception {
		server = new Server();
	}

	public String getSharedPassword() throws NoSuchAlgorithmException
	{
		final String password = "pswd";
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		return new String(md.digest(password.getBytes()));
	}
	@Test
	public void AuthenticationTest() throws Exception {
		final String password = getSharedPassword();
		Map<String, Boolean[]> expectedAccess = new HashMap<String, Boolean[]>();
												//   print  queue  topQueue start  stop   restart stat    readConfig setConfig
		expectedAccess.put("Alice", new Boolean[]	{true,  true,  true,    true,  true,  true,   true,   true,      true});
		expectedAccess.put("Bob", new Boolean[]  	{false, false, false,   true,  true,  true,   true,   true,      true});
		expectedAccess.put("Cecilia", new Boolean[]	{true,  true,  true,    false, false, true,   false,  false,     false});
		expectedAccess.put("David", new Boolean[]	{true,  true,  false,   false, false, false,  false,  false,  	 false});
		expectedAccess.put("Erica", new Boolean[]	{true,  true,  false,   false, false, false,  false,  false,  	 false});
		expectedAccess.put("Fred", new Boolean[]	{true,  true,  false,   false, false, false,  false,  false,  	 false});
		expectedAccess.put("George", new Boolean[]	{true,  true,  false,   false, false, false,  false,  false,  	 false});
		
		for(Map.Entry<String, Boolean[]> conf : expectedAccess.entrySet())
		{
			String username = conf.getKey();
			SignedObject token = server.authenticate(username, password);
			System.out.println("Checking permissions for " + username);
			try
			{
				System.out.println("Testing printing permission for " + username);
				server.print("SomeFile", "SomePrinter", token);
				System.out.println("Granted.");
			}
			catch(AuthenticationException e)
			{
				System.out.println("Denied");
			}
			try
			{
				System.out.println("Testing queue permission for " + username);
				server.queue(token);
				System.out.println("Granted.");
			}
			catch(AuthenticationException e)
			{
				System.out.println("Denied");
			}
			
			try
			{
				System.out.println("Testing topQueue permission for " + username);
				server.topQueue(1, token);
				System.out.println("Granted.");
			}
			catch(AuthenticationException e)
			{
				System.out.println("Denied");
			}
			
			try
			{
				System.out.println("Testing start permission for " + username);
				server.start(token);
				System.out.println("Granted.");
			}
			catch(AuthenticationException e)
			{
				System.out.println("Denied");
			}
			
			try
			{
				System.out.println("Testing stop permission for " + username);
				server.stop(token);
				System.out.println("Granted.");
			}
			catch(AuthenticationException e)
			{
				System.out.println("Denied");
			}

			try
			{
				System.out.println("Testing restart permission for " + username);
				server.restart(token);
				System.out.println("Granted.");
			}
			catch(AuthenticationException e)
			{
				System.out.println("Denied");
			}
			
			try
			{
				System.out.println("Testing stat permission for " + username);
				server.status(token);
				System.out.println("Granted.");
			}
			catch(AuthenticationException e)
			{
				System.out.println("Denied");
			}
			
			try
			{
				System.out.println("Testing stat permission for " + username);
				server.readConfig("SomeParameter", token);
				System.out.println("Granted.");
			}
			catch(AuthenticationException e)
			{
				System.out.println("Denied");
			}
			
			try
			{
				System.out.println("Testing stat permission for " + username);
				server.setConfig("SomeParameter", "value", token);
				System.out.println("Granted.");
			}
			catch(AuthenticationException e)
			{
				System.out.println("Denied");
			}
			
			System.out.println("---");
		}
		
	}

}