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
import edu.uoc.ictflag.ela.bll.IAssignmentsReportManager;
import edu.uoc.ictflag.ela.model.AssignmentsReportDataItem;
import edu.uoc.ictflag.ela.model.FilterField;

@Named
@ViewScoped
public class AssignmentsReportController extends ReportController<AssignmentsReportDataItem> implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Inject
	IAssignmentsReportManager assignmentsReportManager;
	
	@Override
	protected IAssignmentsReportManager getReportManager()
	{
		return assignmentsReportManager;
	}
	
	// PRIVATE FIELDS 
	
	private int alphabeticGradesBarChartMinWidth;
	private int numericGradesBarChartMinWidth;
	private int outcomesBarChartMinWidth;
	private int successRateBarChartMinHeight;
	private int durationBarChartMinWidth;
	
	private BarChartModel alphabeticGradesBarChartModel;
	private BarChartModel numericGradesBarChartModel;
	private BarChartModel outcomesBarChartModel;
	private HorizontalBarChartModel successRateBarChartModel;
	private BarChartModel durationBarChartModel;
	
	private ChartSeries numericGradesMinSeries;
	private ChartSeries numericGradesAvgSeries;
	private ChartSeries numericGradesMaxSeries;
	
	private ChartSeries alphabeticGradesASeries;
	private ChartSeries alphabeticGradesBSeries;
	private ChartSeries alphabeticGradesCPlusSeries;
	private ChartSeries alphabeticGradesCMinusSeries;
	private ChartSeries alphabeticGradesDSeries;
	
	private ChartSeries rightSeries;
	private ChartSeries wrongSeries;
	private ChartSeries timeoutSeries;
	private ChartSeries errorSeries;
	
	private ChartSeries successRateSeries;
	
	private ChartSeries durationMinSeries;
	private ChartSeries durationAvgSeries;
	private ChartSeries durationMaxSeries;
	
	// GETTERS AND SETTERS 
	
	public int getAlphabeticGradesBarChartMinWidth()
	{
		return alphabeticGradesBarChartMinWidth;
	}
	
	public int getOutcomesBarChartMinWidth()
	{
		return outcomesBarChartMinWidth;
	}
	
	public int getSuccessRateBarChartMinHeight()
	{
		return successRateBarChartMinHeight;
	}
	
	public int getDurationBarChartMinWidth()
	{
		return durationBarChartMinWidth;
	}
	
	public BarChartModel getNumericGradesBarChartModel()
	{
		return numericGradesBarChartModel;
	}
	
	public BarChartModel getAlphabeticGradesBarChartModel()
	{
		return alphabeticGradesBarChartModel;
	}
	
	public BarChartModel getOutcomesBarChartModel()
	{
		return outcomesBarChartModel;
	}
	
	public BarChartModel getSuccessRateBarChartModel()
	{
		return successRateBarChartModel;
	}
	
	public BarChartModel getDurationBarChartModel()
	{
		return durationBarChartModel;
	}
	
	public List<AssignmentsReportDataItem> getDataTableModel()
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
	protected void setupDataInCharts(AssignmentsReportDataItem dataItem)
	{
		numericGradesMinSeries.set(dataItem.getChartLabel(), dataItem.getMinNumericGrade());
		numericGradesAvgSeries.set(dataItem.getChartLabel(), dataItem.getAvgNumericGrade());
		numericGradesMaxSeries.set(dataItem.getChartLabel(), dataItem.getMaxNumericGrade());
		
		alphabeticGradesASeries.set(dataItem.getChartLabel(), dataItem.getAlphabeticGradeA());
		alphabeticGradesBSeries.set(dataItem.getChartLabel(), dataItem.getAlphabeticGradeB());
		alphabeticGradesCPlusSeries.set(dataItem.getChartLabel(), dataItem.getAlphabeticGradeCPlus());
		alphabeticGradesCMinusSeries.set(dataItem.getChartLabel(), dataItem.getAlphabeticGradeCMinus());
		alphabeticGradesDSeries.set(dataItem.getChartLabel(), dataItem.getAlphabeticGradeD());
		
		rightSeries.set(dataItem.getChartLabel(), dataItem.getRight());
		wrongSeries.set(dataItem.getChartLabel(), dataItem.getWrong());
		timeoutSeries.set(dataItem.getChartLabel(), dataItem.getTimeout());
		errorSeries.set(dataItem.getChartLabel(), dataItem.getError());
		
		successRateSeries.set(dataItem.getChartLabel(), dataItem.getSuccessRate() * 100);

		durationMinSeries.set(dataItem.getChartLabel(), dataItem.getMinDuration());
		durationAvgSeries.set(dataItem.getChartLabel(), dataItem.getAvgDuration());
		durationMaxSeries.set(dataItem.getChartLabel(), dataItem.getMaxDuration());
		
		if (stackedData)
		{
			maxNumericGrade = Math.max(maxNumericGrade,
					dataItem.getMinNumericGrade() + dataItem.getAvgNumericGrade() + dataItem.getMaxNumericGrade());
			
			maxAlphabeticGrade = Math.max(maxAlphabeticGrade, dataItem.getGroupCount());
			
			maxNumberOfOutcomes = Math.max(maxNumberOfOutcomes, dataItem.getGroupCount());
			
			maxNumberOfAttempts = Math.max(maxNumberOfAttempts,
					dataItem.getMinAttempts() + Math.round(dataItem.getAvgAttempts()) + dataItem.getMaxAttempts());
			
			maxTimeBetweenAttempts = Math.max(maxTimeBetweenAttempts,
					dataItem.getMinTimeBetweenAttempts() + dataItem.getAvgTimeBetweenAttempts() + dataItem.getMaxTimeBetweenAttempts());
			
			maxDuration = Math.max(maxDuration, dataItem.getMinDuration() + dataItem.getAvgDuration() + dataItem.getMaxDuration());
		}
		else
		{
			maxNumericGrade = Math.max(maxNumericGrade, dataItem.getMaxNumericGrade());
			
			maxAlphabeticGrade = Math.max(maxAlphabeticGrade, dataItem.getAlphabeticGradeA());
			maxAlphabeticGrade = Math.max(maxAlphabeticGrade, dataItem.getAlphabeticGradeB());
			maxAlphabeticGrade = Math.max(maxAlphabeticGrade, dataItem.getAlphabeticGradeCPlus());
			maxAlphabeticGrade = Math.max(maxAlphabeticGrade, dataItem.getAlphabeticGradeCMinus());
			maxAlphabeticGrade = Math.max(maxAlphabeticGrade, dataItem.getAlphabeticGradeD());
			
			maxNumberOfOutcomes = Math.max(maxNumberOfOutcomes, dataItem.getRight());
			maxNumberOfOutcomes = Math.max(maxNumberOfOutcomes, dataItem.getWrong());
			maxNumberOfOutcomes = Math.max(maxNumberOfOutcomes, dataItem.getTimeout());
			maxNumberOfOutcomes = Math.max(maxNumberOfOutcomes, dataItem.getError());
			
			maxNumberOfAttempts = Math.max(maxNumberOfAttempts, dataItem.getMaxAttempts());
			
			maxTimeBetweenAttempts = Math.max(maxTimeBetweenAttempts, dataItem.getMaxTimeBetweenAttempts());
			
			maxDuration = Math.max(maxDuration, dataItem.getMaxDuration());
		}
	}
	
	@Override
	protected void chartsPreConfiguration()
	{
		// NUMERIC GRADES CHART PRE CONFIGURATION
		
		numericGradesBarChartModel = new BarChartModel();
		numericGradesBarChartModel.setStacked(stackedData);
		
		numericGradesMinSeries = new ChartSeries();
		numericGradesMinSeries.setLabel(LocalizationController.getLocalizedString("NumericGradesMin"));
		//numericGradesMinSeries.setFill(true);
		
		numericGradesBarChartModel.addSeries(numericGradesMinSeries);
		
		numericGradesAvgSeries = new ChartSeries();
		numericGradesAvgSeries.setLabel(LocalizationController.getLocalizedString("NumericGradesAvg"));
		//numericGradesAvgSeries.setFill(true);
		
		numericGradesBarChartModel.addSeries(numericGradesAvgSeries);
		
		numericGradesMaxSeries = new ChartSeries();
		numericGradesMaxSeries.setLabel(LocalizationController.getLocalizedString("NumericGradesMax"));
		//numericGradesMaxSeries.setFill(true);
		
		numericGradesBarChartModel.addSeries(numericGradesMaxSeries);
		
		// ALPHABETIC GRADES CHART PRE CONFIGURATION
		
		alphabeticGradesBarChartModel = new BarChartModel();
		alphabeticGradesBarChartModel.setStacked(stackedData);
		
		alphabeticGradesASeries = new ChartSeries();
		alphabeticGradesASeries.setLabel("A");
		alphabeticGradesBarChartModel.addSeries(alphabeticGradesASeries);
		
		alphabeticGradesBSeries = new ChartSeries();
		alphabeticGradesBSeries.setLabel("B");
		alphabeticGradesBarChartModel.addSeries(alphabeticGradesBSeries);
		
		alphabeticGradesCPlusSeries = new ChartSeries();
		alphabeticGradesCPlusSeries.setLabel("C+");
		alphabeticGradesBarChartModel.addSeries(alphabeticGradesCPlusSeries);
		
		alphabeticGradesCMinusSeries = new ChartSeries();
		alphabeticGradesCMinusSeries.setLabel("C-");
		alphabeticGradesBarChartModel.addSeries(alphabeticGradesCMinusSeries);
		
		alphabeticGradesDSeries = new ChartSeries();
		alphabeticGradesDSeries.setLabel("D");
		alphabeticGradesBarChartModel.addSeries(alphabeticGradesDSeries);
		
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
		
		// SUCCESS RATE CHART PRE CONFIGURATION
		
		successRateBarChartModel = new HorizontalBarChartModel();
		successRateBarChartModel.setStacked(stackedData);
		
		successRateSeries = new ChartSeries();
		successRateSeries.setLabel(LocalizationController.getLocalizedString("SuccessRate"));
		
		successRateBarChartModel.addSeries(successRateSeries);
		
		// DURATION CHART PRE CONFIGURATION
		
		durationBarChartModel = new BarChartModel();
		durationBarChartModel.setStacked(stackedData);
		
		durationMinSeries = new ChartSeries();
		durationMinSeries.setLabel(LocalizationController.getLocalizedString("DurationMin"));
		
		durationBarChartModel.addSeries(durationMinSeries);
		
		durationAvgSeries = new ChartSeries();
		durationAvgSeries.setLabel(LocalizationController.getLocalizedString("DurationAvg"));
		
		durationBarChartModel.addSeries(durationAvgSeries);
		
		durationMaxSeries = new ChartSeries();
		durationMaxSeries.setLabel(LocalizationController.getLocalizedString("DurationMax"));
		
		durationBarChartModel.addSeries(durationMaxSeries);
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
	protected void chartsPostConfiguration(List<AssignmentsReportDataItem> data)
	{
		int items = showAllGroups ? data.size() : Math.min(maxGroups, data.size());
		
		int minWidth = items * minColumnWidth;
		
		boolean rotateLabels = data.size() > 5;
		
		// ALPHABETIC GRADES CHART POST CONFIGURATION
		
		setupVerticalBarChartAxis(alphabeticGradesBarChartModel, LocalizationController.getLocalizedString("AlphabeticGrades"), "nw", rotateLabels,
				0, (float) Math.round(maxAlphabeticGrade * 1.1), "%d");
		
		alphabeticGradesBarChartMinWidth = minWidth * 5; // minWidth * series 

		// NUMERIC GRADES CHART POST CONFIGURATION
		
		setupVerticalBarChartAxis(numericGradesBarChartModel, LocalizationController.getLocalizedString("NumericGrades"), "nw", rotateLabels, 0,
				(float) Math.round(maxNumericGrade * 1.1), "%d");

		numericGradesBarChartMinWidth = minWidth * 3; // minWidth * series 

		// OUTCOMES CHART POST CONFIGURATION 
		
		setupVerticalBarChartAxis(outcomesBarChartModel, LocalizationController.getLocalizedString("Outcomes"), "nw", rotateLabels, 0,
				(float) Math.round(maxNumberOfOutcomes * 1.1), "%d");

		outcomesBarChartMinWidth = minWidth * 4; // minWidth * series 

		// SUCCESS RATE CHART POST CONFIGURATION 
		
		setupHorizontalBarChartAxis(successRateBarChartModel, LocalizationController.getLocalizedString("SuccessRate"), "ne", false, 0, 100,
				"%d%");

		successRateBarChartMinHeight = Math.max(500, minWidth);

		// DURATION CHART POST CONFIGURATION
		
		setupVerticalBarChartAxis(durationBarChartModel, LocalizationController.getLocalizedString("Duration"), "nw", rotateLabels, 0,
				(float) Math.round(maxDuration * 1.1), "%d'");

		durationBarChartMinWidth = minWidth * 3; // minWidth * series 
	}
	
	public int getNumericGradesBarChartMinWidth()
	{
		return numericGradesBarChartMinWidth;
	}
	
	public void setNumericGradesBarChartMinWidth(int numericGradesBarChartMinWidth)
	{
		this.numericGradesBarChartMinWidth = numericGradesBarChartMinWidth;
	}
}
