package edu.uoc.ictflag.ela.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import edu.uoc.ictflag.core.dal.SearchParameters;
import edu.uoc.ictflag.core.dal.SortArgument;
import edu.uoc.ictflag.core.localization.LocalizationController;
import edu.uoc.ictflag.core.log.ActivityLog;
import edu.uoc.ictflag.core.log.LogHelper;
import edu.uoc.ictflag.core.web.NavigationHelper;
import edu.uoc.ictflag.core.web.SessionBean;
import edu.uoc.ictflag.ela.bll.IStudentsReportManager;
import edu.uoc.ictflag.ela.model.FilterField;
import edu.uoc.ictflag.ela.model.StudentsReportDataItem;
import edu.uoc.ictflag.security.web.UserStatusController;

@Named
@ViewScoped
public class StudentsReportController extends ReportController<StudentsReportDataItem> implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Inject
	IStudentsReportManager studentsReportManager;
	
	@Inject
	UserStatusController userStatusController;
	
	@Override
	protected IStudentsReportManager getReportManager()
	{
		return studentsReportManager;
	}
	
	// PRIVATE FIELDS 
	
	private double maxHighestFailureRate;
	private long maxLowestNumberOfRightExercises;
	private long maxHighestNumberOfAttempts;
	
	private int highestFailureRateBarChartMinWidth;
	private int lowestNumberOfRightExercisesBarChartMinWidth;
	private int highestNumberOfAttemptsBarChartMinWidth;
	
	private List<StudentsReportDataItem> highestFailureRateStudents;
	private List<StudentsReportDataItem> lowestNumberOfRightExercisesStudents;
	private List<StudentsReportDataItem> highestNumberOfAttemptsStudents;
	
	private BarChartModel highestFailureRateBarChartModel;
	private BarChartModel lowestNumberOfRightExercisesBarChartModel;
	private BarChartModel highestNumberOfAttemptsBarChartModel;
	
	private ChartSeries highestFailureRateSeries;
	private ChartSeries lowestNumberOfRightExercisesSeries;
	private ChartSeries highestNumberOfAttemptsSeries;

	protected List<String> groupFieldsStudents;
	protected int maxStudents = 10;
	protected boolean showAllStudents;
	
	private boolean canShowHighestFailureRateBarChart;
	private boolean canShowLowestNumberOfRightExercisesBarChart;
	private boolean canShowHighestNumberOfAttemptsBarChart;
	
	// GETTERS AND SETTERS 

	public BarChartModel getHighestFailureRateBarChartModel()
	{
		return highestFailureRateBarChartModel;
	}
	
	public BarChartModel getLowestNumberOfRightExercisesBarChartModel()
	{
		return lowestNumberOfRightExercisesBarChartModel;
	}
	
	public BarChartModel getHighestNumberOfAttemptsBarChartModel()
	{
		return highestNumberOfAttemptsBarChartModel;
	}
	
	public int getHighestFailureRateBarChartMinWidth()
	{
		return highestFailureRateBarChartMinWidth;
	}
	
	public int getLowestNumberOfRightExercisesBarChartMinWidth()
	{
		return lowestNumberOfRightExercisesBarChartMinWidth;
	}
	
	public int getHighestNumberOfAttemptsBarChartMinWidth()
	{
		return highestNumberOfAttemptsBarChartMinWidth;
	}

	public int getMaxStudents()
	{
		return maxStudents;
	}
	
	public void setMaxStudents(int maxStudents)
	{
		this.maxStudents = maxStudents;
	}
	
	public boolean getShowAllStudents()
	{
		return showAllStudents;
	}
	
	public void setShowAllStudents(boolean showAllStudents)
	{
		this.showAllStudents = showAllStudents;
	}
	
	public boolean isCanShowHighestFailureRateBarChart()
	{
		return canShowHighestFailureRateBarChart;
	}
	
	public void setCanShowHighestFailureRateBarChart(boolean canShowHighestFailureRateBarChart)
	{
		this.canShowHighestFailureRateBarChart = canShowHighestFailureRateBarChart;
	}
	
	public boolean isCanShowLowestNumberOfRightExercisesBarChart()
	{
		return canShowLowestNumberOfRightExercisesBarChart;
	}
	
	public void setCanShowLowestNumberOfRightExercisesBarChart(boolean canShowLowestNumberOfRightExercisesBarChart)
	{
		this.canShowLowestNumberOfRightExercisesBarChart = canShowLowestNumberOfRightExercisesBarChart;
	}
	
	public boolean isCanShowHighestNumberOfAttemptsBarChart()
	{
		return canShowHighestNumberOfAttemptsBarChart;
	}
	
	public void setCanShowHighestNumberOfAttemptsBarChart(boolean canShowHighestNumberOfAttemptsBarChart)
	{
		this.canShowHighestNumberOfAttemptsBarChart = canShowHighestNumberOfAttemptsBarChart;
	}
	
	// IMPLEMENTATION
	
	@ActivityLog
	public void initialize() // throws Exception
	{
		try
		{
			//if (!FacesContext.getCurrentInstance().isPostback())
			{
				userStatusController.initialize();
				
				setupToolsFilter();
				
				setupProgramsFilter();
				
				setupSubjectsFilter();
				
				setupCoursesAssignmentsFilter();
				
				setupCourseGroupsFilter();
				
				setupAttemptsFilter();
				
				setupDateFilterOptions();
				
				setupGroupFieldsOptions();
				
				setupSemestersFilter();
				
				setupCharts();
			}
		}
		catch (Exception e)
		{
			LogHelper.error(e);
			
			try
			{
				NavigationHelper.redirectTo404();
			}
			catch (Exception e2)
			{
				LogHelper.error(e2);
			}
		}
	}
	
	@Override
	protected List<String> getGroupOptions()
	{
		List<String> options = new ArrayList<String>();
		
		options.add(FilterField.TOOL);
		options.add(FilterField.PROGRAM);
		options.add(FilterField.SUBJECT);
		options.add(FilterField.COURSE);
		options.add(FilterField.COURSEGROUP);
		options.add(FilterField.ASSIGNMENT);
		//options.add(FilterField.EXERCISE);
		options.add(FilterField.SEMESTER);
		
		return options;
	}
	
	@Override
	protected void setupDataInCharts(StudentsReportDataItem dataItem)
	{
	}
	
	@Override
	protected void chartsPreConfiguration()
	{
	}
	
	@Override
	protected List<String> getFiltersToSetup()
	{
		List<String> filters = new ArrayList<String>();
		
		filters.add(FilterField.TOOL);
		filters.add(FilterField.PROGRAM);
		filters.add(FilterField.SUBJECT);
		filters.add(FilterField.COURSE_ASSIGNMENT);
		filters.add(FilterField.COURSEGROUP);
		filters.add(FilterField.SEMESTER);
		filters.add(FilterField.ATTEMPTS);
		filters.add(FilterField.DATE);
		
		return filters;
	}
	
	@Override
	protected void chartsPostConfiguration(List<StudentsReportDataItem> data)
	{
	}
	
	@Override
	protected void topChartsPreConfiguration()
	{
		// HIGHEST FAILURE RATE STUDENTS CHART PRE CONFIGURATION
				
		highestFailureRateBarChartModel = new BarChartModel();
		highestFailureRateBarChartModel.setStacked(false);
				
		highestFailureRateSeries = new ChartSeries();
		highestFailureRateSeries.setLabel(LocalizationController.getLocalizedString("FailureRate"));
				
		highestFailureRateBarChartModel.addSeries(highestFailureRateSeries);
				
		// LOWEST NUMBER OF RIGHT EXERCISES STUDENTS CHART PRE CONFIGURATION
				
		lowestNumberOfRightExercisesBarChartModel = new BarChartModel();
		lowestNumberOfRightExercisesBarChartModel.setStacked(false);
				
		lowestNumberOfRightExercisesSeries = new ChartSeries();
		lowestNumberOfRightExercisesSeries.setLabel(LocalizationController.getLocalizedString("LowestNumberOfRightExercisesStudents"));
				
		lowestNumberOfRightExercisesBarChartModel.addSeries(lowestNumberOfRightExercisesSeries);
				
		// HIGHEST NUMBER OF ATTEMPTS STUDENTS CHART PRE CONFIGURATION
		
		highestNumberOfAttemptsBarChartModel = new BarChartModel();
		highestNumberOfAttemptsBarChartModel.setStacked(false);
		
		highestNumberOfAttemptsSeries = new ChartSeries();
		highestNumberOfAttemptsSeries.setLabel(LocalizationController.getLocalizedString("HighestNumberOfAttemptsStudents"));
		
		highestNumberOfAttemptsBarChartModel.addSeries(highestNumberOfAttemptsSeries);
	}
	
	@Override
	protected void topChartsPostConfiguration()
	{
		// HIGHEST FAILURE RATE STUDENTS CHART POSTCONFIGURATION
		
		int items = showAllStudents ? highestFailureRateStudents.size() : Math.min(maxExercises, highestFailureRateStudents.size());
		
		int minWidth = items * minColumnWidth;
		
		boolean rotateLabels = highestFailureRateStudents.size() > 5;
		
		setupVerticalBarChartAxis(highestFailureRateBarChartModel, LocalizationController.getLocalizedString("HighestFailureRateStudents"), "ne",
				rotateLabels, 0, (float) Math.round(maxHighestFailureRate * 1.1), "%d%");
		
		highestFailureRateBarChartMinWidth = minWidth;
		
		// LOWEST NUMBER OF RIGHT EXERCISES STUDENTS CHART PRE CONFIGURATION
		
		items = showAllStudents ? lowestNumberOfRightExercisesStudents.size() : Math.min(maxExercises, lowestNumberOfRightExercisesStudents.size());
		
		minWidth = items * minColumnWidth;
		
		rotateLabels = lowestNumberOfRightExercisesStudents.size() > 5;
		
		setupVerticalBarChartAxis(lowestNumberOfRightExercisesBarChartModel,
				LocalizationController.getLocalizedString("LowestNumberOfRightExercises"), "nw", rotateLabels, 0,
				(float) Math.round(maxLowestNumberOfRightExercises * 1.1), "%d");
		
		lowestNumberOfRightExercisesBarChartMinWidth = minWidth;
		
		// HIGHEST NUMBER OF ATTEMPTS STUDENTS CHART POST CONFIGURATION
		
		items = showAllStudents ? highestNumberOfAttemptsStudents.size() : Math.min(maxExercises, highestNumberOfAttemptsStudents.size());
		
		minWidth = items * minColumnWidth;
		
		rotateLabels = highestNumberOfAttemptsStudents.size() > 5;
		
		setupVerticalBarChartAxis(highestNumberOfAttemptsBarChartModel, LocalizationController.getLocalizedString("HighestNumberOfAttemptsStudents"),
				"ne", rotateLabels, 0, (float) Math.round(maxHighestNumberOfAttempts * 1.1), "%d");
		
		highestNumberOfAttemptsBarChartMinWidth = minWidth;
	}
	
	@Override
	protected void getTopChartsData(SearchParameters searchParameters) throws Exception
	{
		// TOP X STUDENTS DATA
		
		//		searchParameters = new SearchParameters();
		//		
		//		searchParameters.setFilters(filters);
		
		groupFieldsStudents = new ArrayList<String>(groupFieldsSelectedOptions);
		groupFieldsStudents.add(FilterField.STUDENT);
		
		searchParameters.setGroupFields(groupFieldsStudents);
		
		if (!showAllStudents)
		{
			searchParameters.setPage(1);
			searchParameters.setPageSize(maxStudents);
		}
		
		// HIGHEST FAILURE RATE 
		
		List<SortArgument> sortArguments = new ArrayList<SortArgument>();
		sortArguments.add(new SortArgument(FilterField.FAILURERATE, true));
		searchParameters.setSortArguments(sortArguments);
		
		ArrayList<String> hundredFilter = new ArrayList<String>();
		
		hundredFilter.add(" < 1");
		
		searchParameters.getFilters().put(FilterField.FAILURERATE, hundredFilter);
		
		highestFailureRateStudents = studentsReportManager.getReportData(SessionBean.getUsername(), searchParameters);
		
		searchParameters.getFilters().remove(FilterField.FAILURERATE);
		
		// LOWEST NUMBER OF RIGHT EXERCISES 
		
		sortArguments = new ArrayList<SortArgument>();
		sortArguments.add(new SortArgument(FilterField.RIGHTEXERCISES, false));
		searchParameters.setSortArguments(sortArguments);
		
		ArrayList<String> zeroFilter = new ArrayList<String>();
		
		zeroFilter.add(" > 0");
		
		searchParameters.getFilters().put(FilterField.RIGHTEXERCISES, zeroFilter);
		
		lowestNumberOfRightExercisesStudents = studentsReportManager.getReportData(SessionBean.getUsername(), searchParameters);
		
		searchParameters.getFilters().remove(FilterField.RIGHTEXERCISES);
		
		// HIGHEST NUMBER OF ATTEMPTS
		
		sortArguments = new ArrayList<SortArgument>();
		sortArguments.add(new SortArgument(FilterField.TOTALATTEMPTS, true));
		searchParameters.setSortArguments(sortArguments);
		
		highestNumberOfAttemptsStudents = studentsReportManager.getReportData(SessionBean.getUsername(), searchParameters);
	}
	
	@Override
	protected void processTopChartsData()
	{
		// TOP X STUDENTS
		
		maxHighestFailureRate = 0;
		maxLowestNumberOfRightExercises = 0;
		maxHighestNumberOfAttempts = 0;
		
		int index = 0;
		
		for (StudentsReportDataItem dataItem : highestFailureRateStudents)
		{
			if (showAllStudents || index <= maxStudents) // DRAW THE STUDENT IN GRAPHS ONLY IF NOT EXCEDES THE FILTER 
			{
				calculateDataItemLabel(dataItem, groupFieldsStudents);
					
				highestFailureRateSeries.set(dataItem.getChartLabel(), dataItem.getFailureRate() * 100);
				
				maxHighestFailureRate = Math.max(maxHighestFailureRate, dataItem.getFailureRate() * 100);
			}
			else
			{
				break;
			}
			
			index++;
		}
		
		canShowHighestFailureRateBarChart = highestFailureRateStudents.size() > 0;
		
		index = 0;
		
		for (StudentsReportDataItem dataItem : lowestNumberOfRightExercisesStudents)
		{
			if (showAllStudents || index <= maxStudents) // DRAW THE STUDENT IN GRAPHS ONLY IF NOT EXCEDES THE FILTER 
			{
				calculateDataItemLabel(dataItem, groupFieldsStudents);
					
				lowestNumberOfRightExercisesSeries.set(dataItem.getChartLabel(), dataItem.getRightExercises());
					
				maxLowestNumberOfRightExercises = Math.max(maxLowestNumberOfRightExercises, dataItem.getRightExercises());
			}
			else
			{
				break;
			}
			
			index++;
		}
		
		canShowLowestNumberOfRightExercisesBarChart = lowestNumberOfRightExercisesStudents.size() > 0;
		
		index = 0;
		
		for (StudentsReportDataItem dataItem : highestNumberOfAttemptsStudents)
		{
			if (showAllStudents || index <= maxStudents) // DRAW THE STUDENT IN GRAPHS ONLY IF NOT EXCEDES THE FILTER 
			{
				calculateDataItemLabel(dataItem, groupFieldsStudents);
				
				highestNumberOfAttemptsSeries.set(dataItem.getChartLabel(), dataItem.getAttempts());
				
				maxHighestNumberOfAttempts = Math.max(maxHighestNumberOfAttempts, dataItem.getAttempts());
			}
			else
			{
				break;
			}
			
			index++;
		}
		
		canShowHighestNumberOfAttemptsBarChart = highestNumberOfAttemptsStudents.size() > 0;
	}
}
