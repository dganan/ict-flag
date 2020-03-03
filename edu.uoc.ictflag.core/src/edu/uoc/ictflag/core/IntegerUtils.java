package edu.uoc.ictflag.core;

public class IntegerUtils
{
	public static Integer parseInteger(String str)
	{
		try
		{
			return Integer.parseInt(str);
		}
		catch (Exception e)
		{
			return null;
		}
	}
}
