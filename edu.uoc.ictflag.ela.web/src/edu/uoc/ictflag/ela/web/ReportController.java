package edu.uoc.ictflag.ela.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.model.CheckboxTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import edu.com.ictflag.security.bll.IUserActivityManager;
import edu.com.ictflag.security.bll.IUserManager;
import edu.uoc.ictflag.core.ListUtils;
import edu.uoc.ictflag.core.dal.SearchParameters;
import edu.uoc.ictflag.core.dal.SortArgument;
import edu.uoc.ictflag.core.localization.LocalizationController;
import edu.uoc.ictflag.core.localization.LocalizedItem;
import edu.uoc.ictflag.core.model.INameable;
import edu.uoc.ictflag.core.web.SessionBean;
import edu.uoc.ictflag.ela.bll.IExercisesReportManager;
import edu.uoc.ictflag.ela.bll.IReportManager;
import edu.uoc.ictflag.ela.model.Assignment;
import edu.uoc.ictflag.ela.model.Exercise;
import edu.uoc.ictflag.ela.model.ExercisesReportDataItem;
import edu.uoc.ictflag.ela.model.FilterField;
import edu.uoc.ictflag.ela.model.ReportDataItem;
import edu.uoc.ictflag.ela.model.Tool;
import edu.uoc.ictflag.institution.model.Course;
import edu.uoc.ictflag.institution.model.CourseGroup;
import edu.uoc.ictflag.institution.model.Program;
import edu.uoc.ictflag.institution.model.Semester;
import edu.uoc.ictflag.institution.model.Subject;
import edu.uoc.ictflag.security.model.User;
import edu.uoc.ictflag.security.model.UserActivity;

public abstract class ReportController<T extends ReportDataItem>
{
	protected abstract IReportManager<T> getReportManager();
	
	//protected abstract IUserManager getUserManager();
	
	@Inject
	IExercisesReportManager exercisesReportManager;
	
	@Inject
	IUserManager userManager;
	
	@Inject
	IUserActivityManager userActivityManager;
	
	protected List<LocalizedItem> tools;
	protected List<String> selectedTools;
	
	protected List<LocalizedItem> programs;
	protected List<String> selectedPrograms;
	
	protected List<LocalizedItem> subjects;
	protected List<String> selectedSubjects;
	
	protected List<LocalizedItem> courseGroups;
	protected List<String> selectedCourseGroups;
	
	protected List<LocalizedItem> semesters;
	protected List<String> selectedSemesters;
	
	protected TreeNode coursesAssignmentsTree;
	protected TreeNode[] selectedCoursesAssignmentsNodes;
	protected Map<String, List<String>> selectedCoursesAssignments;
	protected int totalCoursesAssignmentsCount;
	
	private TreeNode toolsExercisesTree;
	private TreeNode[] selectedToolsExercisesNodes;
	protected Map<String, List<String>> selectedToolsExercises;
	protected int totalToolsExercisesCount;
	
	protected List<LocalizedItem> dateFilterOptions;
	protected String selectedDateFilterOption;
	
	protected Date intervalStartDate;
	protected Date intervalEndDate;
	
	protected int maxAttempts;
	protected int attempts;
	protected String attemptsOperation;
	protected boolean onlyLastAttempt;
	
	protected List<LocalizedItem> groupFieldsOptions;
	protected List<String> groupFieldsSelectedOptions;
	
	protected List<String> groupFieldsExercises;
	
	protected boolean stackedData;
	
	protected int maxGroups = 10;
	protected boolean showAllGroups;
	
	protected int maxExercises = 10;
	protected boolean showAllExercises;
	
	protected long maxNumberOfOutcomes;
	protected long maxNumberOfAttempts;
	protected long maxNumberOfScores;
	protected double maxNumericGrade;
	protected long maxAlphabeticGrade;
	protected double maxTimeBetweenAttempts;
	protected double maxDuration;
	
	protected List<T> dataTableModel;
	
	protected Map<String, List<String>> filters;
	protected List<T> data;
	
	protected double maxLowestSuccessRate;
	private double maxLongestDuration;
	protected double maxHighestNumberOfAttempts;
	
	protected int lowestSuccessRateBarChartMinWidth;
	private int longestDurationBarChartMinWidth;
	protected int highestNumberOfAttemptsBarChartMinWidth;
	
	protected List<ExercisesReportDataItem> lowestSuccessRateExercises;
	private List<ExercisesReportDataItem> longestDurationExercises;
	protected List<ExercisesReportDataItem> highestNumberOfAttemptsExercises;
	
	protected BarChartModel lowestSuccessRateBarChartModel;
	private BarChartModel longestDurationBarChartModel;
	protected BarChartModel highestNumberOfAttemptsBarChartModel;
	
	protected ChartSeries lowestSuccessRateSeries;
	private ChartSeries longestDurationSeries;
	protected ChartSeries highestNumberOfAttemptsSeries;

	protected boolean canShowCharts;
	
	protected boolean canShowLowestSuccessRateBarChart;
	private boolean canShowLongestDurationBarChart;
	protected boolean canShowHighestNumberOfAttemptsBarChart;
	
	protected final int minColumnWidth = 30;
	
	public List<LocalizedItem> getGroupFieldsOptions()
	{
		return groupFieldsOptions;
	}
	
	public List<String> getGroupFieldsSelectedOptions()
	{
		return groupFieldsSelectedOptions;
	}
	
	public void setGroupFieldsSelectedOptions(List<String> groupFieldsSelectedOptions)
	{
		this.groupFieldsSelectedOptions = groupFieldsSelectedOptions;
	}
	
