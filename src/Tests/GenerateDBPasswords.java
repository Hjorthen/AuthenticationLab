package Tests;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import org.junit.Before;
import org.junit.Test;

import Server.HashProvider;

public class GenerateDBPasswords {

	HashProvider hasher;
	
	@Before
	public void setUp() throws Exception {
		hasher = new HashProvider();
	}

	@Test
	public void GeneratePasswords() throws InvalidKeySpecException, NoSuchAlgorithmException, UnsupportedEncodingException {
		byte[] salt = "TestSalt".getBytes("UTF-8");
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		String password = "admin";
		String passwordHash = new String(md.digest(password.getBytes()));
		System.out.println("Expected client password: " + passwordHash);
		Encoder base64Encoder = Base64.getEncoder();
		System.out.println("Password: " + hasher.GetHash(passwordHash, salt) + ", salt: " + new String(base64Encoder.encode(salt)));
	}
}
