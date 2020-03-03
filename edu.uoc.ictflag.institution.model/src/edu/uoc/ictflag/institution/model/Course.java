package edu.uoc.ictflag.institution.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import edu.uoc.ictflag.core.localization.LocalizedString;
import edu.uoc.ictflag.core.model.ColumnTypes;
import edu.uoc.ictflag.core.model.IIdentifiable;
import edu.uoc.ictflag.core.model.IMappeable;
import edu.uoc.ictflag.security.model.User;

@Entity
public class Course implements Serializable, IIdentifiable, IMappeable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	
	@NotNull
	@Column(columnDefinition = ColumnTypes.TEXT_COLUMN)
	String code;
	
	@Valid
	@NotNull
	@Transient
	LocalizedString name;
	
	@Column(columnDefinition = ColumnTypes.TEXT_COLUMN, name = "name")
	private String nameStr;
	
	@NotNull
	Semester semester;
	
	@NotNull
	@Column(columnDefinition = ColumnTypes.TEXT_COLUMN)
	String language;
	
	@NotNull
	Subject subject;
	
	User lecturer;
	
	public Long getId()
	{
		return id;
	}
	
	public void setId(Long id)
	{
		this.id = id;
	}
	
	public String getCode()
	{
		return code;
	}
	
	public void setCode(String code)
	{
		this.code = code;
	}
	
	public LocalizedString getName()
	{
		return name;
	}
	
	public void setName(LocalizedString name)
	{
		this.name = name;
	}
	
	public Semester getSemester()
	{
		return semester;
	}
	
	public void setSemester(Semester semester)
	{
		this.semester = semester;
	}
	
	public String getLanguage()
	{
		return language;
	}
	
	public void setLanguage(String language)
	{
		this.language = language;
	}
	
	public Subject getSubject()
	{
		return subject;
	}
	
	public void setSubject(Subject subject)
	{
		this.subject = subject;
	}
	
	public User getLecturer()
	{
		return lecturer;
	}
	
	public void setLecturer(User lecturer)
	{
		this.lecturer = lecturer;
	}
	
	public Course()
	{
		name = new LocalizedString();
	}
	
	@Override
	public boolean equals(Object o)
	{
		return o instanceof Course && (((Course) o).getId() == this.getId() || ((Course) o).getId().equals(this.getId()));
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
	
	@PrePersist
	@PreUpdate
	public void preUpdate()
	{
		this.nameStr = name.toStringFormat();
	}
	
	@PostLoad
	public void postLoad()
	{
		this.name = LocalizedString.fromStringFormat(nameStr);
	}
	
	@Override
	public void mapTo(Object t)
	{
		Course c = (Course) t;
		
		c.code = this.code;
		c.name = this.name;
		c.semester = this.semester;
		c.language = this.language;
		c.subject = this.subject;
		c.lecturer = this.lecturer;
	}
	
	@Override
	public String toString()
	{
		return code;
	}
}
