package Tests;

import static org.junit.Assert.*;

import java.security.spec.InvalidKeySpecException;

import org.junit.Before;
import org.junit.Test;

import Server.HashProvider;

public class HashProviderTests {
	HashProvider hasher;
	@Before
	public void setUp() throws Exception {
		hasher = new HashProvider();
	}

	@Test
	public void HashProvider_WithSameArguments_CreatesIdenticalHash() throws InvalidKeySpecException {
		String password = "MyPassword123";
		byte[] salt = "MySalt123".getBytes();
		System.err.println(salt.length);
		String hash1 = hasher.GetHash(password, salt);
		String hash2 = hasher.GetHash(password, salt);
		assertEquals(hash1, hash2);
	}
}
