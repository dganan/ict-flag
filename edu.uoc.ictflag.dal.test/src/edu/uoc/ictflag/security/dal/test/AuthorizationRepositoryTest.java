package edu.uoc.ictflag.security.dal.test;

import static edu.uoc.ictflag.setup.core.DbSetupHelper.getEntityManager;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.setupPage;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.setupRolePageAccess;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.setupRolePermission;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.uoc.ictflag.core.localization.LocalizedString;
import edu.uoc.ictflag.dal.test.DBTestHelper;
import edu.uoc.ictflag.security.dal.AuthorizationRepository;
import edu.uoc.ictflag.security.model.Page;
import edu.uoc.ictflag.security.model.Permission;
import edu.uoc.ictflag.security.model.UserRole;
import edu.uoc.ictflag.security.model.UserRolePermissions;

public class AuthorizationRepositoryTest
{
	private static AuthorizationRepository ar;
	
	@BeforeClass
	public static void setUpProgramRepositoryTest() throws Exception
	{
		DBTestHelper.reset();
		
		setupRolePermission("Program", "institution", UserRole.ADMIN, Arrays.asList(Permission.READ, Permission.EDIT, Permission.DELETE));
		setupRolePermission("Program", "institution", UserRole.PROGRAM_MANAGER, Arrays.asList(Permission.READ, Permission.EDIT));
		
		Page p1 = setupPage("ProgramsList", "Admin", "institution",
				LocalizedString.fromStringFormat("en#Programs list;ca#Llista de programes;es#Lista de programas"), "private/ProgramsList.xhtml",
				"user-plus", 0, true);
		Page p2 = setupPage("EditProgram", "Admin", "institution",
				LocalizedString.fromStringFormat("en#Edit program;ca#Editar programa;es#Editar programa"), "private/EditProgram.xhtml", "", -1,
				false);
		
		setupRolePageAccess(p1, UserRole.ADMIN);
		setupRolePageAccess(p1, UserRole.PROGRAM_MANAGER);
		setupRolePageAccess(p2, UserRole.ADMIN);
		setupRolePageAccess(p2, UserRole.PROGRAM_MANAGER);
		
		ar = new AuthorizationRepository();
		ar.setEntityManager(getEntityManager());
	}
	
	@Test
	public void getUserRolePermissionsTest() throws Exception
	{
		UserRolePermissions perm = ar.getUserRolePermissions(UserRole.SUPERUSER, "institution", "Program");
		
		// There are no permissions in database for superuser. The BLL is who gives full access to this role
		assertNull(perm);
		
		perm = ar.getUserRolePermissions(UserRole.PROGRAM_MANAGER, "institution", "Program");
		
		assertTrue(perm.getPermissions().contains(Permission.READ));
		assertTrue(perm.getPermissions().contains(Permission.EDIT));
		assertFalse(perm.getPermissions().contains(Permission.DELETE));
		
		perm = ar.getUserRolePermissions(UserRole.LECTURER, "institution", "Program");
		
		assertNull(perm);
	}
	
	@Test
	public void userRolePageAccessExistsTest() throws Exception
	{
		// There are no page access in database for superuser. The BLL is who gives full access to this role
		assertFalse(ar.userRolePageAccessExists(UserRole.SUPERUSER, "private/ProgramsList.xhtml"));
		assertFalse(ar.userRolePageAccessExists(UserRole.SUPERUSER, "private/EditProgram.xhtml"));
		
		assertTrue(ar.userRolePageAccessExists(UserRole.PROGRAM_MANAGER, "private/ProgramsList.xhtml"));
		assertTrue(ar.userRolePageAccessExists(UserRole.PROGRAM_MANAGER, "private/EditProgram.xhtml"));
		assertFalse(ar.userRolePageAccessExists(UserRole.LECTURER, "private/ProgramsList.xhtml"));
		assertFalse(ar.userRolePageAccessExists(UserRole.LECTURER, "private/EditProgram.xhtml"));
	}
	
	@Test
	public void getUserRoleMenuPagesTest() throws Exception
	{
		List<Page> pages = ar.getUserRoleMenuPages(UserRole.ADMIN);
		
		assertEquals(1, pages.size());
		
		pages = ar.getUserRoleMenuPages(UserRole.STUDENT);
		
		assertEquals(0, pages.size());
	}
	
	@Test
	public void getAllMenuPagesTest() throws Exception
	{
		List<Page> pages = ar.getAllMenuPages();
		
		assertEquals(1, pages.size());
	}
}
