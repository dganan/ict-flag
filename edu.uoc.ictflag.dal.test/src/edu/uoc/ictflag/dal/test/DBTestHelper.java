package edu.uoc.ictflag.dal.test;

import static edu.uoc.ictflag.setup.core.DbSetupHelper.setupPage;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.setupRolePageAccess;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.setupRolePermission;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import edu.uoc.ictflag.core.localization.LocalizedString;
import edu.uoc.ictflag.security.model.Page;
import edu.uoc.ictflag.security.model.Permission;
import edu.uoc.ictflag.security.model.UserRole;
import edu.uoc.ictflag.setup.core.DbSetupHelper;

public class DBTestHelper
{
	private static EntityManagerFactory emf;
	private static EntityManager em;
	
	public static void reset()
	{
		reset(true);
	}
	
	public static void reset(boolean recreate)
	{
		if (em != null)
		{
			em.close();
		}
		
		if (emf != null)
		{
			emf.close();
		}
		
		if (!recreate)
		{
			Map<String, String> overridenProperties = new HashMap<String, String>();
			
			overridenProperties.put("javax.persistence.schema-generation.database.action", "create");
		
			emf = Persistence.createEntityManagerFactory("edu.uoc.ictflag.dal.test", overridenProperties);
		}
		else
		{
			emf = Persistence.createEntityManagerFactory("edu.uoc.ictflag.dal.test");
		}
		
		em = emf.createEntityManager();
		
		DbSetupHelper.setEntityManager(em);
	}
	
	public static void setupPermissions() throws Exception
	{
		setupRolePermission("Program", "institution", UserRole.ADMIN, Arrays.asList(Permission.READ, Permission.EDIT, Permission.DELETE));
		setupRolePermission("Program", "institution", UserRole.PROGRAM_MANAGER, Arrays.asList(Permission.READ));
		
		Page p1 = setupPage("ProgramsList", "Admin", "institution",
				LocalizedString.fromStringFormat("en#Programs list;ca#Llista de programes;es#Lista de programas"), "private/ProgramsList.xhtml",
				"fa fa-user-plus", 0, true);
		Page p2 = setupPage("EditProgram", "Admin", "institution",
				LocalizedString.fromStringFormat("en#Edit program;ca#Editar programa;es#Editar programa"), "private/EditProgram.xhtml", "", -1,
				false);
				
		setupRolePageAccess(p1, UserRole.ADMIN);
		setupRolePageAccess(p1, UserRole.PROGRAM_MANAGER);
		setupRolePageAccess(p2, UserRole.ADMIN);
	}
}
