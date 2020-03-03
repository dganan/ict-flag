package edu.uoc.ictflag.institution.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import edu.uoc.ictflag.core.model.IIdentifiable;
import edu.uoc.ictflag.security.model.User;

@Entity
public class CourseGroupMember implements Serializable, IIdentifiable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	
	@NotNull
	@OneToOne(cascade = { CascadeType.DETACH })
	User user;
	
	public CourseGroupMember()
	{
	}
	
	public CourseGroupMember(User user)
	{
		this.user = user;
	}
	
	public Long getId()
	{
		return id;
	}
	
	public void setId(Long id)
	{
		this.id = id;
	}
	
	public User getUser()
	{
		return user;
	}
	
	public void setUser(User user)
	{
		this.user = user;
	}
	
	@Override
	public boolean equals(Object o)
	{
		return o instanceof CourseGroupMember
				&& (((CourseGroupMember) o).getId() == this.getId() || ((CourseGroupMember) o).getId().equals(this.getId()));
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
	
	@Override
	public String toString()
	{
		return this.user.getName() + " - " + user.getEmail();
	}
}
