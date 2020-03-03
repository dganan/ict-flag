package edu.uoc.ictflag.institution.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import edu.uoc.ictflag.core.localization.LocalizedString;
import edu.uoc.ictflag.core.model.ColumnTypes;
import edu.uoc.ictflag.core.model.INameable;
import edu.uoc.ictflag.security.model.User;

@Entity
public class CourseGroup implements Serializable, INameable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	
	@NotNull
	Course course;
	
	@NotNull
	@Column(columnDefinition = ColumnTypes.TEXT_COLUMN)
	String code;
	
	@Valid
	@NotNull
	@Transient
	LocalizedString name;
	
	@Column(columnDefinition = ColumnTypes.TEXT_COLUMN, name = "name")
	private String nameStr;
	
	@Size(min = 1, message = "{javax.validation.constraints.Size.Min1.message}")
	@OneToMany(cascade = { CascadeType.ALL })
	@JoinTable(name = "coursegroup_member", joinColumns = @JoinColumn(name = "coursegroup_id", referencedColumnName = "id") ,
			inverseJoinColumns = @JoinColumn(name = "member_id", referencedColumnName = "id") )
	Set<CourseGroupMember> members;

	User instructor;
	
	public CourseGroup ()
	{
		name = new LocalizedString();
		members = new HashSet<CourseGroupMember>();
	}
	
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
	
	public Course getCourse()
	{
		return course;
	}

	public void setCourse(Course course)
	{
		this.course = course;
	}

	public Set<CourseGroupMember> getMembers()
	{
		return members;
	}

	public void setMembers(Set<CourseGroupMember> members)
	{
		this.members = members;
	}
	
	public User getInstructor()
	{
		return instructor;
	}
	
	public void setInstructor(User instructor)
	{
		this.instructor = instructor;
	}
	
	@Override
	public boolean equals(Object o)
	{
		return o instanceof CourseGroup && (((CourseGroup) o).getId() == this.getId() || ((CourseGroup) o).getId().equals(this.getId()));
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
}