	public List<LocalizedItem> getTools()
	{
		return tools;
	}
	
	public void setTools(List<LocalizedItem> tools)
	{
		this.tools = tools;
	}
	
	public List<String> getSelectedTools()
	{
		return selectedTools;
	}
	
	public void setSelectedTools(List<String> selectedTools)
	{
		this.selectedTools = selectedTools;
	}
	
	public TreeNode getToolsExercisesTree()
	{
		return toolsExercisesTree;
	}
	
	public void setToolsExercisesTree(TreeNode toolsExercisesTree)
	{
		this.toolsExercisesTree = toolsExercisesTree;
	}
	
	public TreeNode[] getSelectedToolsExercisesNodes()
	{
		return selectedToolsExercisesNodes;
	}
	
	public void setSelectedToolsExercisesNodes(TreeNode[] selectedToolsExercisesNodes)
	{
		this.selectedToolsExercisesNodes = selectedToolsExercisesNodes;
	}
	
	public List<LocalizedItem> getPrograms()
	{
		return programs;
	}
	
	public void setPrograms(List<LocalizedItem> programs)
	{
		this.programs = programs;
	}
	
	public List<String> getSelectedPrograms()
	{
		return selectedPrograms;
	}
	
	public void setSelectedPrograms(List<String> selectedPrograms)
	{
		this.selectedPrograms = selectedPrograms;
	}
	
	public List<LocalizedItem> getSubjects()
	{
		return subjects;
	}
	
	public void setSubjects(List<LocalizedItem> subjects)
	{
		this.subjects = subjects;
	}
	
	public List<String> getSelectedSubjects()
	{
		return selectedSubjects;
	}
	
	public void setSelectedSubjects(List<String> selectedSubjects)
	{
		this.selectedSubjects = selectedSubjects;
	}
	
	public List<LocalizedItem> getSemesters()
	{
		return semesters;
	}
	
	public void setSemesters(List<LocalizedItem> semesters)
	{
		this.semesters = semesters;
	}
	
	public List<String> getSelectedSemesters()
	{
		return selectedSemesters;
	}
	
	public void setSelectedSemesters(List<String> selectedSemesters)
	{
		this.selectedSemesters = selectedSemesters;
	}
	
	public List<LocalizedItem> getCourseGroups()
	{
		return courseGroups;
	}
	
	public void setCourseGroups(List<LocalizedItem> courseGroups)
	{
		this.courseGroups = courseGroups;
	}
	
	public List<String> getSelectedCourseGroups()
	{
		return selectedCourseGroups;
	}
	
	public void setSelectedCourseGroups(List<String> selectedCourseGroups)
	{
		this.selectedCourseGroups = selectedCourseGroups;
	}
	
	public TreeNode getCoursesAssignmentsTree()
	{
		return coursesAssignmentsTree;
	}
	
	public void setCoursesAssignmentsTree(TreeNode coursesAssignmentsTree)
	{
		this.coursesAssignmentsTree = coursesAssignmentsTree;
	}
	
	public TreeNode[] getSelectedCoursesAssignmentsNodes()
	{
		return selectedCoursesAssignmentsNodes;
	}
	
	public void setSelectedCoursesAssignmentsNodes(TreeNode[] selectedCoursesAssignmentsNodes)
	{
		this.selectedCoursesAssignmentsNodes = selectedCoursesAssignmentsNodes;
	}
	
	public String getSelectedDateFilterOption()
	{
		return selectedDateFilterOption;
	}
	
	public void setSelectedDateFilterOption(String selectedDateFilterOption)
	{
		this.selectedDateFilterOption = selectedDateFilterOption;
	}
	
	public List<LocalizedItem> getDateFilterOptions()
	{
		return dateFilterOptions;
	}
	
	public void setDateFilterOptions(List<LocalizedItem> dateFilterOptions)
	{
		this.dateFilterOptions = dateFilterOptions;
	}
	
	public Date getIntervalStartDate()
	{
		return intervalStartDate;
	}
	
	public void setIntervalStartDate(Date intervalStartDate)
	{
		this.intervalStartDate = intervalStartDate;
	}
	
	public Date getIntervalEndDate()
	{
		return intervalEndDate;
	}
	
	public void setIntervalEndDate(Date intervalEndDate)
	{
		this.intervalEndDate = intervalEndDate;
	}
	
	public int getMaxGroups()
	{
		return maxGroups;
	}
	
	public void setMaxGroups(int maxGroups)
	{
		this.maxGroups = maxGroups;
	}
	
	public boolean getShowAllGroups()
	{
		return showAllGroups;
	}
	
	public void setShowAllGroups(boolean showAllGroups)
	{
		this.showAllGroups = showAllGroups;
	}
	
	public int getMaxExercises()
	{
		return maxExercises;
	}
	
	public void setMaxExercises(int maxExercises)
	{
		this.maxExercises = maxExercises;
	}
	
	public boolean getShowAllExercises()
	{
		return showAllExercises;
	}
	
	public void setShowAllExercises(boolean showAllExercises)
	{
		this.showAllExercises = showAllExercises;
	}
	
	public boolean getStackedData()
	{
		return stackedData;
	}
	
	public void setStackedData(boolean stackedData)
	{
		this.stackedData = stackedData;
	}
	
	public int getMaxAttempts()
	{
		return maxAttempts;
	}
	
	public void setMaxAttempts(int maxAttempts)
	{
		this.maxAttempts = maxAttempts;
	}
	
