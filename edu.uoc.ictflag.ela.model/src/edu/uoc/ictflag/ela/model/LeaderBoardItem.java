package edu.uoc.ictflag.ela.model;

public class LeaderBoardItem
{
	private String level;
	private String students;
	private String value;
	
	public String getLevel()
	{
		return level;
	}
	
	public void setLevel(String level)
	{
		this.level = level;
	}
	
	public String getStudents()
	{
		return students;
	}
	
	public void setStudents(String students)
	{
		this.students = students;
	}
	
	public String getValue()
	{
		return value;
	}
	
	public void setValue(String value)
	{
		this.value = value;
	}
	
	public LeaderBoardItem(String level, String students, String value)
	{
		this.level = level;
		this.students = students;
		this.value = value;
	}
}

