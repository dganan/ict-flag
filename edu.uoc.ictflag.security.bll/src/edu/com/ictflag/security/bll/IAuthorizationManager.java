package edu.com.ictflag.security.bll;

import java.util.List;

import edu.uoc.ictflag.security.model.Page;
import edu.uoc.ictflag.security.model.Permission;
import edu.uoc.ictflag.security.model.User;

public interface IAuthorizationManager
{
	boolean userHasPermission(String username, String module, String entity, Permission permission) throws Exception;
	
	boolean userHasPermission(User User, String module, String entity, Permission permission) throws Exception;
	
	boolean userHasPageAccess(String username, String pageURL) throws Exception;
	
	List<Page> getUserMenuPages(String username) throws Exception;
}
