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
		String[] permissions = new String[] {"print", "queue", "topQueue", "start", "stop", "restart", "status", "readConfig", "setConfig" };
		String[] users = new String[] {"Alice", "Bob", "Cecilia", "David", "Erica", "Fred", "George", "Henry", "Ida" };
		for(String username : users)
		{
			ArrayList<Boolean> AccessList = new ArrayList<Boolean>();
			SignedObject token = server.authenticate(username, password);
			System.out.println("Checking permissions for " + username);
			try
			{
				server.print("SomeFile", "SomePrinter", token);
				AccessList.add(true);
			}
			catch(AuthenticationException e)
			{
				AccessList.add(false);
			}
			try
			{
				server.queue(token);
				AccessList.add(true);
			}
			catch(AuthenticationException e)
			{
				AccessList.add(false);
			}
			
			try
			{
				server.topQueue(1, token);
				AccessList.add(true);
			}
			catch(AuthenticationException e)
			{
				AccessList.add(false);
			}
			
			try
			{
				server.start(token);
				AccessList.add(true);
			}
			catch(AuthenticationException e)
			{
				AccessList.add(false);
			}
			
			try
			{
				server.stop(token);
				AccessList.add(true);
			}
			catch(AuthenticationException e)
			{
				AccessList.add(false);
			}

			try
			{
				server.restart(token);
				AccessList.add(true);
			}
			catch(AuthenticationException e)
			{
				AccessList.add(false);
			}
			
			try
			{
				server.status(token);
				AccessList.add(true);
			}
			catch(AuthenticationException e)
			{
				AccessList.add(false);
			}
			
			try
			{
				server.readConfig("SomeParameter", token);
				AccessList.add(true);
			}
			catch(AuthenticationException e)
			{
				AccessList.add(false);
			}
			
			try
			{
				server.setConfig("SomeParameter", "value", token);
				AccessList.add(true);
			}
			catch(AuthenticationException e)
			{
				AccessList.add(false);
			}
			

			System.out.print(username + " had access to: \n");
			for (int i = 0; i < permissions.length; i++) {
				if(AccessList.get(i)) {
					System.out.print(permissions[i] + " ");	
				}
			}
			
			System.out.println("\n---");
		}
		
	}

}