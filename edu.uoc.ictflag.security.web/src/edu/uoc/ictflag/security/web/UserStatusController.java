package edu.uoc.ictflag.security.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import edu.com.ictflag.security.bll.IUserManager;
import edu.uoc.ictflag.core.log.ActivityLog;
import edu.uoc.ictflag.core.log.LogHelper;
import edu.uoc.ictflag.core.web.NavigationHelper;
import edu.uoc.ictflag.core.web.SessionBean;
import edu.uoc.ictflag.core.web.URLHelper;
import edu.uoc.ictflag.core.web.URLHelper.Page;
import edu.uoc.ictflag.security.model.User;
import edu.uoc.ictflag.security.model.UserRole;

@Named
@ViewScoped
public class UserStatusController implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	User user;
	
	@Inject
	IUserManager userManager;
	
	@ActivityLog
	public void changeLocale(String locale) throws Exception
	{
		SessionBean.setAttribute("locale", locale);
		
		FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale(locale));
		
		String username = SessionBean.getUsername();
		
		if (username != null)
		{
			userManager.setUserSelectedLanguage(username, locale);
		}
	}
	
	public List<String> getUserRoles()
	{
		List<String> roles = new ArrayList<String>();
		
		for (UserRole ur : user.getRoles())
		{
			roles.add("Role_" + ur.toString());
		}
		
		return roles;
	}
	
	@ActivityLog
	public void changeUserSelectedRole(String role) throws Exception
	{
		String username = SessionBean.getUsername();
		
		if (username != null)
		{
			if (role.startsWith("Role_"))
			{
				role = role.substring(5);
			}
			
			userManager.setUserSelectedRole(username, UserRole.fromString(role));
			
			user = userManager.getUser(username);
		}
		
		NavigationHelper.redirectToUrl(URLHelper.getRelativePath(Page.HOME_PRIVATE, true));
	}
	
	public String getUserSelectedRole()
	{
		return "Role_" + user.getSelectedRole().toString();
	}
	
	public void initialize()
	{
		String username = SessionBean.getUsername();
		
		if (username != null)
		{
			try
			{
				user = userManager.getUser(username);
				
				String lang = user.getSelectedLanguage();
				
				if (lang != null)
				{
					FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale(lang));
				}
			}
			catch (Exception e)
			{
				LogHelper.error(e);
			}
		}
		
		String locale = (String) SessionBean.getAttribute("locale");
		
		if (locale != null)
		{
			FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale(locale));
		}
	}
}
