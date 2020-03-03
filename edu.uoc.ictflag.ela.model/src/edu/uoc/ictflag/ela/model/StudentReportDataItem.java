package edu.uoc.ictflag.ela.model;

public class StudentReportDataItem
{
	private String student;
	private long recentExercises;
	private long exercisesYesterday;
	
	private StudentReportDataForToolItem dataForVerilUOC;
	private StudentReportDataForToolItem dataForVerilCirc;
	private StudentReportDataForToolItem dataForKeMap;
	private StudentReportDataForToolItem dataForVerilChart;
	
	private StudentReportGlobalProgressItem globalProgress;
	
	public StudentReportDataForToolItem getDataForVerilUOC()
	{
		return dataForVerilUOC;
	}
	
	public void setDataForVerilUOC(StudentReportDataForToolItem dataForVerilUOC)
	{
		this.dataForVerilUOC = dataForVerilUOC;
	}
	
	public StudentReportDataForToolItem getDataForVerilCirc()
	{
		return dataForVerilCirc;
	}
	
	public void setDataForVerilCirc(StudentReportDataForToolItem dataForVerilCirc)
	{
		this.dataForVerilCirc = dataForVerilCirc;
	}
	
	public StudentReportDataForToolItem getDataForKeMap()
	{
		return dataForKeMap;
	}
	
	public void setDataForKeMap(StudentReportDataForToolItem dataForKeMap)
	{
		this.dataForKeMap = dataForKeMap;
	}
	
	public StudentReportDataForToolItem getDataForVerilChart()
	{
		return dataForVerilChart;
	}
	
	public void setDataForVerilChart(StudentReportDataForToolItem dataForVerilChart)
	{
		this.dataForVerilChart = dataForVerilChart;
	}

	public long getRecentExercises()
	{
		return recentExercises;
	}
	
	public void setRecentExercises(long recentExercises)
	{
		this.recentExercises = recentExercises;
	}
	
	public long getExercisesYesterday()
	{
		return exercisesYesterday;
	}
	
	public void setExercisesYesterday(long exercisesYesterday)
	{
		this.exercisesYesterday = exercisesYesterday;
	}
	
	public StudentReportGlobalProgressItem getGlobalProgress()
	{
		return globalProgress;
	}

	public void setGlobalProgress(StudentReportGlobalProgressItem globalProgress)
	{
		this.globalProgress = globalProgress;
	}
	
	public String getStudent()
	{
		return student;
	}
	
	public void setStudent(String student)
	{
		this.student = student;
	}
}
