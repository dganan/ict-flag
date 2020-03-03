package edu.uoc.ictflag.ela.web;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import edu.uoc.ictflag.core.log.ActivityLog;
import edu.uoc.ictflag.core.log.LogHelper;
import edu.uoc.ictflag.core.web.NavigationHelper;

@Named
@ViewScoped
public class StudentReportByNameController extends StudentReportController implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String username;
	
	@ActivityLog
	@Override
	public void initialize() throws Exception
	{
		try
		{
			//if (!FacesContext.getCurrentInstance().isPostback())
			{
				dataItem = studentReportManager.getReportData(username);
			}
		}
		catch (Exception e)
		{
			LogHelper.error(e);
			
			NavigationHelper.redirectTo404();
		}
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public void setUsername(String username)
	{
		this.username = username;
	}
}
