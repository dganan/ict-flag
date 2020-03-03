package edu.com.ictflag.security.bll;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import edu.uoc.ictflag.core.log.LogHelper;
import edu.uoc.ictflag.security.dal.IAuthorizationRepository;
import edu.uoc.ictflag.security.dal.IUserRepository;
import edu.uoc.ictflag.security.model.Page;
import edu.uoc.ictflag.security.model.Permission;
import edu.uoc.ictflag.security.model.User;
import edu.uoc.ictflag.security.model.UserRole;
import edu.uoc.ictflag.security.model.UserRolePermissions;

@Stateless
public class AuthorizationManager implements IAuthorizationManager, Serializable
{
	private static final long serialVersionUID = 1L;
	
	IAuthorizationRepository authorizationRepository;
	IUserRepository userRepository;
	
	@Inject
	public AuthorizationManager(IUserRepository userRepository, IAuthorizationRepository authorizationRepository)
	{
		this.userRepository = userRepository;
		this.authorizationRepository = authorizationRepository;
	}
	
	@Override
	public boolean userHasPermission(String username, String module, String entity, Permission permission) throws Exception
	{
		return userHasPermission(userRepository.getUserByUsername(username), module, entity, permission);
	}
	
	@Override
	public boolean userHasPermission(User User, String module, String entity, Permission permission) throws Exception
	{
		boolean hasPermission = false;
		
		if (User != null)
		{
			if (User.getSelectedRole() == UserRole.SUPERUSER)
			{
				hasPermission = true;
			}
			else
			{
				UserRolePermissions urp = authorizationRepository.getUserRolePermissions(User.getSelectedRole(), module, entity);
				
				hasPermission = urp != null && urp.getPermissions().contains(permission);
			}
			
			if (!hasPermission)
			{
				LogHelper.info(
						"ACCESS DENIAL user: " + User.getUsername() + ", module: " + module + ", entity: " + entity + ", permission: "
						+ permission);
			}
		}
		
		return hasPermission;
	}
	
	@Override
	public boolean userHasPageAccess(String username, String pageURL) throws Exception
	{
		boolean hasAccess = false;
		
		User u = userRepository.getUserByUsername(username);
		
		if (u != null)
		{
			if (u.getSelectedRole() == UserRole.SUPERUSER)
			{
				hasAccess = true;
			}
			else
			{
				hasAccess = authorizationRepository.userRolePageAccessExists(u.getSelectedRole(), pageURL);
			}
		}
		
		return hasAccess;
	}
	
	@Override
	public List<Page> getUserMenuPages(String username) throws Exception
	{
		List<Page> pages = new ArrayList<Page>();
		
		User u = userRepository.getUserByUsername(username);
		
		if (u != null)
		{
			if (u.getSelectedRole() == UserRole.SUPERUSER)
			{
				pages = authorizationRepository.getAllMenuPages();
			}
			else
			{
				pages = authorizationRepository.getUserRoleMenuPages(u.getSelectedRole());
			}
		}
		
		return pages;
	}
}
