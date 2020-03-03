package edu.uoc.ictflag.security.web;

import java.io.Serializable;
import java.util.Date;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import edu.com.ictflag.security.bll.IAuthenticationManager;
import edu.com.ictflag.security.bll.IUserActivityManager;
import edu.com.ictflag.security.bll.IUserManager;
import edu.uoc.ictflag.core.localization.LocalizationController;
import edu.uoc.ictflag.core.log.ActivityLog;
import edu.uoc.ictflag.core.web.NavigationHelper;
import edu.uoc.ictflag.core.web.SessionBean;
import edu.uoc.ictflag.core.web.URLHelper;
import edu.uoc.ictflag.core.web.URLHelper.Page;
import edu.uoc.ictflag.core.web.ValidationHelper;
import edu.uoc.ictflag.security.model.UserActivity;

@Named
@SessionScoped
public class LoginController implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private IAuthenticationManager authenticationManager;
	
	@Inject
	private IUserManager userManager;
	
	@Inject
	private IUserActivityManager userActivityManager;
	
	private String loginRedirect;
	
	private String username;
	private String password;
	
	public LoginController()
	{
	}
	
	public String getLoginRedirect()
	{
		return loginRedirect;
	}
	
	public void setLoginRedirect(String loginRedirect)
	{
		this.loginRedirect = loginRedirect;
	}
	
	public String getUsername()
	{
		if (this.username == null)
		{
			username = (String) SessionBean.getAttribute("username");
		}
		
		return this.username;
	}
	
	public void setUsername(String username)
	{
		this.username = username;
	}
	
	public String getPassword()
	{
		return "";
	}
	
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	public boolean isUserLogged()
	{
		username = (String) SessionBean.getAttribute("username");
		
		return username != null;
	}
	
	public boolean validateUsernameIdp(String username, String idp) throws Exception
	{
		if (authenticationManager.checkUserIDP(username, idp))
		{
			SessionBean.setAttribute("username", username);
			
			userManager.setUserlastVisit(username, new Date());
			
			userActivityManager.createUserActivity(new UserActivity(username, new Date(), "login CAS success", ""));
			
			return true;
		}
		else
		{
			userActivityManager.createUserActivity(new UserActivity(username, new Date(), "login CAS failed", ""));
			
			return false;
		}
	}
	
	public boolean validateUsernameLti(String username) throws Exception
	{
		if (userManager.getUser(username) != null)
		{
			SessionBean.setAttribute("username", username);
			
			userManager.setUserlastVisit(username, new Date());
			
			userActivityManager.createUserActivity(new UserActivity(username, new Date(), "login LTI success", ""));
			
			return true;
		}
		else
		{
			userActivityManager.createUserActivity(new UserActivity(username, new Date(), "login LTI failed", ""));
			
			return false;
		}
	}
	
	@ActivityLog
	public String validateUsernamePassword() throws Exception
	{
		if (authenticationManager.checkUserLogin(username, password))
		{
			SessionBean.setAttribute("username", username);
			
			userManager.setUserlastVisit(username, new Date());
			
			userActivityManager.createUserActivity(new UserActivity(username, new Date(), "login success", ""));
			
			if (loginRedirect != null && !loginRedirect.isEmpty())
			{
				NavigationHelper.redirectToUrl(loginRedirect);
				
				return null;
			}
			else
			{
				return "private-index";
			}
		}
		else
		{
			userActivityManager.createUserActivity(new UserActivity(username, new Date(), "login failed", ""));
			
			ValidationHelper.addValidationMessage(LocalizationController.getLocalizedString("Please_enter_correct_username_and_password"));
		}
		
		return "login";
	}
	
	@ActivityLog
	public void logout() throws Exception
	{
		userActivityManager.createUserActivity(new UserActivity(SessionBean.getUsername(), new Date(), "logout", ""));
		
		SessionBean.invalidateSession();
		
		NavigationHelper.redirectToUrl(URLHelper.getRelativePath(Page.LOGIN, true));
	}
}