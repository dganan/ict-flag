package edu.uoc.ictflag.core;

public class LongUtils
{
	public static Long parseLong(String str)
	{
		try
		{
			return Long.parseLong(str);
		}
		catch (Exception e)
		{
			return null;
		}
	}
}
