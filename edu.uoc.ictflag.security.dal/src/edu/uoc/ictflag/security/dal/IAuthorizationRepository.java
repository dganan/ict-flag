package edu.uoc.ictflag.security.dal;

import java.util.List;

import edu.uoc.ictflag.security.model.Page;
import edu.uoc.ictflag.security.model.UserRole;
import edu.uoc.ictflag.security.model.UserRolePageAccess;
import edu.uoc.ictflag.security.model.UserRolePermissions;

public interface IAuthorizationRepository
{
	UserRolePermissions getUserRolePermissions(UserRole role, String module, String entity) throws Exception;
	
	boolean userRolePageAccessExists(UserRole role, String pageURL) throws Exception;
	
	void createPermission(UserRolePermissions u) throws Exception;
	
	void createAccess(UserRolePageAccess u) throws Exception;
	
	List<Page> getAllMenuPages() throws Exception;
	
	List<Page> getUserRoleMenuPages(UserRole role) throws Exception;
}
