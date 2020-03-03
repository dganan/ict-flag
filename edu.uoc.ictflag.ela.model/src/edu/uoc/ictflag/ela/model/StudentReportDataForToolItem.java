package edu.uoc.ictflag.ela.model;

public class StudentReportDataForToolItem
{
	private long totalSubmissions;
	private long totalSubmissionsPercentage;
	private long totalExercises;
	private long totalExercisesPercentage;
	private long completedExercises;
	private long completedExercisesPercentage;
	private StudentReportDataSemaphore completedExercisesSemaphore;
	private int completedExercisesTendence;
	
	public long getTotalSubmissions()
	{
		return totalSubmissions;
	}
	
	public void setTotalSubmissions(long totalSubmissions)
	{
		this.totalSubmissions = totalSubmissions;
	}
	
	public long getTotalSubmissionsPercentage()
	{
		return totalSubmissionsPercentage;
	}
	
	public void setTotalSubmissionsPercentage(long totalSubmissionsPercentage)
	{
		this.totalSubmissionsPercentage = totalSubmissionsPercentage;
	}
	
	public long getTotalExercises()
	{
		return totalExercises;
	}
	
	public void setTotalExercises(long totalExercises)
	{
		this.totalExercises = totalExercises;
	}
	
	public long getTotalExercisesPercentage()
	{
		return totalExercisesPercentage;
	}
	
	public void setTotalExercisesPercentage(long totalExercisesPercentage)
	{
		this.totalExercisesPercentage = totalExercisesPercentage;
	}
	
	public long getCompletedExercises()
	{
		return completedExercises;
	}
	
	public void setCompletedExercises(long completedExercises)
	{
		this.completedExercises = completedExercises;
	}
	
	public long getCompletedExercisesPercentage()
	{
		return completedExercisesPercentage;
	}
	
	public void setCompletedExercisesPercentage(long completedExercisesPercentage)
	{
		this.completedExercisesPercentage = completedExercisesPercentage;
	}
	
	public int getCompletedExercisesTendence()
	{
		return completedExercisesTendence;
	}

	public void setCompletedExercisesTendence(int completedExercisesTendence)
	{
		this.completedExercisesTendence = completedExercisesTendence;
	}
	
	public StudentReportDataSemaphore getCompletedExercisesSemaphore()
	{
		return completedExercisesSemaphore;
	}
	
	public void setCompletedExercisesSemaphore(StudentReportDataSemaphore completedExercisesSemaphore)
	{
		this.completedExercisesSemaphore = completedExercisesSemaphore;
	}
}