	public int getAttempts()
	{
		return attempts;
	}
	
	public void setAttempts(int attempts)
	{
		this.attempts = attempts;
	}
	
	public String getAttemptsOperation()
	{
		return attemptsOperation;
	}
	
	public void setAttemptsOperation(String attemptsOperation)
	{
		this.attemptsOperation = attemptsOperation;
	}
	
	public boolean getOnlyLastAttempt()
	{
		return onlyLastAttempt;
	}
	
	public void setOnlyLastAttempt(boolean onlyLastAttempt)
	{
		this.onlyLastAttempt = onlyLastAttempt;
	}
	
	public BarChartModel getLowestSuccessRateBarChartModel()
	{
		return lowestSuccessRateBarChartModel;
	}
	
	public BarChartModel getLongestDurationBarChartModel()
	{
		return longestDurationBarChartModel;
	}
	
	public BarChartModel getHighestNumberOfAttemptsBarChartModel()
	{
		return highestNumberOfAttemptsBarChartModel;
	}
	
	public int getLowestSuccessRateBarChartMinWidth()
	{
		return lowestSuccessRateBarChartMinWidth;
	}
	
	public int getLongestDurationBarChartMinWidth()
	{
		return longestDurationBarChartMinWidth;
	}
	
	public int getHighestNumberOfAttemptsBarChartMinWidth()
	{
		return highestNumberOfAttemptsBarChartMinWidth;
	}
	
	public boolean isCanShowCharts()
	{
		return canShowCharts;
	}
	
	public void setCanShowCharts(boolean canShowCharts)
	{
		this.canShowCharts = canShowCharts;
	}
	
	public boolean isCanShowLowestSuccessRateBarChart()
	{
		return canShowLowestSuccessRateBarChart;
	}
	
	public void setCanShowLowestSuccessRateBarChart(boolean canShowLowestSuccessRateBarChart)
	{
		this.canShowLowestSuccessRateBarChart = canShowLowestSuccessRateBarChart;
	}
	
	public boolean isCanShowLongestDurationBarChart()
	{
		return canShowLongestDurationBarChart;
	}
	
	public void setCanShowLongestDurationBarChart(boolean canShowLongestDurationBarChart)
	{
		this.canShowLongestDurationBarChart = canShowLongestDurationBarChart;
	}
	
	public boolean isCanShowHighestNumberOfAttemptsBarChart()
	{
		return canShowHighestNumberOfAttemptsBarChart;
	}
	
	public void setCanShowHighestNumberOfAttemptsBarChart(boolean canShowHighestNumberOfAttemptsBarChart)
	{
		this.canShowHighestNumberOfAttemptsBarChart = canShowHighestNumberOfAttemptsBarChart;
	}
	
	protected void setupToolsFilter() throws Exception
	{
		List<Tool> toolsList = getReportManager().getTools(SessionBean.getUsername());
		
		tools = new ArrayList<LocalizedItem>();
		
		selectedTools = setupFilterHelper(toolsList, tools, selectedTools);
	}
	
	protected void setupToolsExercisesFilter() throws Exception
	{
		if (selectedToolsExercisesNodes == null)
		{
			selectedToolsExercisesNodes = new TreeNode[0];
		}
		
		// CALCULATE SELECTED EXERCISES 		
		
		selectedToolsExercises = new HashMap<String, List<String>>();
		
		for (TreeNode tn : selectedToolsExercisesNodes)
		{
			if (tn.getData() instanceof Exercise)
			{
				String tool = ((Tool) tn.getParent().getData()).getId() + "";
				String exercise = ((Exercise) tn.getData()).getId() + "";
				
				List<String> exercises = null;
				
				if (!selectedToolsExercises.containsKey(tool))
				{
					exercises = new ArrayList<String>();
					
					selectedToolsExercises.put(tool, exercises);
				}
				else
				{
					exercises = selectedToolsExercises.get(tool);
				}
				
				exercises.add(exercise);
			}
		}
		
		// GET TOOLS / EXERCISES 		
		
		toolsExercisesTree = new CheckboxTreeNode("Tools", null);
		
		Map<Tool, List<Exercise>> toolExercisesList = getReportManager().getToolsWithExercises(SessionBean.getUsername());
		
		totalToolsExercisesCount = 0;
		
		boolean selectAll = !FacesContext.getCurrentInstance().isPostback() || selectedToolsExercisesNodes.length == 0;
		
		for (Tool tool : toolExercisesList.keySet())
		{
			List<Exercise> exercises = toolExercisesList.get(tool);
			
			if (tool == null)
			{
				tool = new Tool(); // NONE COURSE
				tool.setCode(LocalizationController.getLocalizedString("UNASSIGNED"));
				tool.setId(null);
			}
			
			CheckboxTreeNode tnode = new CheckboxTreeNode(tool, toolsExercisesTree);
			
			int childSelected = 0;
			
			List<String> selectedExercises = selectedToolsExercises.get(tool.getId() + "");
			
			for (Exercise exercise : exercises)
			{
				if (exercise == null)
				{
					exercise = new Exercise(); // NONE ASSIGNMENT
					exercise.setName(LocalizationController.getLocalizedString("UNASSIGNED"));
					exercise.setTool(tool);
					exercise.setId(null);
				}
				
				TreeNode enode = new CheckboxTreeNode(exercise, tnode);
				
				boolean selected = selectAll || (selectedExercises != null && selectedExercises.contains("" + exercise.getId()));
				
				enode.setSelected(selected);
				
				childSelected = selected ? childSelected + 1 : childSelected;
				
				totalToolsExercisesCount++;
			}
			
			if (selectAll || childSelected == exercises.size())
			{
				tnode.setSelected(true);
			}
			else if (childSelected > 0)
			{
				tnode.setPartialSelected(true);
			}
		}
	}
	
