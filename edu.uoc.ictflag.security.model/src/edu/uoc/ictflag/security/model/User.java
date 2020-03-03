package edu.uoc.ictflag.security.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import edu.uoc.ictflag.core.model.EnumListHelper;
import edu.uoc.ictflag.core.model.IIdentifiable;

/**
 * Entity class User
 */
@Entity
@Table(name="Users")
public class User implements Serializable, IIdentifiable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(unique=true)
	private String username;
	
	@NotNull
	private String name;
	
	private String email;
	
	private String idp;
	
	@Transient
	private List<UserRole> roles;
	
	private String rolesStr;
	
	private String selectedLanguage;
	
	private UserRole selectedRole;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastVisit;
	
	private boolean external;
	
	public Long getId()
	{
		return this.id;
	}
	
	public void setId(Long id)
	{
		this.id = id;
	}
	
	public String getUsername()
	{
		return this.username;
	}
	
	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getName()
	{
		return this.name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getEmail()
	{
		return this.email;
	}
	
	public void setEmail(String email)
	{
		this.email = email;
	}
	
	public String getIdp()
	{
		return idp;
	}
	
	public void setIdp(String idp)
	{
		this.idp = idp;
	}
	
	public List<UserRole> getRoles()
	{
		return this.roles;
	}
	
	public void setRoles(List<UserRole> roles)
	{
		this.roles = roles;
		
		this.selectedRole = roles.get(0);
	}
	
	public User()
	{
	}
	
	public User(Long id, String username, String name, List<UserRole> roles)
	{
		this.id = id;
		this.username = username;
		this.name = name;
		this.roles = roles;
		
		this.selectedRole = roles.get(0);
	}
	
	@Override
	public String toString()
	{
		return name;
	}
	
	@Override
	public boolean equals(Object o)
	{
		return o instanceof User && (((User) o).getId() == this.getId() || ((User) o).getId().equals(this.getId()));
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
	
	public Date getLastVisit()
	{
		return lastVisit;
	}
	
	public void setLastVisit(Date lastVisit)
	{
		this.lastVisit = lastVisit;
	}
	
	public UserRole getSelectedRole()
	{
		return selectedRole;
	}
	
	public void setSelectedRole(UserRole selectedRole)
	{
		this.selectedRole = selectedRole;
	}
	
	public String getSelectedLanguage()
	{
		return selectedLanguage;
	}
	
	public void setSelectedLanguage(String selectedLanguage)
	{
		this.selectedLanguage = selectedLanguage;
	}
	
	public boolean isExternal()
	{
		return external;
	}
	
	public void setExternal(boolean external)
	{
		this.external = external;
	}
	
	@PrePersist
	@PreUpdate
	public void preUpdate()
	{
		if (roles != null && roles.size() > 0)
		{
			this.rolesStr = EnumListHelper.toString(roles);
		}
	}
	
	@PostLoad
	public void postLoad()
	{
		this.roles = EnumListHelper.fromString(this.rolesStr, UserRole.class);
	}
}