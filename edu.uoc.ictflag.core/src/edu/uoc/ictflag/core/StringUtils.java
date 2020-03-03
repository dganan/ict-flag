package edu.uoc.ictflag.core;

public class StringUtils
{
	public static Long tryParse(String s, Long defaultValue)
	{
		Long result = defaultValue;
		
		if (s != null)
		{
			try
			{
				result = Long.parseLong(s);
			}
			catch (NumberFormatException ex)
			{
			}
		}
		
		return result;
	}
}
