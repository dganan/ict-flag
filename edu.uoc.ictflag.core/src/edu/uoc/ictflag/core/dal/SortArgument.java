package edu.uoc.ictflag.core.dal;

public class SortArgument
{
	String field;
	boolean descending;
	
	public SortArgument(String field, boolean descending)
	{
		this.field = field;
		this.descending = descending;
	}
	
	public String getField()
	{
		return field;
	}
	
	public void setField(String field)
	{
		this.field = field;
	}
	
	public boolean isDescending()
	{
		return descending;
	}
	
	public void setDescending(boolean descending)
	{
		this.descending = descending;
	}
}
