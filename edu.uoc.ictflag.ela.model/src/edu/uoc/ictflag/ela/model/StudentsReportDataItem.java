package edu.uoc.ictflag.ela.model;

import java.util.ArrayList;

public class StudentsReportDataItem extends ReportDataItem
{
	double failureRate;
	long rightExercises;
	long attempts;
	
	long groupCount;
	
	public StudentsReportDataItem(Object... args)
	{
		int index = 0;
		
		this.failureRate = args[index] == null ? 0 : (double) args[index];
		index++;
		this.rightExercises = args[index] == null ? 0 : (long) args[index];
		index++;
		this.attempts = args[index] == null ? 0 : (long) args[index];
		index++;
		
		this.groupCount = this.attempts;
		
		this.groupFields = new ArrayList<String>();
		
		for (; index < args.length; index++)
		{
			this.groupFields.add((String) args[index]);
		}
	}
	
	public double getFailureRate()
	{
		return failureRate;
	}
	
	public void setFailureRate(double failureRate)
	{
		this.failureRate = failureRate;
	}
	
	public long getRightExercises()
	{
		return rightExercises;
	}
	
	public void setRightExercises(long rightExercises)
	{
		this.rightExercises = rightExercises;
	}
	
	public long getAttempts()
	{
		return attempts;
	}
	
	public void setAttempts(long attempts)
	{
		this.attempts = attempts;
	}
	
	public long getGroupCount()
	{
		return groupCount;
	}
	
	public void setGroupCount(long groupCount)
	{
		this.groupCount = groupCount;
	}
}
