package edu.uoc.ictflag.core;

import java.util.ArrayList;
import java.util.List;

public class NameValuePair<T>
{
	String name;
	T value;
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public T getValue()
	{
		return value;
	}
	
	public void setValue(T value)
	{
		this.value = value;
	}
	
	public NameValuePair(String name, T value)
	{
		this.name = name;
		this.value = value;
	}
	
	public static <T> List<NameValuePair<T>> getNameValuePairListFromRawList(List<T> list)
	{
		List<NameValuePair<T>> newList = new ArrayList<NameValuePair<T>>();
		
		for (T o : list)
		{
			newList.add(new NameValuePair<T>(o.toString(), o));
		}
		
		return newList;
	}
}
