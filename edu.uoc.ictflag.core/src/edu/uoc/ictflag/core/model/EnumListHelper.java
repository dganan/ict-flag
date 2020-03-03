package edu.uoc.ictflag.core.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Embeddable;

import edu.uoc.ictflag.core.log.LogHelper;

@Embeddable
public class EnumListHelper
{
	public static <T> String toString(List<T> items)
	{
		return items.stream().map(T::toString).collect(Collectors.joining(","));
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> fromString(String dbData, Class<T> classT)
	{
		List<T> ur = new ArrayList<T>();
		
		String[] parts = dbData.split(",");
		
		for (String s : parts)
		{
			try
			{
				ur.add((T) classT.getMethod("fromString", String.class).invoke(null, s));
			}
			catch (Exception e)
			{
				LogHelper.error(e);
			}
		}
		
		return ur;
	}
}