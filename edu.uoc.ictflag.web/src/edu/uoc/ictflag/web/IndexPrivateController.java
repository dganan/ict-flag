package edu.uoc.ictflag.web;

import java.io.Serializable;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import edu.com.ictflag.security.bll.IUserManager;
import edu.uoc.ictflag.core.log.ActivityLog;
import edu.uoc.ictflag.core.log.LogHelper;
import edu.uoc.ictflag.core.web.NavigationHelper;
import edu.uoc.ictflag.core.web.SessionBean;
import edu.uoc.ictflag.security.model.User;
import edu.uoc.ictflag.security.model.UserRole;

@Named
@ViewScoped
public class IndexPrivateController implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private IUserManager userManager;
	

	@ActivityLog
	public void initialize() throws Exception
	{
		try
		{
			//if (!FacesContext.getCurrentInstance().isPostback())
			{
				User u = userManager.getUser(SessionBean.getUsername());
				
				UserRole ur = u.getSelectedRole();
				
				if (ur == UserRole.INSTRUCTOR)
				{
					ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
					context.redirect(context.getRequestContextPath() + "/private/ExercisesReport.xhtml");
				}
				else if (ur == UserRole.STUDENT)
				{
					ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
					context.redirect(context.getRequestContextPath() + "/private/StudentReport.xhtml");
				}
			}
		}
		catch (Exception e)
		{
			LogHelper.error(e);
			
			NavigationHelper.redirectTo404();
		}
	}
}
