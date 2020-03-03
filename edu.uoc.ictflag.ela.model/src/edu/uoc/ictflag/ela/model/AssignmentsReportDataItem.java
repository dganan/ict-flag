package edu.uoc.ictflag.ela.model;

import java.math.BigDecimal;
import java.util.ArrayList;

public class AssignmentsReportDataItem extends ReportDataItem
{
	long right;
	long wrong;
	long timeout;
	long error;
	
	long minAttempts;
	double avgAttempts;
	long maxAttempts;
	
	double minNumericGrade;
	double avgNumericGrade;
	double maxNumericGrade;
	
	long alphabeticGradeA;
	long alphabeticGradeB;
	long alphabeticGradeCPlus;
	long alphabeticGradeCMinus;
	long alphabeticGradeD;
	
	double minTimeBetweenAttempts;
	double avgTimeBetweenAttempts;
	double maxTimeBetweenAttempts;
	
	double minDuration;
	double avgDuration;
	double maxDuration;
	
	long groupCount;
	double successRate;
	
	public AssignmentsReportDataItem(Object... args)
	{
		int index = 0;
		
		this.minNumericGrade = args[index] == null ? 0 : (double) args[index];
		index++;
		this.avgNumericGrade = args[index] == null ? 0 : (double) args[index];
		index++;
		this.maxNumericGrade = args[index] == null ? 0 : (double) args[index];
		index++;
		
		this.alphabeticGradeA = args[index] == null ? 0 : (long) args[index];
		index++;
		this.alphabeticGradeB = args[index] == null ? 0 : (long) args[index];
		index++;
		this.alphabeticGradeCPlus = args[index] == null ? 0 : (long) args[index];
		index++;
		this.alphabeticGradeCMinus = args[index] == null ? 0 : (long) args[index];
		index++;
		this.alphabeticGradeD = args[index] == null ? 0 : (long) args[index];
		index++;

		this.right = args[index] == null ? 0 : ((BigDecimal) args[index]).longValue();
		index++;
		this.wrong = args[index] == null ? 0 : ((BigDecimal) args[index]).longValue();
		index++;
		this.timeout = args[index] == null ? 0 : ((BigDecimal) args[index]).longValue();
		index++;
		this.error = args[index] == null ? 0 : ((BigDecimal) args[index]).longValue();
		index++;
		
		this.minDuration = args[index] == null ? 0 : ((BigDecimal) args[index]).doubleValue();
		index++;
		this.avgDuration = args[index] == null ? 0 : ((BigDecimal) args[index]).doubleValue();
		index++;
		this.maxDuration = args[index] == null ? 0 : ((BigDecimal) args[index]).doubleValue();
		index++;
		
		this.groupFields = new ArrayList<String>();
		
		for (; index < args.length; index++)
		{
			this.groupFields.add((String) args[index]);
		}
		
		this.groupCount = right + wrong + timeout + error;
		
		long gradesCount = alphabeticGradeA + alphabeticGradeB + alphabeticGradeCPlus + alphabeticGradeCMinus + alphabeticGradeD;
		
		this.successRate = gradesCount == 0 ? 0 : ((double) (alphabeticGradeA + alphabeticGradeB + alphabeticGradeCPlus) / (double) gradesCount);
	}
	
	public long getRight()
	{
		return right;
	}
	
	public void setRight(long right)
	{
		this.right = right;
	}
	
	public long getWrong()
	{
		return wrong;
	}
	
	public void setWrong(long wrong)
	{
		this.wrong = wrong;
	}
	
	public long getTimeout()
	{
		return timeout;
	}
	
	public void setTimeout(long timeout)
	{
		this.timeout = timeout;
	}
	
	public long getError()
	{
		return error;
	}
	
	public void setError(long error)
	{
		this.error = error;
	}
	
	public double getRightPercentage()
	{
		return groupCount == 0 ? 0 : (double) right / (double) groupCount;
	}
	
	public double getWrongPercentage()
	{
		return groupCount == 0 ? 0 : (double) wrong / (double) groupCount;
	}
	
	public double getTimeoutPercentage()
	{
		return groupCount == 0 ? 0 : (double) timeout / (double) groupCount;
	}
	
	public double getErrorPercentage()
	{
		return groupCount == 0 ? 0 : (double) error / (double) groupCount;
	}
	