	//	protected void setupToolsExercisesFilter_OLD() throws Exception
	//	{
	//		if (selectedToolsExercises == null)
	//		{
	//			selectedToolsExercises = new TreeNode[0];
	//		}
	//		
	//		// CALCULATE SELECTED EXERCISES 		
	//		
	//		selectedTreeExercises = new ArrayList<String>();
	//		selectedTreeTools = new ArrayList<String>();
	//		
	//		for (TreeNode tn : selectedToolsExercises)
	//		{
	//			if (tn.getData() instanceof Exercise)
	//			{
	//				selectedTreeExercises.add(((Exercise) tn.getData()).getId() + "");
	//				
	//				String tool = ((Tool) tn.getParent().getData()).getId() + "";
	//				
	//				if (!selectedTreeTools.contains(tool))
	//				{
	//					selectedTreeTools.add(tool);
	//				}
	//			}
	//			else
	//			{
	//				selectedTreeTools.add(((Tool) tn.getData()).getId() + "");
	//			}
	//		}
	//		
	//		// GET TOOLS / EXERCISES 		
	//		
	//		toolsExercisesTree = new CheckboxTreeNode("Tools", null);
	//		
	//		Map<Tool, List<Exercise>> toolExercisesList = getReportManager().getToolsWithExercises(SessionBean.getUsername());
	//		
	//		totalTreeToolsCount = 0;
	//		totalTreeExercisesCount = 0;
	//		
	//		boolean selectAll = !FacesContext.getCurrentInstance().isPostback() || selectedToolsExercises.length == 0;
	//		
	//		for (Tool tool : toolExercisesList.keySet())
	//		{
	//			CheckboxTreeNode tnode = new CheckboxTreeNode(tool, toolsExercisesTree);
	//			
	//			int childSelected = 0;
	//			
	//			for (Exercise exercise : toolExercisesList.get(tool))
	//			{
	//				TreeNode enode = new CheckboxTreeNode(exercise, tnode);
	//				
	//				boolean selected = selectAll || selectedTreeExercises.contains("" + exercise.getId());
	//				
	//				enode.setSelected(selected);
	//				
	//				childSelected = selected ? childSelected + 1 : childSelected;
	//				
	//				totalTreeExercisesCount++;
	//			}
	//			
	//			if (selectAll || childSelected == toolExercisesList.get(tool).size())
	//			{
	//				tnode.setSelected(true);
	//			}
	//			else if (childSelected > 0)
	//			{
	//				tnode.setPartialSelected(true);
	//			}
	//			
	//			totalTreeToolsCount++;
	//		}
	//	}
	
	protected void setupProgramsFilter() throws Exception
	{
		List<Program> programsList = getReportManager().getPrograms(SessionBean.getUsername());
		
		programs = new ArrayList<LocalizedItem>();
		
		selectedPrograms = setupFilterHelper(programsList, programs, selectedPrograms);
	}
	
	protected void setupSubjectsFilter() throws Exception
	{
		List<Subject> subjectsList = getReportManager().getSubjects(SessionBean.getUsername());
		
		subjects = new ArrayList<LocalizedItem>();
		
		selectedSubjects = setupFilterHelper(subjectsList, subjects, selectedSubjects);
	}
	
	protected void setupCourseGroupsFilter() throws Exception
	{
		List<CourseGroup> courseGroupsList = getReportManager().getCourseGroups(SessionBean.getUsername());
		
		courseGroups = new ArrayList<LocalizedItem>();
		
		selectedCourseGroups = setupFilterHelper(courseGroupsList, courseGroups, selectedCourseGroups);
	}
	
	protected void setupSemestersFilter() throws Exception
	{
		List<Semester> semestersList = getReportManager().getSemesters(SessionBean.getUsername());
		
		semesters = new ArrayList<LocalizedItem>();
		
		selectedSemesters = setupFilterHelper(semestersList, semesters, selectedSemesters);
	}
	
	protected <N extends INameable> List<String> setupFilterHelper(List<N> list, List<LocalizedItem> localizedItems, List<String> selectedItems)
			throws Exception
	{
		boolean selectAll = !FacesContext.getCurrentInstance().isPostback() || selectedItems.size() == 0;
		
		if (selectAll)
		{
			selectedItems = new ArrayList<String>();
		}
		
		for (N t : list)
		{
			if (t == null)
			{
				localizedItems.add(new LocalizedItem("null", LocalizationController.getLocalizedString("UNASSIGNED")));
				
				if (selectAll)
				{
					selectedItems.add("null");
				}
			}
			else
			{
				localizedItems.add(new LocalizedItem(t.getId().toString(), t.getName().getTranslation()));
				
				if (selectAll)
				{
					selectedItems.add(t.getId().toString());
				}
			}
		}
		
		sortLocalizedList(localizedItems);
		
		return selectedItems;
	}
	
	protected void sortLocalizedList(List<LocalizedItem> list)
	{
		Collections.sort(list, new Comparator<LocalizedItem>()
		{
			@Override
			public int compare(LocalizedItem o1, LocalizedItem o2)
			{
				if (o1.getValue() == null) return -1;
				if (o2.getValue() == null) return 1;
				else return o1.getDisplayName().compareTo(o2.getDisplayName());
			}
		});
	}
	
