package edu.uoc.ictflag.institution.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import edu.uoc.ictflag.core.localization.LocalizedString;
import edu.uoc.ictflag.core.model.INameable;

@Entity
public class Semester implements Serializable, INameable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	
	private int academicYear;
	
	private int number;
	
	@Temporal(TemporalType.DATE)
	private Date startDate;
	@Temporal(TemporalType.DATE)
	private Date endDate;
	
	public Long getId()
	{
		return id;
	}
	
	public void setId(Long id)
	{
		this.id = id;
	}
	
	public LocalizedString getName()
	{
		return LocalizedString.fromStringFormat("en#" + getCode());
	}
	
	public String getCode()
	{
		return academicYear + "-" + number;
	}
	
	public Date getStartDate()
	{
		return startDate;
	}
	
	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}
	
	public Date getEndDate()
	{
		return endDate;
	}
	
	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}
	
	public int getAcademicYear()
	{
		return academicYear;
	}
	
	public void setAcademicYear(int academicYear)
	{
		this.academicYear = academicYear;
	}
	
	public int getNumber()
	{
		return number;
	}
	
	public void setNumber(int number)
	{
		this.number = number;
	}
	
	@Override
	public boolean equals(Object o)
	{
		return o instanceof Semester && (((Semester) o).getId() == this.getId() || ((Semester) o).getId().equals(this.getId()));
	}
	
	@Override
	public int hashCode()
	{
		int hash = 17;
		
		if (id != null)
		{
			hash = hash * 31 + id.hashCode();
		}
		
		return hash;
	}
}
