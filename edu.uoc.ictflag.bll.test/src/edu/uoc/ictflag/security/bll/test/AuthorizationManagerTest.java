package edu.uoc.ictflag.security.bll.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import edu.com.ictflag.security.bll.AuthorizationManager;
import edu.uoc.ictflag.security.dal.AuthorizationRepository;
import edu.uoc.ictflag.security.dal.IAuthorizationRepository;
import edu.uoc.ictflag.security.dal.IUserRepository;
import edu.uoc.ictflag.security.dal.UserRepository;
import edu.uoc.ictflag.security.model.Page;
import edu.uoc.ictflag.security.model.Permission;
import edu.uoc.ictflag.security.model.User;
import edu.uoc.ictflag.security.model.UserRole;
import edu.uoc.ictflag.security.model.UserRolePermissions;

@RunWith(MockitoJUnitRunner.class)
public class AuthorizationManagerTest
{
	IAuthorizationRepository authorizationRepository;
	IUserRepository userRepository;
	
	AuthorizationManager manager;
	
	User ui1;
	User ui2;
	User ui3;
	User ui4;
	User ui5;
	
	private void setupMocks() throws Exception
	{
		authorizationRepository = mock(AuthorizationRepository.class);
		userRepository = mock(UserRepository.class);
		
		manager = new AuthorizationManager(userRepository, authorizationRepository);
		
		setupPermissions(authorizationRepository, userRepository);
	}
	
	private void setupPermissions(IAuthorizationRepository authorizationRepository, IUserRepository userRepository) throws Exception
	{
		ui1 = new User(0L, "su", "Super user", Arrays.asList(UserRole.SUPERUSER));
		ui2 = new User(1L, "admin", "Admin", Arrays.asList(UserRole.ADMIN));
		ui3 = new User(2L, "pm1", "Program manager 1", Arrays.asList(UserRole.PROGRAM_MANAGER));
		ui4 = new User(3L, "pm2", "Program manager 2", Arrays.asList(UserRole.PROGRAM_MANAGER));
		ui5 = new User(4L, "std", "Student", Arrays.asList(UserRole.STUDENT));
		
		UserRolePermissions urp = new UserRolePermissions();
		urp.setRole(UserRole.ADMIN);
		urp.setModule("institution");
		urp.setEntity("Program");
		urp.setPermissions(Arrays.asList(Permission.READ, Permission.EDIT, Permission.DELETE));
		
		when(authorizationRepository.getUserRolePermissions(UserRole.ADMIN, "institution", "Program")).thenReturn(urp);
		
		urp = new UserRolePermissions();
		urp.setRole(UserRole.PROGRAM_MANAGER);
		urp.setModule("institution");
		urp.setEntity("Program");
		urp.setPermissions(Arrays.asList(Permission.READ));
		
		when(authorizationRepository.getUserRolePermissions(UserRole.PROGRAM_MANAGER, "institution", "Program")).thenReturn(urp);
		
		when(userRepository.getUserByUsername("su")).thenReturn(ui1);
		when(userRepository.getUserByUsername("admin")).thenReturn(ui2);
		when(userRepository.getUserByUsername("pm1")).thenReturn(ui3);
		when(userRepository.getUserByUsername("pm2")).thenReturn(ui4);
		when(userRepository.getUserByUsername("std")).thenReturn(ui5);
		
		when(authorizationRepository.userRolePageAccessExists(UserRole.ADMIN, "/private/ProgramsList.xhtml")).thenReturn(true);
		when(authorizationRepository.userRolePageAccessExists(UserRole.ADMIN, "/private/EditProgram.xhtml")).thenReturn(true);
		when(authorizationRepository.userRolePageAccessExists(UserRole.ADMIN, "/private/index.xhtml")).thenReturn(true);
		
		when(authorizationRepository.userRolePageAccessExists(UserRole.PROGRAM_MANAGER, "/private/ProgramsList.xhtml")).thenReturn(true);
		when(authorizationRepository.userRolePageAccessExists(UserRole.PROGRAM_MANAGER, "/private/EditProgram.xhtml")).thenReturn(false);
		when(authorizationRepository.userRolePageAccessExists(UserRole.PROGRAM_MANAGER, "/private/index.xhtml")).thenReturn(true);
		
		when(authorizationRepository.userRolePageAccessExists(UserRole.STUDENT, "/private/ProgramsList.xhtml")).thenReturn(false);
		when(authorizationRepository.userRolePageAccessExists(UserRole.STUDENT, "/private/EditProgram.xhtml")).thenReturn(false);
		when(authorizationRepository.userRolePageAccessExists(UserRole.STUDENT, "/private/index.xhtml")).thenReturn(true);
		
		List<Page> noPage = new ArrayList<Page>();
		List<Page> onePage = new ArrayList<Page>();
		onePage.add(new Page());
		
		when(authorizationRepository.getAllMenuPages()).thenReturn(onePage);
		when(authorizationRepository.getUserRoleMenuPages(UserRole.ADMIN)).thenReturn(onePage);
		when(authorizationRepository.getUserRoleMenuPages(UserRole.STUDENT)).thenReturn(noPage);
	}
	