	protected void setupCoursesAssignmentsFilter() throws Exception
	{
		if (selectedCoursesAssignmentsNodes == null)
		{
			selectedCoursesAssignmentsNodes = new TreeNode[0];
		}
		
		// CALCULATE SELECTED ASSIGNMENTS 		
		
		selectedCoursesAssignments = new HashMap<String, List<String>>();
		
		for (TreeNode tn : selectedCoursesAssignmentsNodes)
		{
			if (tn.getData() instanceof Assignment)
			{
				String course = ((Course) tn.getParent().getData()).getId() + "";
				String assignment = ((Assignment) tn.getData()).getId() + "";
				
				List<String> assignments = null;
				
				if (!selectedCoursesAssignments.containsKey(course))
				{
					assignments = new ArrayList<String>();
					
					selectedCoursesAssignments.put(course, assignments);
				}
				else
				{
					assignments = selectedCoursesAssignments.get(course);
				}
				
				assignments.add(assignment);
			}
		}
		
		// GET COURSES / ASSIGNMENTS 		
		
		coursesAssignmentsTree = new CheckboxTreeNode("Courses", null);
		
		Map<Course, List<Assignment>> courseAssignmentsList = getReportManager().getCoursesWithAssignments(SessionBean.getUsername());
		
		totalCoursesAssignmentsCount = 0;
		
		boolean selectAll = !FacesContext.getCurrentInstance().isPostback() || selectedCoursesAssignmentsNodes.length == 0;
		
		for (Course course : courseAssignmentsList.keySet())
		{
			List<Assignment> assignments = courseAssignmentsList.get(course);
			
			if (course == null)
			{
				course = new Course(); // NONE COURSE
				course.setCode(LocalizationController.getLocalizedString("UNASSIGNED"));
				course.setId(null);
			}
			
			CheckboxTreeNode tnode = new CheckboxTreeNode(course, coursesAssignmentsTree);
			
			int childSelected = 0;
			
			List<String> selectedAssignments = selectedCoursesAssignments.get(course.getId() + "");
			
			for (Assignment assignment : assignments)
			{
				if (assignment == null)
				{
					assignment = new Assignment(); // NONE ASSIGNMENT
					assignment.setName(LocalizationController.getLocalizedString("UNASSIGNED"));
					assignment.setCourse(course);
					assignment.setId(null);
				}
				
				TreeNode enode = new CheckboxTreeNode(assignment, tnode);
				
				boolean selected = selectAll || (selectedAssignments != null && selectedAssignments.contains("" + assignment.getId()));
				
				enode.setSelected(selected);
				
				childSelected = selected ? childSelected + 1 : childSelected;
				
				totalCoursesAssignmentsCount++;
			}
			
			if (selectAll || childSelected == assignments.size())
			{
				tnode.setSelected(true);
			}
			else if (childSelected > 0)
			{
				tnode.setPartialSelected(true);
			}
		}
	}
	
	protected void setupAttemptsFilter() throws Exception
	{
		maxAttempts = getReportManager().getMaxNumberOfAttempts(SessionBean.getUsername());
		
		if (!FacesContext.getCurrentInstance().isPostback())
		{
			attemptsOperation = "<=";
			attempts = maxAttempts;
		}
	}
	
	protected void setupDateFilterOptions()
	{
		if (!FacesContext.getCurrentInstance().isPostback())
		{
			selectedDateFilterOption = "AnyTime";
			intervalStartDate = new Date();
			intervalEndDate = new Date();
		}
		
		dateFilterOptions = new ArrayList<LocalizedItem>();
		dateFilterOptions.add(new LocalizedItem("AnyTime", LocalizationController.getLocalizedString("AnyTime")));
		dateFilterOptions.add(new LocalizedItem("LastWeek", LocalizationController.getLocalizedString("LastWeek")));
		dateFilterOptions.add(new LocalizedItem("LastMonth", LocalizationController.getLocalizedString("LastMonth")));
		dateFilterOptions.add(new LocalizedItem("Interval", LocalizationController.getLocalizedString("Interval")));
	}
	
	public void applyFilters()
	{
		// DO NOT REMOVE THIS METHOD. IT HANDLES THE APPLY BUTTON CLICK
	}
	
	protected abstract List<String> getGroupOptions();
	
	protected void setupGroupFieldsOptions() throws Exception
	{
		groupFieldsOptions = new ArrayList<LocalizedItem>();
		
		getGroupOptions().stream().forEach(x -> groupFieldsOptions.add(new LocalizedItem(x, LocalizationController.getLocalizedString(x))));
		
		if (!FacesContext.getCurrentInstance().isPostback())
		{
			User user = userManager.getUser(SessionBean.getUsername());
			
			// PRE-PROCESS filter and group by list
			switch (user.getSelectedRole())
			{
				case SUPERUSER:
				case ADMIN:
					
					groupFieldsSelectedOptions = new ArrayList<String>();
					
					break;
				
				case PROGRAM_MANAGER:
					
					groupFieldsSelectedOptions = new ArrayList<String>(Arrays.asList(new String[] { FilterField.PROGRAM, FilterField.SUBJECT }));
					
					break;
				
				case LECTURER:
					
					groupFieldsSelectedOptions = new ArrayList<String>(
							Arrays.asList(new String[] { FilterField.PROGRAM, FilterField.SUBJECT, FilterField.COURSE }));
					
					break;
				
				case INSTRUCTOR:
				case STUDENT:
					
					groupFieldsSelectedOptions = new ArrayList<String>(
							Arrays.asList(new String[] { FilterField.PROGRAM, FilterField.SUBJECT, FilterField.COURSE, FilterField.COURSEGROUP }));
					
					break;
				
				default:
					
					break;
			}
		}
	}
	
