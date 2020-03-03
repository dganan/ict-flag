package edu.uoc.ictflag.security.dal;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Random;

import edu.uoc.ictflag.security.model.SecurePassword;

public class PasswordHasher
{
	public static boolean verifyPassword(String password, SecurePassword securePassword) throws Exception
	{
		SecurePassword sp = getSecurePassword (password, securePassword.getSalt());
				
		return sp.getPassword().equals(securePassword.getPassword());
	}
	
	public static SecurePassword generateSecurePassword() throws Exception
	{
		String password = generatePassword();
		
		String salt = getSalt();
		
		return getSecurePassword(password, salt);
	}
	
	public static SecurePassword getSecurePassword(String password) throws Exception
	{
		String salt = getSalt();
		
		return getSecurePassword(password, salt);
	}
	
	private static SecurePassword getSecurePassword(String password, String salt) throws Exception
	{
		SecurePassword securePassword = new SecurePassword();

		securePassword.setSalt(salt);

		// Create MessageDigest instance for MD5
		MessageDigest md = MessageDigest.getInstance("MD5");
		
		// Add password bytes to digest
		md.update(salt.getBytes());
		
		// Get the hash's bytes
		byte[] bytes = md.digest(password.getBytes());
		
		// This bytes[] has bytes in decimal format;
		// Convert it to hexadecimal format
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < bytes.length; i++)
		{
			sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		
		// Get complete hashed password in hex format
		securePassword.setPassword(sb.toString());
		
		return securePassword;
	}
	
	// Add salt
	private static String getSalt() throws Exception
	{
		// Always use a SecureRandom generator
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
		
		// Create array for salt
		byte[] salt = new byte[16];
		
		// Get a random salt
		sr.nextBytes(salt);
		
		// return salt
		return salt.toString();
	}
	
	private static final Random random = new SecureRandom();
	
	private static final int PASSWORD_LENGTH = 8;
	
	private static String letters = "abcdefghjklmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ0123456789+@_!";
	
	private static String generatePassword()
	{
		String pw = "";
		
		for (int i = 0; i < PASSWORD_LENGTH; i++)
		{
			int index = (int) (random.nextDouble() * letters.length());
			
			pw += letters.substring(index, index + 1);
		}
		
		return pw;
	}
}
