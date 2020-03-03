package edu.uoc.ictflag.ela.model;

public enum ResultType
{
	OUTCOME, TEXT, GRADE, NONE;
	
	@Override
	public String toString()
	{
		switch (this)
		{
			case OUTCOME:
				return "F";
			case TEXT:
				return "T";
			case GRADE:
				return "G";
			case NONE:
				return "N";
		}
		
		return null;
	}
	
	public static ResultType fromString(String str)
	{
		switch (str)
		{
			case "F":
				return ResultType.OUTCOME;
			case "T":
				return ResultType.TEXT;
			case "G":
				return ResultType.GRADE;
			case "N":
				return ResultType.NONE;
			default:
				throw new IllegalArgumentException("Unknown ResultType: " + str);
		}
	}
}
