package edu.uoc.ictflag.ela.model;

public class StudentReportGlobalProgressItem
{
	private long[] globalProgressLevelMaxValues;
	private long globalProgressCurrentValue;

	public long[] getGlobalProgressLevelMaxValues()
	{
		return globalProgressLevelMaxValues;
	}

	public void setGlobalProgressLevelMaxValues(long[] globalProgressLevelMaxValues)
	{
		this.globalProgressLevelMaxValues = globalProgressLevelMaxValues;
	}

	public long getGlobalProgressCurrentValue()
	{
		return globalProgressCurrentValue;
	}

	public void setGlobalProgressCurrentValue(long globalProgressCurrentValue)
	{
		this.globalProgressCurrentValue = globalProgressCurrentValue;
	}
	
	public long getGlobalProgressLevelMaxValue(int level)
	{
		long levelMaxValue = 0;
		
		if (level >= 0 && level < globalProgressLevelMaxValues.length)
		{
			levelMaxValue = globalProgressLevelMaxValues[level];
		}
		
		return levelMaxValue;
	}
}