	protected String groupFieldsOptionToDataTableLabel(String groupFieldsOption)
	{
		switch (groupFieldsOption)
		{
			case FilterField.TOOL:
				
				return LocalizationController.getLocalizedString("Tool");
			
			case FilterField.PROGRAM:
				
				return LocalizationController.getLocalizedString("Program");
			
			case FilterField.SUBJECT:
				
				return LocalizationController.getLocalizedString("Subject");
			
			case FilterField.COURSE:
				
				return LocalizationController.getLocalizedString("Course");
			
			case FilterField.COURSEGROUP:
				
				return LocalizationController.getLocalizedString("CourseGroup");
			
			case FilterField.ASSIGNMENT:
				
				return LocalizationController.getLocalizedString("Assignment");
			
			case FilterField.EXERCISE:
				
				return LocalizationController.getLocalizedString("Exercise");
			
			case FilterField.SEMESTER:
				
				return LocalizationController.getLocalizedString("Semester");
		}
		
		return "";
	}
	
	protected void setupCharts() throws Exception
	{
		// TABLE PRE CONFIGURATION
		
		dataTableModel = new ArrayList<T>();
		
		// CHARTS PRE CONFIGURATION
		
		chartsPreConfiguration();
		
		topChartsPreConfiguration();
		
		// APPLY FILTERS, GET DATA 
		
		setupFilters();
		
		getData();
		
		// DO CALCULATIONS AND SETUP CHART SERIES		
		
		processData();
		
		processTopChartsData();
		
		chartsPostConfiguration(data);
		
		topChartsPostConfiguration();
	}
	
	protected void topChartsPreConfiguration()
	{
		// LOWEST SUCCESS RATE EXERCISES CHART PRE CONFIGURATION
		
		lowestSuccessRateBarChartModel = new BarChartModel();
		lowestSuccessRateBarChartModel.setStacked(false);
		
		lowestSuccessRateSeries = new ChartSeries();
		lowestSuccessRateSeries.setLabel(LocalizationController.getLocalizedString("SuccessRate"));
		
		lowestSuccessRateBarChartModel.addSeries(lowestSuccessRateSeries);
		
		// LONGEST TIME EXERCISES CHART PRE CONFIGURATION
		
		longestDurationBarChartModel = new BarChartModel();
		longestDurationBarChartModel.setStacked(false);
		
		longestDurationSeries = new ChartSeries();
		longestDurationSeries.setLabel(LocalizationController.getLocalizedString("LongestDuration"));
		
		longestDurationBarChartModel.addSeries(longestDurationSeries);
		
		// HIGHEST NUMBER OF ATTEMPTS EXERCISES CHART PRE CONFIGURATION
		
		highestNumberOfAttemptsBarChartModel = new BarChartModel();
		highestNumberOfAttemptsBarChartModel.setStacked(false);
		
		highestNumberOfAttemptsSeries = new ChartSeries();
		highestNumberOfAttemptsSeries.setLabel(LocalizationController.getLocalizedString("HighestNumberOfAttempts"));
		
		highestNumberOfAttemptsBarChartModel.addSeries(highestNumberOfAttemptsSeries);
	}
	
	protected void topChartsPostConfiguration()
	{
		// LOWEST SUCCESS RATE EXERCISES CHART POST CONFIGURATION
		
		int items = showAllExercises ? lowestSuccessRateExercises.size() : Math.min(maxExercises, lowestSuccessRateExercises.size());
		
		int minWidth = items * minColumnWidth;
		
		boolean rotateLabels = lowestSuccessRateExercises.size() > 5;
		
		setupVerticalBarChartAxis(lowestSuccessRateBarChartModel, LocalizationController.getLocalizedString("LowestSuccessRateExercises"), "nw",
				rotateLabels, 0, (float) Math.round(maxLowestSuccessRate * 1.1), "%d%");
		
		lowestSuccessRateBarChartMinWidth = minWidth;
		
		// LONGEST TIME EXERCISES CHART POST CONFIGURATION
		
		items = showAllExercises ? longestDurationExercises.size() : Math.min(maxExercises, longestDurationExercises.size());
		
		minWidth = items * minColumnWidth;
		
		rotateLabels = longestDurationExercises.size() > 5;
		
		setupVerticalBarChartAxis(longestDurationBarChartModel, LocalizationController.getLocalizedString("LongestDurationExercises"), "ne",
				rotateLabels, 0, (float) Math.round(maxLongestDuration * 1.1), "%d'");
		
		longestDurationBarChartMinWidth = minWidth;
		
		// HIGHEST NUMBER OF ATTEMPTS EXERCISES CHART POST CONFIGURATION
		
		items = showAllExercises ? highestNumberOfAttemptsExercises.size() : Math.min(maxExercises, highestNumberOfAttemptsExercises.size());
		
		minWidth = items * minColumnWidth;
		
		rotateLabels = highestNumberOfAttemptsExercises.size() > 5;
		
		setupVerticalBarChartAxis(highestNumberOfAttemptsBarChartModel,
				LocalizationController.getLocalizedString("HighestNumberOfAttemptsExercises"), "ne", rotateLabels, 0,
				(float) Math.round(maxHighestNumberOfAttempts * 1.1), "%d");
		
		highestNumberOfAttemptsBarChartMinWidth = minWidth;
	}
	
