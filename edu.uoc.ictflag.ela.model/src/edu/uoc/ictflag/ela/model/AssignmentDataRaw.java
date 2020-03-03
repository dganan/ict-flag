package edu.uoc.ictflag.ela.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AssignmentDataRaw
{
	protected Long id;
	
	private String tool;
	private String toolUUID;
	private String userId;
	
	private String timestamp;
	private String programCode;
	private String subjectCode;
	private String semester;
	private String action;
	private String assignment;
	private String grade;
	
	public AssignmentDataRaw()
	{
	
	}
	
	public AssignmentDataRaw(Date timestamp, String tool, String toolUUID, String userId, String programCode, String subjectCode, String semester, String action, String assignment, String grade)
	{
		if (timestamp == null)
		{
			timestamp = new Date();
		}
		
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.timestamp = formatter.format(timestamp);
		
		this.tool = tool;
		this.toolUUID = toolUUID;
		this.userId = userId;
		
		this.programCode = programCode;
		this.subjectCode = subjectCode;
		this.semester = semester;
		this.action = action;
		this.assignment = assignment;
		this.grade = grade;
	}
	
	public AssignmentDataRaw(Object[] raw)
	{
		int i = 0;
		
		id = Long.parseLong(raw[i].toString());
		i++;
		tool = (String) raw[i];
		i++;
		toolUUID = (String) raw[i];
		i++;
		userId = (String) raw[i];
		i++;
		timestamp = (String) raw[i];
		i++;
		programCode = (String) raw[i];
		i++;
		subjectCode = (String) raw[i];
		i++;
		semester = (String) raw[i];
		i++;
		action = (String) raw[i];
		i++;
		assignment = (String) raw[i];
		i++;
		grade = (String) raw[i];
	}
	
	public Long getId()
	{
		return id;
	}
	
	public void setId(Long id)
	{
		this.id = id;
	}
	
	public String getTimestamp()
	{
		return timestamp;
	}
	
	public void setTimestamp(String timestamp)
	{
		this.timestamp = timestamp;
	}
	
	public String getTool()
	{
		return tool;
	}
	
	public void setTool(String tool)
	{
		this.tool = tool;
	}
	
	public String getToolUUID()
	{
		return toolUUID;
	}
	
	public void setToolUUID(String toolUUID)
	{
		this.toolUUID = toolUUID;
	}
	
	public String getUserId()
	{
		return userId;
	}
	
	public void setUserId(String userId)
	{
		this.userId = userId;
	}
	
	public String getSubjectCode()
	{
		return subjectCode;
	}
	
	public void setSubjectCode(String subjectCode)
	{
		this.subjectCode = subjectCode;
	}
	
	public String getProgramCode()
	{
		return programCode;
	}
	
	public void setProgramCode(String programCode)
	{
		this.programCode = programCode;
	}
	
	public String getSemester()
	{
		return semester;
	}
	
	public void setSemester(String semester)
	{
		this.semester = semester;
	}
	
	public String getAction()
	{
		return action;
	}
	
	public void setAction(String action)
	{
		this.action = action;
	}
	
	public String getAssignment()
	{
		return assignment;
	}
	
	public void setAssignment(String assignment)
	{
		this.assignment = assignment;
	}
	
	public String getGrade()
	{
		return grade;
	}
	
	public void setGrade(String grade)
	{
		this.grade = grade;
	}
	
	@Override
	public String toString()
	{
		return this.timestamp + ";" + this.tool + ";" + this.toolUUID + ";" + this.userId + ";" + this.programCode + ";" + this.subjectCode + ";"
				+ this.semester + ";" + this.action + ";" + this.assignment + ";" + this.grade;
	}
}
