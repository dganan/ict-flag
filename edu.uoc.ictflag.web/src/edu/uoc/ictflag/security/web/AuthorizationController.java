package edu.uoc.ictflag.security.web;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import edu.com.ictflag.security.bll.IAuthorizationManager;
import edu.uoc.ictflag.core.web.SessionBean;
import edu.uoc.ictflag.security.model.Permission;

@Named
@SessionScoped
public class AuthorizationController implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private IAuthorizationManager authorizationManager;
	
	public AuthorizationController()
	{
	}
	
	public boolean userCanEdit(String module, String entity) throws Exception
	{
		return authorizationManager.userHasPermission(SessionBean.getUsername(), module, entity, Permission.EDIT);
	}
	
	public boolean userCanRead(String module, String entity) throws Exception
	{
		return authorizationManager.userHasPermission(SessionBean.getUsername(), module, entity, Permission.READ);
	}
	
	public boolean userCanDelete(String module, String entity) throws Exception
	{
		return authorizationManager.userHasPermission(SessionBean.getUsername(), module, entity, Permission.DELETE);
	}
}