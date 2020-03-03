package edu.uoc.ictflag.ela.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;

import edu.uoc.ictflag.core.dal.SearchParameters;
import edu.uoc.ictflag.core.dal.SortArgument;
import edu.uoc.ictflag.core.localization.LocalizationController;
import edu.uoc.ictflag.core.localization.LocalizedItem;
import edu.uoc.ictflag.core.log.ActivityLog;
import edu.uoc.ictflag.core.log.LogHelper;
import edu.uoc.ictflag.core.web.NavigationHelper;
import edu.uoc.ictflag.core.web.SessionBean;
import edu.uoc.ictflag.ela.bll.IExercisesReportManager;
import edu.uoc.ictflag.ela.bll.IStudentReportManager;
import edu.uoc.ictflag.ela.model.ExercisesReportDataItem;
import edu.uoc.ictflag.ela.model.FilterField;
import edu.uoc.ictflag.ela.model.ReportDataItem;
import edu.uoc.ictflag.ela.model.StudentReportGlobalProgressItem;
import edu.uoc.ictflag.security.web.UserStatusController;

@Named
@ViewScoped
public class ExercisesReportFixedController extends ReportController<ExercisesReportDataItem> implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Inject
	IExercisesReportManager exercisesReportManager;
	
	@Inject
	IStudentReportManager studentReportManager;
	
	@Inject
	UserStatusController userStatusController;
	
	@Override
	protected IExercisesReportManager getReportManager()
	{
		return exercisesReportManager;
	}
	
	// PRIVATE FIELDS 
	
	private String reportLabel;
	
	private StudentReportGlobalProgressItem globalProgress;
	
	private int outcomesBarChartMinWidth;
	private int scoresBarChartMinWidth;
	private int successRateBarChartMinHeight;
	private int attemptsBarChartMinWidth;
	
	private BarChartModel outcomesBarChartModel;
	private BarChartModel scoresBarChartModel;
	private HorizontalBarChartModel successRateBarChartModel;
	private BarChartModel attemptsBarChartModel;
	
	private ChartSeries rightSeries;
	private ChartSeries wrongSeries;
	private ChartSeries timeoutSeries;
	private ChartSeries errorSeries;
	private ChartSeries score1Series;
	private ChartSeries score2Series;
	private ChartSeries score3Series;
	private ChartSeries successRateSeries;
	private ChartSeries attemptsMinSeries;
	private ChartSeries attemptsAvgSeries;
	private ChartSeries attemptsMaxSeries;
	
	// GETTERS AND SETTERS 
	
	public StudentReportGlobalProgressItem getGlobalProgress()
	{
		return globalProgress;
	}
	
	public void setGlobalProgress(StudentReportGlobalProgressItem globalProgress)
	{
		this.globalProgress = globalProgress;
	}
	
	public int getOutcomesBarChartMinWidth()
	{
		return outcomesBarChartMinWidth;
	}
	
	public int getScoresBarChartMinWidth()
	{
		return scoresBarChartMinWidth;
	}
	
	public int getSuccessRateBarChartMinHeight()
	{
		return successRateBarChartMinHeight;
	}
	
	public int getAttemptsBarChartMinWidth()
	{
		return attemptsBarChartMinWidth;
	}
	
	public BarChartModel getOutcomesBarChartModel()
	{
		return outcomesBarChartModel;
	}
	
	public BarChartModel getScoresBarChartModel()
	{
		return scoresBarChartModel;
	}
	
	public BarChartModel getSuccessRateBarChartModel()
	{
		return successRateBarChartModel;
	}
	
	public BarChartModel getAttemptsBarChartModel()
	{
		return attemptsBarChartModel;
	}
	
	public void setAttemptsBarChartModel(BarChartModel attemptsBarChartModel)
	{
		this.attemptsBarChartModel = attemptsBarChartModel;
	}
	
	public List<ExercisesReportDataItem> getDataTableModel()
	{
		return dataTableModel;
	}
	
	// IMPLEMENTATION
	
	@ActivityLog
	public void initialize() throws Exception
	{
		try
		{
			//if (!FacesContext.getCurrentInstance().isPostback())
			{
				userStatusController.initialize();
				
				// setupProgramsFilter();
				
				// setupSubjectsFilter();
				
				// setupCoursesAssignmentsFilter();
				
				// setupCourseGroupsFilter();
				
				setupToolsFilter();
				
				//setupToolsExercisesFilter();
				
				setupAttemptsFilter();
				
				setupDateFilterOptions();
				
				setupGroupFieldsOptions();
				
				//setupSemestersFilter();
				
				setupCharts();
			}
		}
		catch (Exception e)
		{
			LogHelper.error(e);
			
			NavigationHelper.redirectTo404();
		}
	}
	
	@Override
	protected List<String> getGroupOptions()
	{
		List<String> options = new ArrayList<String>();
		
		options.add(FilterField.TOOL);
		//options.add(FilterField.PROGRAM);
		//options.add(FilterField.SUBJECT);
		//options.add(FilterField.COURSE);
		//options.add(FilterField.COURSEGROUP);
		options.add(FilterField.ASSIGNMENT);
		options.add(FilterField.EXERCISE);
		//options.add(FilterField.SEMESTER);
		
		return options;
	}
	
	@Override
	protected void setupGroupFieldsOptions() throws Exception
	{
		groupFieldsOptions = new ArrayList<LocalizedItem>();
		
		getGroupOptions().stream().forEach(x -> groupFieldsOptions.add(new LocalizedItem(x, LocalizationController.getLocalizedString(x))));
	}
	
	@Override
	protected void setupFilters() throws Exception
	{
		maxAttempts = getReportManager().getMaxNumberOfAttempts(SessionBean.getUsername());
		
		attemptsOperation = "<=";
		attempts = maxAttempts;
		
		showAllGroups = true;
		showAllExercises = true;
		
		if (selectedSemesters == null)
		{
			selectedSemesters = new ArrayList<String>();
		}
		
		selectedSemesters.add("21"); // 21 = 2016-1, 20 = 2016-2
		
		super.setupFilters();
	}
	
	@Override
	protected void getData() throws Exception
	{
		globalProgress = studentReportManager.getGlobalProgressData();
		
		if (groupFieldsSelectedOptions == null)
		{
			groupFieldsSelectedOptions = new ArrayList<String>();
		}
		
		groupFieldsSelectedOptions.add(0, FilterField.SEMESTER);
		groupFieldsSelectedOptions.add(0, FilterField.COURSE);
		groupFieldsSelectedOptions.add(0, FilterField.SUBJECT);
		groupFieldsSelectedOptions.add(0, FilterField.PROGRAM);
		
		super.getData();
	}
	
	@Override
	protected void setupDataInCharts(ExercisesReportDataItem dataItem)
	{
		rightSeries.set(dataItem.getChartLabel(), dataItem.getRight());
		wrongSeries.set(dataItem.getChartLabel(), dataItem.getWrong());
		timeoutSeries.set(dataItem.getChartLabel(), dataItem.getTimeout());
		errorSeries.set(dataItem.getChartLabel(), dataItem.getError());

		score1Series.set(dataItem.getChartLabel(), dataItem.getScore1());
		score2Series.set(dataItem.getChartLabel(), dataItem.getScore2());
		score3Series.set(dataItem.getChartLabel(), dataItem.getScore3());

		successRateSeries.set(dataItem.getChartLabel(), dataItem.getSuccessRate() * 100);
		
		attemptsMinSeries.set(dataItem.getChartLabel(), dataItem.getMinAttempts());
		attemptsAvgSeries.set(dataItem.getChartLabel(), dataItem.getAvgAttempts());
		attemptsMaxSeries.set(dataItem.getChartLabel(), dataItem.getMaxAttempts());
		
		if (stackedData)
		{
			maxNumberOfOutcomes = Math.max(maxNumberOfOutcomes, dataItem.getGroupCount());
			
			maxNumberOfScores = Math.max(maxNumberOfScores, dataItem.getGroupCount());
			
			maxNumberOfAttempts = Math.max(maxNumberOfAttempts,
					dataItem.getMinAttempts() + Math.round(dataItem.getAvgAttempts()) + dataItem.getMaxAttempts());
			
			maxTimeBetweenAttempts = Math.max(maxTimeBetweenAttempts,
					dataItem.getMinTimeBetweenAttempts() + dataItem.getAvgTimeBetweenAttempts() + dataItem.getMaxTimeBetweenAttempts());
		}
		else
		{
			maxNumberOfOutcomes = Math.max(maxNumberOfOutcomes, dataItem.getRight());
			maxNumberOfOutcomes = Math.max(maxNumberOfOutcomes, dataItem.getWrong());
			maxNumberOfOutcomes = Math.max(maxNumberOfOutcomes, dataItem.getTimeout());
			maxNumberOfOutcomes = Math.max(maxNumberOfOutcomes, dataItem.getError());
			
			maxNumberOfScores = Math.max(maxNumberOfScores, dataItem.getScore1());
			maxNumberOfScores = Math.max(maxNumberOfScores, dataItem.getScore2());
			maxNumberOfScores = Math.max(maxNumberOfScores, dataItem.getScore3());
			
			maxNumberOfAttempts = Math.max(maxNumberOfAttempts, dataItem.getMaxAttempts());
			
			maxTimeBetweenAttempts = Math.max(maxTimeBetweenAttempts, dataItem.getMaxTimeBetweenAttempts());
		}
	}
	
	@Override
	protected void chartsPreConfiguration()
	{
		// OUTCOMES CHART PRE CONFIGURATION
		
		outcomesBarChartModel = new BarChartModel();
		outcomesBarChartModel.setStacked(stackedData);
		
		rightSeries = new ChartSeries();
		rightSeries.setLabel(LocalizationController.getLocalizedString("Right"));
		
		wrongSeries = new ChartSeries();
		wrongSeries.setLabel(LocalizationController.getLocalizedString("Wrong"));
		
		timeoutSeries = new ChartSeries();
		timeoutSeries.setLabel(LocalizationController.getLocalizedString("Timeout"));
		
		errorSeries = new ChartSeries();
		errorSeries.setLabel(LocalizationController.getLocalizedString("Error"));
		
		outcomesBarChartModel.addSeries(rightSeries);
		outcomesBarChartModel.addSeries(wrongSeries);
		outcomesBarChartModel.addSeries(timeoutSeries);
		outcomesBarChartModel.addSeries(errorSeries);
		
		// SCORES CHART PRE CONFIGURATION
		
		scoresBarChartModel = new BarChartModel();
		scoresBarChartModel.setStacked(stackedData);
		
		score1Series = new ChartSeries();
		score1Series.setLabel(LocalizationController.getLocalizedString("1Star"));
		
		score2Series = new ChartSeries();
		score2Series.setLabel(LocalizationController.getLocalizedString("2Stars"));
		
		score3Series = new ChartSeries();
		score3Series.setLabel(LocalizationController.getLocalizedString("3Stars"));
		
		scoresBarChartModel.addSeries(score1Series);
		scoresBarChartModel.addSeries(score2Series);
		scoresBarChartModel.addSeries(score3Series);
		
		// SUCCESS RATE CHART PRE CONFIGURATION
		
		successRateBarChartModel = new HorizontalBarChartModel();
		successRateBarChartModel.setStacked(stackedData);
		
		successRateSeries = new ChartSeries();
		successRateSeries.setLabel(LocalizationController.getLocalizedString("SuccessRate"));
		
		successRateBarChartModel.addSeries(successRateSeries);
		
		// ATTEMPTS CHART PRE CONFIGURATION
		
		attemptsBarChartModel = new BarChartModel();
		attemptsBarChartModel.setStacked(stackedData);
		//attemptsBarChartModel.setStacked(true);
		//attemptsBarChartModel.setShowPointLabels(true);
		
		attemptsMinSeries = new ChartSeries();
		attemptsMinSeries.setLabel(LocalizationController.getLocalizedString("AttemptsMin"));
		//attemptsMinSeries.setFill(true);
		
		attemptsBarChartModel.addSeries(attemptsMinSeries);
		
		attemptsAvgSeries = new ChartSeries();
		attemptsAvgSeries.setLabel(LocalizationController.getLocalizedString("AttemptsAvg"));
		//attemptsAvgSeries.setFill(true);
		
		attemptsBarChartModel.addSeries(attemptsAvgSeries);
		
		attemptsMaxSeries = new ChartSeries();
		attemptsMaxSeries.setLabel(LocalizationController.getLocalizedString("AttemptsMax"));
		//attemptsMaxSeries.setFill(true);
		
		attemptsBarChartModel.addSeries(attemptsMaxSeries);
	}
	
	@Override
	protected List<String> getFiltersToSetup()
	{
		List<String> filters = new ArrayList<String>();
		
		// filters.add(FilterField.TOOL_EXERCISE);
		filters.add(FilterField.TOOL);
		// filters.add(FilterField.PROGRAM);
		// filters.add(FilterField.SUBJECT);
		// filters.add(FilterField.COURSE_ASSIGNMENT);
		// filters.add(FilterField.COURSEGROUP);
		// filters.add(FilterField.SEMESTER);
		// filters.add(FilterField.EXERCISE);
		// filters.add(FilterField.ATTEMPTS);
		filters.add(FilterField.DATE);
		
		return filters;
	}
	
	@Override
	protected void chartsPostConfiguration(List<ExercisesReportDataItem> data)
	{
		//int minWidth = data.size() * minColumnWidth;
		int minWidth = Math.min(maxGroups, data.size()) * minColumnWidth;
		
		boolean rotateLabels = data.size() > 5;
		
		// OUTCOMES CHART POST CONFIGURATION 
		
		setupVerticalBarChartAxis(outcomesBarChartModel, LocalizationController.getLocalizedString("Outcomes"), "nw", rotateLabels, 0,
				Math.round(maxNumberOfOutcomes * 1.1), "%d");
		
		outcomesBarChartMinWidth = minWidth * 4; // minWidth * series
		
		// SCORES CHART POST CONFIGURATION 
		
		setupVerticalBarChartAxis(scoresBarChartModel, LocalizationController.getLocalizedString("Scores"), "nw", rotateLabels, 0,
				Math.round(maxNumberOfScores * 1.1), "%d");
		
		scoresBarChartMinWidth = minWidth * 4; // minWidth * series		

		// SUCCESS RATE CHART POST CONFIGURATION 
		
		setupHorizontalBarChartAxis(successRateBarChartModel, LocalizationController.getLocalizedString("SuccessRate"), "ne", false, 0, 100,
				"%d%");
		
		successRateBarChartMinHeight = Math.max(500, minWidth);
		
		// ATTEMPTS CHART POST CONFIGURATION
		
		setupVerticalBarChartAxis(attemptsBarChartModel, LocalizationController.getLocalizedString("Attempts"), "nw", rotateLabels, 0,
				Math.round(maxNumberOfAttempts * 1.1), "%d");
		
		attemptsBarChartMinWidth = minWidth * 3; // minWidth * series
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
		
		long left = globalProgress.getGlobalProgressLevelMaxValue(level) - globalProgress.getGlobalProgressCurrentValue();
		
		if (left <= 0)
		{
			levelMessage = LocalizationController.getLocalizedString("NoExercisesLeft").replace("EXERCISES",
					"" + globalProgress.getGlobalProgressLevelMaxValue(level));
		}
		else
		{
			long left_2 = (level == 0 ? 0 : globalProgress.getGlobalProgressLevelMaxValue(level - 1))
					- globalProgress.getGlobalProgressCurrentValue();
			
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
				max = globalProgress.getGlobalProgressLevelMaxValue(level);
				break;
			default:
				min = globalProgress.getGlobalProgressLevelMaxValue(level - 1);
				max = globalProgress.getGlobalProgressLevelMaxValue(level);
				break;
		}
		
		long currentValue = globalProgress.getGlobalProgressCurrentValue();
		
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
	
	@Override
	protected void getTopChartsData(SearchParameters searchParameters) throws Exception
	{
		// TOP X EXERCISES DATA
		
		//		searchParameters = new SearchParameters();
		//		
		//		searchParameters.setFilters(filters);
		
		groupFieldsExercises = new ArrayList<String>(groupFieldsSelectedOptions);
		
		if (!groupFieldsSelectedOptions.contains(FilterField.EXERCISE))
		{
			groupFieldsExercises.add(FilterField.EXERCISE);
		}
		
		searchParameters.setGroupFields(groupFieldsExercises);
		
		if (!showAllExercises)
		{
			searchParameters.setPage(1);
			searchParameters.setPageSize(maxExercises);
		}
		
		// LOWEST SUCCESS RATE 
		
		List<SortArgument> sortArguments = new ArrayList<SortArgument>();
		sortArguments.add(new SortArgument(FilterField.SUCCESSRATE, false));
		searchParameters.setSortArguments(sortArguments);
		
		lowestSuccessRateExercises = exercisesReportManager.getReportData(SessionBean.getUsername(), searchParameters);
		
		// HIGHEST NUMBER OF ATTEMPTS 
		
		sortArguments = new ArrayList<SortArgument>();
		sortArguments.add(new SortArgument(FilterField.MAXATTEMPTS, true));
		searchParameters.setSortArguments(sortArguments);
		
		highestNumberOfAttemptsExercises = exercisesReportManager.getReportData(SessionBean.getUsername(), searchParameters);
	}
	
	@Override
	protected void processTopChartsData()
	{
		// TOP X EXERCICES
		
		maxLowestSuccessRate = 0;
		maxHighestNumberOfAttempts = 0;
		
		int index = 0;
		
		for (ExercisesReportDataItem dataItem : lowestSuccessRateExercises)
		{
			calculateDataItemLabel(dataItem, groupFieldsExercises);
			
			if (showAllExercises || index <= maxExercises) // DRAW THE EXERCISE IN GRAPHS ONLY IF NOT EXCEDES THE FILTER 
			{
				lowestSuccessRateSeries.set(dataItem.getChartLabel(), dataItem.getSuccessRate() * 100);
				
				maxLowestSuccessRate = Math.max(maxLowestSuccessRate, dataItem.getSuccessRate() * 100);
			}
			else
			{
				break;
			}
			
			index++;
		}
		
		canShowLowestSuccessRateBarChart = lowestSuccessRateExercises.size() > 0;
		
		index = 0;
		
		for (ExercisesReportDataItem dataItem : highestNumberOfAttemptsExercises)
		{
			calculateDataItemLabel(dataItem, groupFieldsExercises);
			
			if (showAllExercises || index <= maxExercises) // DRAW THE EXERCISE IN GRAPHS ONLY IF NOT EXCEDES THE FILTER 
			{
				highestNumberOfAttemptsSeries.set(dataItem.getChartLabel(), dataItem.getMaxAttempts());
				
				maxHighestNumberOfAttempts = Math.max(maxHighestNumberOfAttempts, dataItem.getMaxAttempts());
			}
			else
			{
				break;
			}
			
			index++;
		}
		
		canShowHighestNumberOfAttemptsBarChart = highestNumberOfAttemptsExercises.size() > 0;
	}
	
	@Override
	protected void topChartsPreConfiguration()
	{
		// LOWEST SUCCESS RATE EXERCISES CHART PRE CONFIGURATION
		
		lowestSuccessRateBarChartModel = new BarChartModel();
		lowestSuccessRateBarChartModel.setStacked(false);
		
		lowestSuccessRateSeries = new ChartSeries();
		lowestSuccessRateSeries.setLabel(LocalizationController.getLocalizedString("SuccessRate"));
		
		lowestSuccessRateBarChartModel.addSeries(lowestSuccessRateSeries);
		
		// HIGHEST NUMBER OF ATTEMPTS EXERCISES CHART PRE CONFIGURATION
		
		highestNumberOfAttemptsBarChartModel = new BarChartModel();
		highestNumberOfAttemptsBarChartModel.setStacked(false);
		
		highestNumberOfAttemptsSeries = new ChartSeries();
		highestNumberOfAttemptsSeries.setLabel(LocalizationController.getLocalizedString("HighestNumberOfAttempts"));
		
		highestNumberOfAttemptsBarChartModel.addSeries(highestNumberOfAttemptsSeries);
	}
	
	@Override
	protected void topChartsPostConfiguration()
	{
		// LOWEST SUCCESS RATE EXERCISES CHART POST CONFIGURATION
		
		int items = showAllExercises ? lowestSuccessRateExercises.size() : Math.min(maxExercises, lowestSuccessRateExercises.size());
		
		int minWidth = items * minColumnWidth;
		
		boolean rotateLabels = lowestSuccessRateExercises.size() > 5;
		
		setupVerticalBarChartAxis(lowestSuccessRateBarChartModel, LocalizationController.getLocalizedString("LowestSuccessRateExercises"), "nw",
				rotateLabels, 0, (float) Math.round(maxLowestSuccessRate * 1.1), "%d%");
		
		lowestSuccessRateBarChartMinWidth = minWidth;
		
		// HIGHEST NUMBER OF ATTEMPTS EXERCISES CHART POST CONFIGURATION
		
		items = showAllExercises ? highestNumberOfAttemptsExercises.size() : Math.min(maxExercises, highestNumberOfAttemptsExercises.size());
		
		minWidth = items * minColumnWidth;
		
		rotateLabels = highestNumberOfAttemptsExercises.size() > 5;
		
		setupVerticalBarChartAxis(highestNumberOfAttemptsBarChartModel, LocalizationController.getLocalizedString("HighestNumberOfAttemptsExercises"),
				"ne", rotateLabels, 0, (float) Math.round(maxHighestNumberOfAttempts * 1.1), "%d");
		
		highestNumberOfAttemptsBarChartMinWidth = minWidth;
	}

	protected <S extends ReportDataItem> void calculateDataItemLabel(S dataItem, List<String> groupFieldsSelectedOptions)
	{
		String chartLabel = LocalizationController.getLocalizedString("Total");
		String tableLabel = LocalizationController.getLocalizedString("Total");
		reportLabel = "";
		
		String NONE = LocalizationController.getLocalizedString("UNASSIGNED");
		
		List<String> groupFields = dataItem.getGroupFields();
		
		if (groupFields != null && groupFields.size() > 0)
		{
			// skip 4 = PROGRAM, SUBJECT, COURSE, SEMESTER 
			chartLabel = groupFields.size() == 4 ? ""
					: String.join(" - ", groupFields.stream().skip(4).map(x -> x == null ? NONE : x).collect(Collectors.toList()));
			
			tableLabel = String.join("\n",
					IntStream.range(0, groupFields.size()).mapToObj(x -> groupFieldsOptionToDataTableLabel(groupFieldsSelectedOptions.get(x)) + ": "
							+ (groupFields.get(x) == null ? NONE : groupFields.get(x))).collect(Collectors.toList()));
			
			reportLabel = String.join("\n", IntStream.range(0, 4).mapToObj(x -> groupFieldsOptionToDataTableLabel(groupFieldsSelectedOptions.get(x))
					+ ": " + (groupFields.get(x) == null ? NONE : groupFields.get(x))).collect(Collectors.toList()));
		}
		
		dataItem.setChartLabel(chartLabel);
		dataItem.setTableLabel(tableLabel);
	}
	
	public String getReportLabel()
	{
		return reportLabel;
	}
	
	public void setReportLabel(String reportLabel)
	{
		this.reportLabel = reportLabel;
	}
}
