package edu.uoc.ictflag.security.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import edu.uoc.ictflag.core.model.ColumnTypes;
import edu.uoc.ictflag.core.model.IIdentifiable;

@Entity
public class UserActivity implements Serializable, IIdentifiable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String userName;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date activityTime;
	
	@Column(columnDefinition = ColumnTypes.TEXT_COLUMN)
	private String activity;
	
	@Column(columnDefinition = ColumnTypes.TEXT_COLUMN)
	private String otherInfo;
	
	public Long getId()
	{
		return this.id;
	}
	
	public void setId(Long id)
	{
		this.id = id;
	}

	public String getUserName()
	{
		return userName;
	}
	
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	
	public Date getActivityTime()
	{
		return activityTime;
	}
	
	public void setActivityTime(Date activityTime)
	{
		this.activityTime = activityTime;
	}
	
	public String getActivity()
	{
		return activity;
	}
	
	public void setActivity(String activity)
	{
		this.activity = activity;
	}
	
	public String getOtherInfo()
	{
		return otherInfo;
	}
	
	public void setOtherInfo(String otherInfo)
	{
		this.otherInfo = otherInfo;
	}
	
	public UserActivity()
	{
	}
	
	public UserActivity(String userName, Date activityTime, String activity, String otherInfo)
	{
		this.userName = userName;
		this.activityTime = activityTime;
		this.activity = activity;
		this.otherInfo = otherInfo;
	}
}