	@Test
	public void userHasPermissionTest() throws Exception
	{
		// PREPARE TEST
		
		setupMocks();
		
		// RUN TEST
		
		assertTrue(manager.userHasPermission("su", "institution", "Program", Permission.READ));
		assertTrue(manager.userHasPermission("su", "institution", "Program", Permission.EDIT));
		assertTrue(manager.userHasPermission("su", "institution", "Program", Permission.DELETE));

		assertTrue(manager.userHasPermission(ui1, "institution", "Program", Permission.READ));
		assertTrue(manager.userHasPermission(ui1, "institution", "Program", Permission.EDIT));
		assertTrue(manager.userHasPermission(ui1, "institution", "Program", Permission.DELETE));
		
		assertTrue(manager.userHasPermission("admin", "institution", "Program", Permission.READ));
		assertTrue(manager.userHasPermission("admin", "institution", "Program", Permission.EDIT));
		assertTrue(manager.userHasPermission("admin", "institution", "Program", Permission.DELETE));
		
		assertTrue(manager.userHasPermission(ui2, "institution", "Program", Permission.READ));
		assertTrue(manager.userHasPermission(ui2, "institution", "Program", Permission.EDIT));
		assertTrue(manager.userHasPermission(ui2, "institution", "Program", Permission.DELETE));
		
		assertTrue(manager.userHasPermission("pm1", "institution", "Program", Permission.READ));
		assertFalse(manager.userHasPermission("pm1", "institution", "Program", Permission.EDIT));
		assertFalse(manager.userHasPermission("pm1", "institution", "Program", Permission.DELETE));
		
		assertTrue(manager.userHasPermission(ui3, "institution", "Program", Permission.READ));
		assertFalse(manager.userHasPermission(ui3, "institution", "Program", Permission.EDIT));
		assertFalse(manager.userHasPermission(ui3, "institution", "Program", Permission.DELETE));
		
		assertFalse(manager.userHasPermission("std", "institution", "Program", Permission.READ));
		assertFalse(manager.userHasPermission("std", "institution", "Program", Permission.EDIT));
		assertFalse(manager.userHasPermission("std", "institution", "Program", Permission.DELETE));
		
		assertFalse(manager.userHasPermission(ui5, "institution", "Program", Permission.READ));
		assertFalse(manager.userHasPermission(ui5, "institution", "Program", Permission.EDIT));
		assertFalse(manager.userHasPermission(ui5, "institution", "Program", Permission.DELETE));
	}
	
	@Test
	public void userHasPageAccessTest() throws Exception
	{
		// PREPARE TEST
		
		setupMocks();
		
		// RUN TEST

		assertTrue(manager.userHasPageAccess("su", "/private/ProgramsList.xhtml"));
		assertTrue(manager.userHasPageAccess("su", "/private/EditProgram.xhtml"));
		assertTrue(manager.userHasPageAccess("su", "/private/index.xhtml"));
		
		assertTrue(manager.userHasPageAccess("admin", "/private/ProgramsList.xhtml"));
		assertTrue(manager.userHasPageAccess("admin", "/private/EditProgram.xhtml"));
		assertTrue(manager.userHasPageAccess("admin", "/private/index.xhtml"));
		
		assertTrue(manager.userHasPageAccess("pm1", "/private/ProgramsList.xhtml"));
		assertFalse(manager.userHasPageAccess("pm1", "/private/EditProgram.xhtml"));
		assertTrue(manager.userHasPageAccess("pm1", "/private/index.xhtml"));
		
		assertFalse(manager.userHasPageAccess("std", "/private/ProgramsList.xhtml"));
		assertFalse(manager.userHasPageAccess("std", "/private/EditProgram.xhtml"));
		assertTrue(manager.userHasPageAccess("std", "/private/index.xhtml"));
	}
	
	@Test
	public void getUserMenuPagesTest() throws Exception
	{
		// PREPARE TEST
		
		setupMocks();
		
		// RUN TEST
		
		assertEquals(1, manager.getUserMenuPages("su").size());
		
		assertEquals(1, manager.getUserMenuPages("admin").size());
		
		assertEquals(0, manager.getUserMenuPages("std").size());
		
		assertEquals(0, manager.getUserMenuPages("not-exists").size());
	}
}
