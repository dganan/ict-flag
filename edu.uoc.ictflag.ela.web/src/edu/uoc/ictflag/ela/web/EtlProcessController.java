package edu.uoc.ictflag.ela.web;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import edu.uoc.ictflag.core.log.ActivityLog;
import edu.uoc.ictflag.core.log.LogHelper;
import edu.uoc.ictflag.core.web.NavigationHelper;
import edu.uoc.ictflag.core.web.ValidationHelper;
import edu.uoc.ictflag.ela.bll.IReportsEtlManager;

@Named
@ViewScoped
public class EtlProcessController implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private boolean success;
	
	@Inject
	IReportsEtlManager reportsEtlManager;
	
	@ActivityLog
	public void initialize() throws Exception
	{
		try
		{
		}
		catch (Exception e)
		{
			LogHelper.error(e);
			
			NavigationHelper.redirectTo404();
		}
	}
	
	public void executeEtl()
	{
		try
		{
			success = false;
			
			reportsEtlManager.startReportsEtlProcess();
			
			success = true;
		}
		catch (Exception e)
		{
			ValidationHelper.addValidationMessage("An error occurred during the Etl process. Check system logs for more information.");
		}
	}
	
	public boolean isSuccess()
	{
		return success;
	}
	
	public void setSuccess(boolean success)
	{
		this.success = success;
	}
}
