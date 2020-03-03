package edu.uoc.ictflag.ela.model;

public enum Outcome
{
	RIGHT, WRONG, TIMEOUT, ERROR;
	
	@Override
	public String toString()
	{
		switch (this)
		{
			case RIGHT:
				return "R";
			case WRONG:
				return "W";
			case TIMEOUT:
				return "T";
			case ERROR:
				return "E";
		}
		
		return null;
	}
	
	public static Outcome fromString(String str)
	{
		switch (str)
		{
			case "R":
				return Outcome.RIGHT;
			case "W":
				return Outcome.WRONG;
			case "T":
				return Outcome.TIMEOUT;
			case "E":
				return Outcome.ERROR;
			default:
				throw new IllegalArgumentException("Unknown Action: " + str);
		}
	}
}
