package edu.uoc.ictflag.security.web.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import edu.uoc.ictflag.security.dal.PasswordHasher;
import edu.uoc.ictflag.security.model.SecurePassword;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationManagerTest
{
	@Test
	public void passwordHasherTest() throws Exception
	{
		// RUN TEST

		SecurePassword sp1 = PasswordHasher.getSecurePassword("su1");
		SecurePassword sp2 = PasswordHasher.getSecurePassword("su2");
		
		assertTrue(PasswordHasher.verifyPassword("su1", sp1));
		assertFalse(PasswordHasher.verifyPassword("su1", sp2));
		
		assertTrue(PasswordHasher.verifyPassword("su2", sp2));
		assertFalse(PasswordHasher.verifyPassword("su2", sp1));
		
		SecurePassword sp3 = PasswordHasher.getSecurePassword("");
		
		assertTrue(PasswordHasher.verifyPassword("", sp3));
		
		try
		{
			PasswordHasher.getSecurePassword(null);
			
			fail("null passwors should provoke a NullPointerException");
		}
		catch (NullPointerException e)
		{
		
		}
		
		sp3 = PasswordHasher.getSecurePassword(
				"very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very long password");
		assertTrue(PasswordHasher.verifyPassword(
				"very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very long password",
				sp3));
				
		sp3 = PasswordHasher.getSecurePassword("01AaB!@#&%_;:?¿");
		assertTrue(PasswordHasher.verifyPassword("01AaB!@#&%_;:?¿", sp3));
	}
}
