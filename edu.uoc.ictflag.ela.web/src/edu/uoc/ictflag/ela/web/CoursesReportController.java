package edu.uoc.ictflag.ela.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;

import edu.uoc.ictflag.core.localization.LocalizationController;
import edu.uoc.ictflag.core.log.ActivityLog;
import edu.uoc.ictflag.core.log.LogHelper;
import edu.uoc.ictflag.core.web.NavigationHelper;
import edu.uoc.ictflag.ela.bll.ICoursesReportManager;
import edu.uoc.ictflag.ela.model.CoursesReportDataItem;
import edu.uoc.ictflag.ela.model.FilterField;

@Named
@ViewScoped
public class CoursesReportController extends ReportController<CoursesReportDataItem> implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Inject
	ICoursesReportManager coursesReportManager;
	
	@Override
	protected ICoursesReportManager getReportManager()
	{
		return coursesReportManager;
	}
	
	// PRIVATE FIELDS 
	
	private int assignmentsAlphabeticGradesBarChartMinWidth;
	private int assignmentsNumericGradesBarChartMinWidth;
	private int assignmentsSuccessRateBarChartMinHeight;
	private int assignmentsDurationBarChartMinWidth;
	
	private int exercisesOutcomesBarChartMinWidth;
	
	private BarChartModel assignmentsAlphabeticGradesBarChartModel;
	private BarChartModel assignmentsNumericGradesBarChartModel;
	private HorizontalBarChartModel assignmentsSuccessRateBarChartModel;
	private BarChartModel assignmentsDurationBarChartModel;
	
	private BarChartModel exercisesOutcomesBarChartModel;
	
	private ChartSeries assignmentsNumericGradesMinSeries;
	private ChartSeries assignmentsNumericGradesAvgSeries;
	private ChartSeries assignmentsNumericGradesMaxSeries;
	
	private ChartSeries assignmentsAlphabeticGradesASeries;
	private ChartSeries assignmentsAlphabeticGradesBSeries;
	private ChartSeries assignmentsAlphabeticGradesCPlusSeries;
	private ChartSeries assignmentsAlphabeticGradesCMinusSeries;
	private ChartSeries assignmentsAlphabeticGradesDSeries;
	
	private ChartSeries assignmentsSuccessRateSeries;
	
	private ChartSeries assignmentsDurationMinSeries;
	private ChartSeries assignmentsDurationAvgSeries;
	private ChartSeries assignmentsDurationMaxSeries;
	

	private ChartSeries exercisesRightSeries;
	private ChartSeries exercisesWrongSeries;
	private ChartSeries exercisesTimeoutSeries;
	private ChartSeries exercisesErrorSeries;
	

	protected int maxExercises = 10;
	
	// GETTERS AND SETTERS 
	
	public int getAssignmentsNumericGradesBarChartMinWidth()
	{
		return assignmentsNumericGradesBarChartMinWidth;
	}
	
	public void setAssignmentsNumericGradesBarChartMinWidth(int assignmentsNumericGradesBarChartMinWidth)
	{
		this.assignmentsNumericGradesBarChartMinWidth = assignmentsNumericGradesBarChartMinWidth;
	}
	
	public int getAssignmentsAlphabeticGradesBarChartMinWidth()
	{
		return assignmentsAlphabeticGradesBarChartMinWidth;
	}
	
	public int getAssignmentsSuccessRateBarChartMinHeight()
	{
		return assignmentsSuccessRateBarChartMinHeight;
	}
	
	public int getAssignmentsDurationBarChartMinWidth()
	{
		return assignmentsDurationBarChartMinWidth;
	}
	
	public int getExercisesOutcomesBarChartMinWidth()
	{
		return exercisesOutcomesBarChartMinWidth;
	}
	
	public int getMaxExercises()
	{
		return maxExercises;
	}
	
	public void setMaxExercises(int maxExercises)
	{
		this.maxExercises = maxExercises;
	}
	
	public BarChartModel getAssignmentsNumericGradesBarChartModel()
	{
		return assignmentsNumericGradesBarChartModel;
	}
	
	public BarChartModel getAssignmentsAlphabeticGradesBarChartModel()
	{
		return assignmentsAlphabeticGradesBarChartModel;
	}
	
	public BarChartModel getAssignmentsSuccessRateBarChartModel()
	{
		return assignmentsSuccessRateBarChartModel;
	}
	
	public BarChartModel getAssignmentsDurationBarChartModel()
	{
		return assignmentsDurationBarChartModel;
	}
	
	public BarChartModel getExercisesOutcomesBarChartModel()
	{
		return exercisesOutcomesBarChartModel;
	}
	
	public List<CoursesReportDataItem> getDataTableModel()
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
			
			NavigationHelper.redirectTo404();
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
	protected void setupDataInCharts(CoursesReportDataItem dataItem)
	{
		assignmentsNumericGradesMinSeries.set(dataItem.getChartLabel(), dataItem.getMinNumericGrade());
		assignmentsNumericGradesAvgSeries.set(dataItem.getChartLabel(), dataItem.getAvgNumericGrade());
		assignmentsNumericGradesMaxSeries.set(dataItem.getChartLabel(), dataItem.getMaxNumericGrade());
		
		assignmentsAlphabeticGradesASeries.set(dataItem.getChartLabel(), dataItem.getAlphabeticGradeA());
		assignmentsAlphabeticGradesBSeries.set(dataItem.getChartLabel(), dataItem.getAlphabeticGradeB());
		assignmentsAlphabeticGradesCPlusSeries.set(dataItem.getChartLabel(), dataItem.getAlphabeticGradeCPlus());
		assignmentsAlphabeticGradesCMinusSeries.set(dataItem.getChartLabel(), dataItem.getAlphabeticGradeCMinus());
		assignmentsAlphabeticGradesDSeries.set(dataItem.getChartLabel(), dataItem.getAlphabeticGradeD());
		
		assignmentsSuccessRateSeries.set(dataItem.getChartLabel(), dataItem.getSuccessRate() * 100);
		
		assignmentsDurationMinSeries.set(dataItem.getChartLabel(), dataItem.getMinDuration());
		assignmentsDurationAvgSeries.set(dataItem.getChartLabel(), dataItem.getAvgDuration());
		assignmentsDurationMaxSeries.set(dataItem.getChartLabel(), dataItem.getMaxDuration());
		
		exercisesRightSeries.set(dataItem.getChartLabel(), dataItem.getRight());
		exercisesWrongSeries.set(dataItem.getChartLabel(), dataItem.getWrong());
		exercisesTimeoutSeries.set(dataItem.getChartLabel(), dataItem.getTimeout());
		exercisesErrorSeries.set(dataItem.getChartLabel(), dataItem.getError());
		
		if (stackedData)
		{
			maxNumericGrade = Math.max(maxNumericGrade,
					dataItem.getMinNumericGrade() + dataItem.getAvgNumericGrade() + dataItem.getMaxNumericGrade());
			
			maxAlphabeticGrade = Math.max(maxAlphabeticGrade, dataItem.getGroupCount());
			
			maxDuration = Math.max(maxDuration, dataItem.getMinDuration() + dataItem.getAvgDuration() + dataItem.getMaxDuration());
			
			maxNumberOfOutcomes = Math.max(maxNumberOfOutcomes, dataItem.getGroupCount());
		}
		else
		{
			maxNumericGrade = Math.max(maxNumericGrade, dataItem.getMaxNumericGrade());
			
			maxAlphabeticGrade = Math.max(maxAlphabeticGrade, dataItem.getAlphabeticGradeA());
			maxAlphabeticGrade = Math.max(maxAlphabeticGrade, dataItem.getAlphabeticGradeB());
			maxAlphabeticGrade = Math.max(maxAlphabeticGrade, dataItem.getAlphabeticGradeCPlus());
			maxAlphabeticGrade = Math.max(maxAlphabeticGrade, dataItem.getAlphabeticGradeCMinus());
			maxAlphabeticGrade = Math.max(maxAlphabeticGrade, dataItem.getAlphabeticGradeD());
			
			maxDuration = Math.max(maxDuration, dataItem.getMaxDuration());
			
			maxNumberOfOutcomes = Math.max(maxNumberOfOutcomes, dataItem.getRight());
			maxNumberOfOutcomes = Math.max(maxNumberOfOutcomes, dataItem.getWrong());
			maxNumberOfOutcomes = Math.max(maxNumberOfOutcomes, dataItem.getTimeout());
			maxNumberOfOutcomes = Math.max(maxNumberOfOutcomes, dataItem.getError());
		}
	}
	
	@Override
	protected void chartsPreConfiguration()
	{
		// NUMERIC GRADES CHART PRE CONFIGURATION
		
		assignmentsNumericGradesBarChartModel = new BarChartModel();
		assignmentsNumericGradesBarChartModel.setStacked(stackedData);
		
		assignmentsNumericGradesMinSeries = new ChartSeries();
		assignmentsNumericGradesMinSeries.setLabel(LocalizationController.getLocalizedString("NumericGradesMin"));
		//assignmentsNumericGradesMinSeries.setFill(true);
		
		assignmentsNumericGradesBarChartModel.addSeries(assignmentsNumericGradesMinSeries);
		
		assignmentsNumericGradesAvgSeries = new ChartSeries();
		assignmentsNumericGradesAvgSeries.setLabel(LocalizationController.getLocalizedString("NumericGradesAvg"));
		//assignmentsNumericGradesAvgSeries.setFill(true);
		
		assignmentsNumericGradesBarChartModel.addSeries(assignmentsNumericGradesAvgSeries);
		
		assignmentsNumericGradesMaxSeries = new ChartSeries();
		assignmentsNumericGradesMaxSeries.setLabel(LocalizationController.getLocalizedString("NumericGradesMax"));
		//assignmentsNumericGradesMaxSeries.setFill(true);
		
		assignmentsNumericGradesBarChartModel.addSeries(assignmentsNumericGradesMaxSeries);
		
		// ALPHABETIC GRADES CHART PRE CONFIGURATION
		
		assignmentsAlphabeticGradesBarChartModel = new BarChartModel();
		assignmentsAlphabeticGradesBarChartModel.setStacked(stackedData);
		
		assignmentsAlphabeticGradesASeries = new ChartSeries();
		assignmentsAlphabeticGradesASeries.setLabel("A");
		assignmentsAlphabeticGradesBarChartModel.addSeries(assignmentsAlphabeticGradesASeries);
		
		assignmentsAlphabeticGradesBSeries = new ChartSeries();
		assignmentsAlphabeticGradesBSeries.setLabel("B");
		assignmentsAlphabeticGradesBarChartModel.addSeries(assignmentsAlphabeticGradesBSeries);
		
		assignmentsAlphabeticGradesCPlusSeries = new ChartSeries();
		assignmentsAlphabeticGradesCPlusSeries.setLabel("C+");
		assignmentsAlphabeticGradesBarChartModel.addSeries(assignmentsAlphabeticGradesCPlusSeries);
		
		assignmentsAlphabeticGradesCMinusSeries = new ChartSeries();
		assignmentsAlphabeticGradesCMinusSeries.setLabel("C-");
		assignmentsAlphabeticGradesBarChartModel.addSeries(assignmentsAlphabeticGradesCMinusSeries);
		
		assignmentsAlphabeticGradesDSeries = new ChartSeries();
		assignmentsAlphabeticGradesDSeries.setLabel("D");
		assignmentsAlphabeticGradesBarChartModel.addSeries(assignmentsAlphabeticGradesDSeries);
		
		// SUCCESS RATE CHART PRE CONFIGURATION
		
		assignmentsSuccessRateBarChartModel = new HorizontalBarChartModel();
		assignmentsSuccessRateBarChartModel.setStacked(stackedData);
		
		assignmentsSuccessRateSeries = new ChartSeries();
		assignmentsSuccessRateSeries.setLabel(LocalizationController.getLocalizedString("SuccessRate"));
		
		assignmentsSuccessRateBarChartModel.addSeries(assignmentsSuccessRateSeries);
		
		// DURATION CHART PRE CONFIGURATION
		
		assignmentsDurationBarChartModel = new BarChartModel();
		assignmentsDurationBarChartModel.setStacked(stackedData);
		
		assignmentsDurationMinSeries = new ChartSeries();
		assignmentsDurationMinSeries.setLabel(LocalizationController.getLocalizedString("DurationMin"));
		
		assignmentsDurationBarChartModel.addSeries(assignmentsDurationMinSeries);
		
		assignmentsDurationAvgSeries = new ChartSeries();
		assignmentsDurationAvgSeries.setLabel(LocalizationController.getLocalizedString("DurationAvg"));
		
		assignmentsDurationBarChartModel.addSeries(assignmentsDurationAvgSeries);
		
		assignmentsDurationMaxSeries = new ChartSeries();
		assignmentsDurationMaxSeries.setLabel(LocalizationController.getLocalizedString("DurationMax"));
		
		assignmentsDurationBarChartModel.addSeries(assignmentsDurationMaxSeries);

		// OUTCOMES CHART PRE CONFIGURATION
		
		exercisesOutcomesBarChartModel = new BarChartModel();
		exercisesOutcomesBarChartModel.setStacked(stackedData);
		
		exercisesRightSeries = new ChartSeries();
		exercisesRightSeries.setLabel(LocalizationController.getLocalizedString("Right"));
		
		exercisesWrongSeries = new ChartSeries();
		exercisesWrongSeries.setLabel(LocalizationController.getLocalizedString("Wrong"));
		
		exercisesTimeoutSeries = new ChartSeries();
		exercisesTimeoutSeries.setLabel(LocalizationController.getLocalizedString("Timeout"));
		
		exercisesErrorSeries = new ChartSeries();
		exercisesErrorSeries.setLabel(LocalizationController.getLocalizedString("Error"));
		
		exercisesOutcomesBarChartModel.addSeries(exercisesRightSeries);
		exercisesOutcomesBarChartModel.addSeries(exercisesWrongSeries);
		exercisesOutcomesBarChartModel.addSeries(exercisesTimeoutSeries);
		exercisesOutcomesBarChartModel.addSeries(exercisesErrorSeries);
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
	protected void setupGroupFieldsOptions() throws Exception
	{
		super.setupGroupFieldsOptions();
		
		if (!groupFieldsSelectedOptions.contains(FilterField.SEMESTER))
		{
			groupFieldsSelectedOptions.add(FilterField.SEMESTER);
		}
	}
	
	@Override
	protected void chartsPostConfiguration(List<CoursesReportDataItem> data)
	{
		int items = showAllGroups ? data.size() : Math.min(maxGroups, data.size());
		
		int minWidth = items * minColumnWidth;
		
		boolean rotateLabels = data.size() > 5;
		
		// ALPHABETIC GRADES CHART POST CONFIGURATION
		
		setupVerticalBarChartAxis(assignmentsAlphabeticGradesBarChartModel, LocalizationController.getLocalizedString("AlphabeticGrades"), "nw",
				rotateLabels, 0, (float) Math.round(maxAlphabeticGrade * 1.1), "%d");
		
		assignmentsAlphabeticGradesBarChartMinWidth = minWidth * 5; // minWidth * series 
		
		// NUMERIC GRADES CHART POST CONFIGURATION
		
		setupVerticalBarChartAxis(assignmentsNumericGradesBarChartModel, LocalizationController.getLocalizedString("NumericGrades"), "nw",
				rotateLabels, 0, (float) Math.round(maxNumericGrade * 1.1), "%d");
		
		assignmentsNumericGradesBarChartMinWidth = minWidth * 3; // minWidth * series 
		
		// SUCCESS RATE CHART POST CONFIGURATION 
		
		setupHorizontalBarChartAxis(assignmentsSuccessRateBarChartModel, LocalizationController.getLocalizedString("SuccessRate"), "ne", false, 0, 100,
				"%d%");
		
		assignmentsSuccessRateBarChartMinHeight = Math.max(500, minWidth);
		
		// DURATION CHART POST CONFIGURATION
		
		setupVerticalBarChartAxis(assignmentsDurationBarChartModel, LocalizationController.getLocalizedString("Duration"), "nw", rotateLabels, 0,
				(float) Math.round(maxDuration * 1.1), "%d'");
		
		assignmentsDurationBarChartMinWidth = minWidth * 3; // minWidth * series 
		
		// OUTCOMES CHART POST CONFIGURATION 
		
		setupVerticalBarChartAxis(exercisesOutcomesBarChartModel, LocalizationController.getLocalizedString("Outcomes"), "nw", rotateLabels, 0,
				(float) Math.round(maxNumberOfOutcomes * 1.1), "%d");
		
		exercisesOutcomesBarChartMinWidth = minWidth * 4; // minWidth * series 
	}
}
