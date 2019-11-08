package Tests;

import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;
import java.security.SignedObject;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;


import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import Repository.PasswordMockRepository;
import Server.Authenticator;
import Server.HashProvider;
import rmi.AuthenticationException;

public class PasswordStorageTests {
	PasswordMockRepository repository;
	Authenticator authenticator;
	HashProvider hashProvider;
	private final byte[] TestSalt = "MyTestSalt".getBytes();
	private final String TestPassword = "MyTestPassword";
	private final String TestUser = "MyTestUser";
	private final String WrongPassword = "NotMyTestPassword";
	private final String WrongUser = "NotMyTestUser";
	
	@Before
	public void setUp() throws Exception {
		hashProvider = new HashProvider();
		repository = new PasswordMockRepository();
		authenticator = new Authenticator(repository, "Test Run", 1);
		
		String hash = hashProvider.GetHash(TestPassword, TestSalt);
		repository.AddUser(TestUser, hash, TestSalt);
	}

	@Test
	public void AuthenticateUser_WithCorrectCredentials_GeneratesValidToken() throws InvalidKeySpecException, SQLException, rmi.AuthenticationException {
		SignedObject obj = authenticator.AuthenticateUser(TestUser, TestPassword);
		assertNotNull(obj);
		assertTrue(authenticator.VerifyToken(obj));
	}
 	
	@Test
	@Parameters()
	public void AuthenticateUser_WithWrongCredentials_GeneratesNoToken() throws AuthenticationException, InvalidKeySpecException, SQLException {
		try {
			SignedObject obj = authenticator.AuthenticateUser(TestUser, WrongPassword);
			fail("Authentication did not throw");
		} catch (Exception e) {
			// Expection is expected
		}
		
		try {
			SignedObject obj = authenticator.AuthenticateUser(WrongUser, WrongPassword);
			fail("Authentication did not throw");
		} catch (Exception e) {
			// Expection is expected
		}
		
		try {
			SignedObject obj = authenticator.AuthenticateUser(WrongUser, TestPassword);
			fail("Authentication did not throw");
		} catch (Exception e) {
			// Expection is expected
		}
	}
	

	@Test
	public void VerifyToken_WithNoToken_DoesNotAuthenticate() throws AuthenticationException, InvalidKeySpecException, SQLException {
		assertFalse(authenticator.VerifyToken(null));
	}

}
