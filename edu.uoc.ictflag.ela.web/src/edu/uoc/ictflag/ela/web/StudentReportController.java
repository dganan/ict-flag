package edu.uoc.ictflag.ela.web;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import edu.uoc.ictflag.core.localization.LocalizationController;
import edu.uoc.ictflag.core.log.ActivityLog;
import edu.uoc.ictflag.core.log.LogHelper;
import edu.uoc.ictflag.core.web.NavigationHelper;
import edu.uoc.ictflag.core.web.SessionBean;
import edu.uoc.ictflag.ela.bll.IStudentReportManager;
import edu.uoc.ictflag.ela.model.StudentReportDataItem;
import edu.uoc.ictflag.ela.model.StudentReportDataSemaphore;

@Named
@ViewScoped
public class StudentReportController implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Inject
	IStudentReportManager studentReportManager;
	
	protected StudentReportDataItem dataItem;
	
	public StudentReportDataItem getDataItem()
	{
		return dataItem;
	}
	
	public void setDataItem(StudentReportDataItem dataItem)
	{
		this.dataItem = dataItem;
	}
	
	public String getReportDate()
	{
		DateFormat dateFormat = LocalizationController.getCurrentLanguageStatic() == "en" ? new SimpleDateFormat("yyyy/MM/dd")
				: new SimpleDateFormat("dd/MM/yyyy");
		
		Calendar cal = Calendar.getInstance();
		
		cal.add(Calendar.DATE, -1);
		
		return dateFormat.format(cal.getTime());
	}
	
	// IMPLEMENTATION
	
	@ActivityLog
	public void initialize() throws Exception
	{
		try
		{
			//if (!FacesContext.getCurrentInstance().isPostback())
			{
				dataItem = studentReportManager.getReportData(SessionBean.getUsername());
			}
		}
		catch (Exception e)
		{
			LogHelper.error(e);
			
			NavigationHelper.redirectTo404();
		}
	}
	
	public long getExercisesYesterday()
	{
		return dataItem.getExercisesYesterday();
	}
	
	public String getRecentActivity()
	{
		String recentActivity = null;
		
		if (dataItem.getRecentExercises() == 0)
		{
			recentActivity = LocalizationController.getLocalizedString("RecentActivityNone");
		}
		else
		{
			recentActivity = LocalizationController.getLocalizedString("RecentActivityLast48Hours").replace("SOLVED_EXERCISES",
					"" + dataItem.getRecentExercises());
		}
		
		return recentActivity;
	}
	
	public String getLevel1Message()
	{
		return getLevelMessage(0);
	}
	
	public String getLevel2Message()
	{
		return getLevelMessage(1);
	}
	
	public String getLevel3Message()
	{
		return getLevelMessage(2);
	}
	
	public String getLevel4Message()
	{
		return getLevelMessage(3);
	}
	
	public String getLevelMessage(int level)
	{
		String levelMessage = null;
		
		long left = dataItem.getGlobalProgress().getGlobalProgressLevelMaxValue(level) - dataItem.getGlobalProgress().getGlobalProgressCurrentValue();
		
		if (left <= 0)
		{
			levelMessage = LocalizationController.getLocalizedString("NoExercisesLeft").replace("EXERCISES",
					"" + dataItem.getGlobalProgress().getGlobalProgressLevelMaxValue(level));
		}
		else
		{
			long left_2 = (level == 0 ? 0 : dataItem.getGlobalProgress().getGlobalProgressLevelMaxValue(level - 1))
					- dataItem.getGlobalProgress().getGlobalProgressCurrentValue();
			
			if (left_2 > 0)
			{
				levelMessage = "";
			}
			else
			{	
				levelMessage = LocalizationController.getLocalizedString("ExercisesLeft").replace("EXERCISES", "" + left);
			}
		}
		
		return levelMessage;
	}
	
	public int getPercentageLevel1()
	{
		return getPercentageLevel(0);
	}
	
	public int getPercentageLevel2()
	{
		return getPercentageLevel(1);
	}
	
	public int getPercentageLevel3()
	{
		return getPercentageLevel(2);
	}
	
	public int getPercentageLevel4()
	{
		return getPercentageLevel(3);
	}
	
	public String getPercentageLevel1Text()
	{
		return getPercentageLevelText(0);
	}
	
	public String getPercentageLevel2Text()
	{
		return getPercentageLevelText(1);
	}
	
	public String getPercentageLevel3Text()
	{
		return getPercentageLevelText(2);
	}
	
	public String getPercentageLevel4Text()
	{
		return getPercentageLevelText(3);
	}
	
	public String getPercentageLevelText(int l)
	{
		int level = getPercentageLevel(l);
		
		if (level <= 0 || level >= 100)
		{
			return "";
		}
		
		return level + "%";
	}
	
	public int getPercentageLevel(int level)
	{
		int percentage = 0;
		long min = 0;
		long max = 0;
		
		switch (level)
		{
			case 1:
				min = 0;
				max = dataItem.getGlobalProgress().getGlobalProgressLevelMaxValue(level);
				break;
			default:
				min = dataItem.getGlobalProgress().getGlobalProgressLevelMaxValue(level - 1);
				max = dataItem.getGlobalProgress().getGlobalProgressLevelMaxValue(level);
				break;
		}
		
		long currentValue = dataItem.getGlobalProgress().getGlobalProgressCurrentValue();
		
		if (currentValue > min)
		{
			if (currentValue >= max)
			{
				percentage = 100;
			}
			else
			{
				percentage = Math.round((((currentValue - min) * 100) / (max - min)));
			}
		}

		return percentage;
	}
	
	public String getSuggestions()
	{
		String suggestions = null;
		
		String lowestParticipationTool = "VerilCirc";
		long participation = dataItem.getDataForVerilCirc().getCompletedExercises();
		
		if (participation > dataItem.getDataForKeMap().getCompletedExercises())
		{
			lowestParticipationTool = "KeMap";
			participation = dataItem.getDataForKeMap().getCompletedExercises();
		}
		
		if (participation > dataItem.getDataForVerilChart().getCompletedExercises())
		{
			lowestParticipationTool = "VerilChart";
		}
		
		suggestions = LocalizationController.getLocalizedString("StudentReportSuggestions").replace("TOOL", lowestParticipationTool);
		
		return suggestions;
	}
	
	public String semaphoreToColor(StudentReportDataSemaphore semaphore)
	{
		String color = "";
		
		switch (semaphore)
		{
			case Green:
				
				color = "#008A00";
				break;
			
			case Orange:
				
				color = "#FA6800";
				break;
			
			case Red:
				
				color = "#E51400";
				break;
		}
		
		return color;
	}
	
	public String semaphoreToString(StudentReportDataSemaphore semaphore)
	{
		return semaphore.toString().toLowerCase();
	}
	
	public String percentageNormalized(long percentage)
	{
		return "" + Math.min(100, percentage);
	}
	
	public boolean tendenceToEnabled(int tendence)
	{
		// return true;
		return tendence == 0 ? false : true;
	}
	
	public String tendenceToColor(int tendence)
	{
		return tendence > 0 ? "#3c763d" : tendence == 0 ? "#fa6800" : "#a94442";
	}
	
	public String tendenceToIcon(int tendence)
	{
		return tendence > 0 ? "check" : tendence == 0 ? "info-circle" : "warning";
	}
	
	public String tendenceToMessage(int tendence, int exercisesLast)
	{
		String message = "";
		
		if (tendence > 0)
		{
			message = LocalizationController.getLocalizedString("TendencePositive");
		}
		else if (tendence == 0)
		{
			if (exercisesLast == 0)
			{
				message = LocalizationController.getLocalizedString("TendenceZeroWithoutActivity");
			}
			else
			{
				message = LocalizationController.getLocalizedString("TendenceZeroWithActivity");
			}
		}
		else if (tendence < 0)
		{
			message = LocalizationController.getLocalizedString("TendenceNegative");
		}
		
		return message;
	}
}
