package edu.uoc.ictflag.ela.web;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import edu.uoc.ictflag.core.log.LogHelper;
import edu.uoc.ictflag.core.web.ValidationHelper;
import edu.uoc.ictflag.ela.bll.IReportsEtlManager;

@Singleton
@Startup
public class EtlProcessScheduler
{
	@Inject
	IReportsEtlManager reportsEtlManager;
		
	@Schedule(second = "0", minute = "0", hour = "3", persistent = false)
	public void programmedEtlProcess()
	{
		LogHelper.info("Executing programmed ETL process");
		
		try
		{
			reportsEtlManager.startReportsEtlProcess();
		}
		catch (Exception e)
		{
			ValidationHelper.addValidationMessage("An error occurred during the Etl process. Check system logs for more information.");
		}
	}
}
