package edu.uoc.ictflag.security.dal.test;

import static edu.uoc.ictflag.setup.core.DbSetupHelper.getEntityManager;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.setupUser;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.uoc.ictflag.dal.test.DBTestHelper;
import edu.uoc.ictflag.security.dal.UserRepository;
import edu.uoc.ictflag.security.model.User;
import edu.uoc.ictflag.security.model.UserRole;

public class UserRepositoryTest
{
	private static UserRepository ur;
	
	private static User u1;
	private static User u2;
	
	@BeforeClass
	public static void setUpProgramRepositoryTest() throws Exception
	{
		DBTestHelper.reset();
		
		u1 = setupUser("su", "Super user", "su", "su@uoc.edu", Arrays.asList(UserRole.SUPERUSER));
		u2 = setupUser("pm1", "Program manager 1", "pm1", "pm1@uoc.edu", Arrays.asList(UserRole.PROGRAM_MANAGER));
		
		ur = new UserRepository();
		ur.setEntityManager(getEntityManager());
	}
	
	@Test
	public void getUserTest() throws Exception
	{
		User u = ur.getUserByUsername("su");
		
		assertEquals(u1, u);
		
		u = ur.getUserByUsername("pm1");
		
		assertEquals(u2, u);
	}
	
	@Test
	public void getUserRoleTest() throws Exception
	{
		User ui = ur.getUserByUsername("su");
		
		assertEquals(UserRole.SUPERUSER, ui.getSelectedRole());
		
		ui = ur.getUserByUsername("pm1");
		
		assertEquals(UserRole.PROGRAM_MANAGER, ui.getSelectedRole());
	}
}
