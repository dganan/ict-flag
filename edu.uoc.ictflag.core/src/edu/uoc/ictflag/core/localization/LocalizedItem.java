package edu.uoc.ictflag.core.localization;

public class LocalizedItem
{
	private String value;
	private String displayName;
	
	public LocalizedItem(String value, String displayName)
	{
		this.value = value;
		this.displayName = displayName;
	}
	
	public String getValue()
	{
		return value;
	}
	
	public void setValue(String value)
	{
		this.value = value;
	}
	
	public String getDisplayName()
	{
		return displayName;
	}
	
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}

	@Override
	public String toString()
	{
		return value;
	}
}
