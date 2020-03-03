package edu.uoc.ictflag.security.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import edu.uoc.ictflag.core.model.IIdentifiable;

@Entity
public class UserRolePageAccess implements Serializable, IIdentifiable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	private Page page;
	
	private UserRole role;
	
	@Override
	public Long getId()
	{
		return id;
	}
	
	@Override
	public void setId(Long id)
	{
		this.id = id;
	}
	
	public Page getPage()
	{
		return page;
	}
	
	public void setPage(Page page)
	{
		this.page = page;
	}
	
	public UserRole getRole()
	{
		return role;
	}
	
	public void setRole(UserRole role)
	{
		this.role = role;
	}
}