	public long getMinAttempts()
	{
		return minAttempts;
	}
	
	public void setMinAttempts(long minAttempts)
	{
		this.minAttempts = minAttempts;
	}
	
	public double getAvgAttempts()
	{
		return avgAttempts;
	}
	
	public void setAvgAttempts(double avgAttempts)
	{
		this.avgAttempts = avgAttempts;
	}
	
	public long getMaxAttempts()
	{
		return maxAttempts;
	}
	
	public void setMaxAttempts(long maxAttempts)
	{
		this.maxAttempts = maxAttempts;
	}
	
	public double getMinNumericGrade()
	{
		return minNumericGrade;
	}
	
	public void setMinNumericGrade(double minNumericGrade)
	{
		this.minNumericGrade = minNumericGrade;
	}
	
	public double getAvgNumericGrade()
	{
		return avgNumericGrade;
	}
	
	public void setAvgNumericGrade(double avgNumericGrade)
	{
		this.avgNumericGrade = avgNumericGrade;
	}
	
	public double getMaxNumericGrade()
	{
		return maxNumericGrade;
	}
	
	public void setMaxNumericGrade(double maxNumericGrade)
	{
		this.maxNumericGrade = maxNumericGrade;
	}
	
	public long getAlphabeticGradeA()
	{
		return alphabeticGradeA;
	}
	
	public void setAlphabeticGradeA(long alphabeticGradeA)
	{
		this.alphabeticGradeA = alphabeticGradeA;
	}
	
	public long getAlphabeticGradeB()
	{
		return alphabeticGradeB;
	}
	
	public void setAlphabeticGradeB(long alphabeticGradeB)
	{
		this.alphabeticGradeB = alphabeticGradeB;
	}
	
	public long getAlphabeticGradeCPlus()
	{
		return alphabeticGradeCPlus;
	}
	
	public void setAlphabeticGradeCPlus(long alphabeticGradeCPlus)
	{
		this.alphabeticGradeCPlus = alphabeticGradeCPlus;
	}
	
	public long getAlphabeticGradeCMinus()
	{
		return alphabeticGradeCMinus;
	}
	
	public void setAlphabeticGradeCMinus(long alphabeticGradeCMinus)
	{
		this.alphabeticGradeCMinus = alphabeticGradeCMinus;
	}
	
	public long getAlphabeticGradeD()
	{
		return alphabeticGradeD;
	}
	
	public void setAlphabeticGradeD(long alphabeticGradeD)
	{
		this.alphabeticGradeD = alphabeticGradeD;
	}
	
	public double getMinTimeBetweenAttempts()
	{
		return minTimeBetweenAttempts;
	}
	
	public void setMinTimeBetweenAttempts(double minTimeBetweenAttempts)
	{
		this.minTimeBetweenAttempts = minTimeBetweenAttempts;
	}
	
	public double getAvgTimeBetweenAttempts()
	{
		return avgTimeBetweenAttempts;
	}
	
	public void setAvgTimeBetweenAttempts(double avgTimeBetweenAttempts)
	{
		this.avgTimeBetweenAttempts = avgTimeBetweenAttempts;
	}
	
	public double getMaxTimeBetweenAttempts()
	{
		return maxTimeBetweenAttempts;
	}
	
	public void setMaxTimeBetweenAttempts(double maxTimeBetweenAttempts)
	{
		this.maxTimeBetweenAttempts = maxTimeBetweenAttempts;
	}
	
	public double getMinDuration()
	{
		return minDuration;
	}
	
	public void setMinDuration(double minDuration)
	{
		this.minDuration = minDuration;
	}
	
	public double getAvgDuration()
	{
		return avgDuration;
	}
	
	public void setAvgDuration(double avgDuration)
	{
		this.avgDuration = avgDuration;
	}
	
	public double getMaxDuration()
	{
		return maxDuration;
	}
	
	public void setMaxDuration(double maxDuration)
	{
		this.maxDuration = maxDuration;
	}
	
	public long getGroupCount()
	{
		return groupCount;
	}
	
	public void setGroupCount(long groupCount)
	{
		this.groupCount = groupCount;
	}
	
	public double getSuccessRate()
	{
		return successRate;
	}
	
	public void setSuccessRate(double successRate)
	{
		this.successRate = successRate;
	}
}