	protected abstract void chartsPreConfiguration();
	
	protected <S extends ReportDataItem> void calculateDataItemLabel(S dataItem)
	{
		calculateDataItemLabel(dataItem, groupFieldsSelectedOptions);
	}
	
	protected <S extends ReportDataItem> void calculateDataItemLabel(S dataItem, List<String> groupFieldsSelectedOptions)
	{
		String chartLabel = LocalizationController.getLocalizedString("Total");
		String tableLabel = LocalizationController.getLocalizedString("Total");
		
		String NONE = LocalizationController.getLocalizedString("UNASSIGNED");
		
		List<String> groupFields = dataItem.getGroupFields();
		
		if (groupFields != null && groupFields.size() > 0)
		{
			chartLabel = String.join(" - ", groupFields.stream().map(x -> x == null ? NONE : x).collect(Collectors.toList()));

			tableLabel = String.join("\n",
					IntStream.range(0, groupFields.size())
					.mapToObj(x -> groupFieldsOptionToDataTableLabel(groupFieldsSelectedOptions.get(x)) + ": "
							+ (groupFields.get(x) == null ? NONE : groupFields.get(x)))
					.collect(Collectors.toList()));
		}
		
		dataItem.setChartLabel(chartLabel);
		dataItem.setTableLabel(tableLabel);
	}
	
	protected abstract void setupDataInCharts(T dataItem);
	
	protected abstract void chartsPostConfiguration(List<T> data);
	
	protected abstract List<String> getFiltersToSetup();
	
	protected void setupFilters() throws Exception
	{
		List<String> filtersToSetup = getFiltersToSetup();
		
		filters = new HashMap<String, List<String>>();
		
		filtersToSetup.stream().forEach(x -> setupFilter(x, filters));
	}
	
	private void setupFilter(String filter, Map<String, List<String>> filters)
	{
		switch (filter)
		{
			case FilterField.TOOL:
				
				if (selectedTools != null && selectedTools.size() != tools.size()) // NOT ALL SELECTED
				{
					filters.put(FilterField.TOOL, selectedTools);
				}
				else
				{
					filters.put(FilterField.TOOL, new ArrayList<String>());
				}
				
				break;
			
			case FilterField.PROGRAM:
				
				if (selectedPrograms.size() != programs.size()) // NOT ALL SELECTED
				{
					filters.put(FilterField.PROGRAM, selectedPrograms);
				}
				else
				{
					filters.put(FilterField.PROGRAM, new ArrayList<String>());
				}
				
				break;
			
			case FilterField.SUBJECT:
				
				if (selectedSubjects.size() != subjects.size()) // NOT ALL SELECTED
				{
					filters.put(FilterField.SUBJECT, selectedSubjects);
				}
				else
				{
					filters.put(FilterField.SUBJECT, new ArrayList<String>());
				}
				
				break;
			
			case FilterField.COURSE:
				
				//		if (selectedCourses.size() != totalCoursesCount) // NOT ALL SELECTED
				//		{
				//			filters.put(FilterField.COURSE, selectedCourses);
				//		}
				//		else
				//		{
				//			filters.put(FilterField.COURSE, new ArrayList<String>());
				//		}
				
				break;
			
			case FilterField.COURSEGROUP:
				
				if (selectedCourseGroups.size() != courseGroups.size()) // NOT ALL SELECTED
				{
					filters.put(FilterField.COURSEGROUP, selectedCourseGroups);
				}
				else
				{
					filters.put(FilterField.COURSEGROUP, new ArrayList<String>());
				}
				
				break;

			case FilterField.SEMESTER:
				
				if (selectedSemesters.size() != semesters.size()) // NOT ALL SELECTED
				{
					filters.put(FilterField.SEMESTER, selectedSemesters);
				}
				else
				{
					filters.put(FilterField.SEMESTER, new ArrayList<String>());
				}
				
				break;
			
			case FilterField.TOOL_EXERCISE:
				
				List<String> toolsExercises = ListUtils.flattenStringList(selectedToolsExercises);
				
				if (toolsExercises.size() != totalToolsExercisesCount) // NOT ALL SELECTED
				{
					filters.put(FilterField.TOOL_EXERCISE, toolsExercises);
				}
				else
				{
					filters.put(FilterField.TOOL_EXERCISE, new ArrayList<String>());
				}
				
				break;
			
			case FilterField.COURSE_ASSIGNMENT:
				
				List<String> coursesAssignments = ListUtils.flattenStringList(selectedCoursesAssignments);
				
				if (coursesAssignments.size() != totalCoursesAssignmentsCount) // NOT ALL SELECTED
				{
					filters.put(FilterField.COURSE_ASSIGNMENT, coursesAssignments);
				}
				else
				{
					filters.put(FilterField.COURSE_ASSIGNMENT, new ArrayList<String>());
				}
				
				break;
			
			case FilterField.DATE:
				
				if (!selectedDateFilterOption.equals("AnyTime")) // NOT ANY TIME SELECTED
				{
					String startDate = "";
					String endDate = "";
					
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					Calendar cal = Calendar.getInstance();
					
					if (selectedDateFilterOption.equals("LastWeek"))
					{
						endDate = dateFormat.format(cal.getTime());
						
						cal.add(Calendar.DATE, -7);
						
						startDate = dateFormat.format(cal.getTime());
					}
					else if (selectedDateFilterOption.equals("LastMonth"))
					{
						endDate = dateFormat.format(cal.getTime());
						
						cal.add(Calendar.MONTH, -1);
						
						startDate = dateFormat.format(cal.getTime());
					}
					else if (selectedDateFilterOption.equals("Interval"))
					{
						startDate = dateFormat.format(intervalStartDate);
						endDate = dateFormat.format(intervalEndDate);
					}
					
					filters.put(FilterField.DATE, Arrays.asList(new String[] { startDate, endDate }));
				}
				
				break;
			
			case FilterField.ATTEMPTS:
				
				filters.put(FilterField.ATTEMPTS, Arrays.asList(new String[] { attemptsOperation, "" + attempts, "" + onlyLastAttempt }));
				
				break;
		}
	}
	
