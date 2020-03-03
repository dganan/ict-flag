package edu.uoc.ictflag.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.uoc.ictflag.core.model.IIdentifiable;

public class ListUtils
{
	public static <T extends IIdentifiable> T findById(List<T> list, Long id)
	{
		for (T t : list)
		{
			if (t.getId().equals(id))
			{
				return t;
			}
		}
		
		return null;
	}
	
	public static <T extends IIdentifiable, S extends IIdentifiable> List<String> flattenIdList(Map<T, List<S>> list)
	{
		List<String> flattenedList = new ArrayList<String>();
		
		for (T t : list.keySet())
		{
			List<S> sList = list.get(t);
			
			for (S s : sList)
			{
				String tid = t == null ? null : t.getId() + "";
				String sid = s == null ? null : s.getId() + "";
				
				flattenedList.add(tid + ":" + sid);
			}
		}
		
		return flattenedList;
	}
	
	public static List<String> flattenStringList(Map<String, List<String>> list)
	{
		List<String> flattenedList = new ArrayList<String>();
		
		for (String c : list.keySet())
		{
			List<String> as = list.get(c);
			
			for (String a : as)
			{
				flattenedList.add(c + ":" + a);
			}
		}
		
		return flattenedList;
	}
}