	protected void getData() throws Exception
	{
		SearchParameters searchParameters = new SearchParameters();

		searchParameters.setFilters(filters);

		searchParameters.setGroupFields(groupFieldsSelectedOptions);
		
		/////
		
		StringBuilder sb = new StringBuilder();
		
		for (String filter : filters.keySet())
		{
			sb.append("FILTER: ");
			sb.append(filter);
			sb.append(" - ");
			sb.append(String.join(",", filters.get(filter)));
			sb.append("\n");
		}
		
		sb.append("\n");
		
		for (String groupBy : groupFieldsSelectedOptions)
		{
			sb.append("GROUP BY: ");
			sb.append(groupBy);
			sb.append("\n");
		}
		
		userActivityManager.createUserActivity(new UserActivity(SessionBean.getUsername(), new Date(), "report filters", sb.toString()));
		
		/////
		
		data = getReportManager().getReportData(SessionBean.getUsername(), searchParameters);
		
		getTopChartsData(searchParameters);
	}
	
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
		
		// LONGEST TIME 
		
		sortArguments = new ArrayList<SortArgument>();
		sortArguments.add(new SortArgument(FilterField.MAXDURATION, true));
		searchParameters.setSortArguments(sortArguments);
		
		longestDurationExercises = exercisesReportManager.getReportData(SessionBean.getUsername(), searchParameters);
		
		// HIGHEST NUMBER OF ATTEMPTS 
		
		sortArguments = new ArrayList<SortArgument>();
		sortArguments.add(new SortArgument(FilterField.MAXATTEMPTS, true));
		searchParameters.setSortArguments(sortArguments);
		
		highestNumberOfAttemptsExercises = exercisesReportManager.getReportData(SessionBean.getUsername(), searchParameters);
	}
	
	protected void processData()
	{
		int index = 0;
		
		maxNumericGrade = 0;
		maxAlphabeticGrade = 0;
		
		maxNumberOfOutcomes = 0;
		maxNumberOfAttempts = 0;
		maxTimeBetweenAttempts = 0;
		maxDuration = 0;
		
		for (T dataItem : data)
		{
			calculateDataItemLabel(dataItem);
			
			if (showAllGroups || index <= maxGroups) // DRAW THE GROUP IN GRAPHS ONLY IF NOT EXCEDES THE FILTER 
			{
				setupDataInCharts(dataItem);
			}
			
			dataTableModel.add(dataItem);
			
			index++;
		}
		
		canShowCharts = data.size() > 0;
	}
	
	protected void processTopChartsData()
	{
		// TOP X EXERCICES
		
		maxLowestSuccessRate = 0;
		maxLongestDuration = 0;
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
		
		for (ExercisesReportDataItem dataItem : longestDurationExercises)
		{
			calculateDataItemLabel(dataItem, groupFieldsExercises);
			
			if (showAllExercises || index <= maxExercises) // DRAW THE EXERCISE IN GRAPHS ONLY IF NOT EXCEDES THE FILTER 
			{
				longestDurationSeries.set(dataItem.getChartLabel(), dataItem.getMaxDuration());
				
				maxLongestDuration = Math.max(maxLongestDuration, dataItem.getMaxDuration());
			}
			else
			{
				break;
			}
			
			index++;
		}
		
		canShowLongestDurationBarChart = longestDurationExercises.size() > 0;
		
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
	
	protected void setupVerticalBarChartAxis(BarChartModel chart, String title, String legendPosition, boolean rotateLabels, float min, float max,
			String tickFormat)
	{
		setupBarChartAxis(chart, chart.getAxis(AxisType.X), chart.getAxis(AxisType.Y), title, legendPosition, rotateLabels, min, max, tickFormat);
		
		chart.setExtender("verticalChartExtender");
	}
	
	protected void setupHorizontalBarChartAxis(BarChartModel chart, String title, String legendPosition, boolean rotateLabels, float min, float max,
			String tickFormat)
	{
		setupBarChartAxis(chart, chart.getAxis(AxisType.Y), chart.getAxis(AxisType.X), title, legendPosition, rotateLabels, min, max, tickFormat);
		
		chart.setExtender("horizontalChartExtender");
	}
	
	protected void setupBarChartAxis(BarChartModel chart, Axis xAxis, Axis yAxis, String title, String legendPosition, boolean rotateLabels,
			float min, float max, String tickFormat)
	{
		//chart.setTitle(title);
		chart.setLegendPosition(legendPosition);
		
		//xAxis.setLabel(LocalizationController.getLocalizedString("Outcome"));
		
		if (rotateLabels)
		{
			xAxis.setTickAngle(-90);
		}
		
		// yAxis.setLabel("#");
		yAxis.setMin(min);
		yAxis.setMax(max);
		yAxis.setTickFormat(tickFormat);
	}
	
	public String replaceQuotes(String str)
	{
		return str.replace("&apos;", "'");
	}
}
