package edu.uoc.ictflag.security.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;

import edu.uoc.ictflag.core.model.EnumListHelper;
import edu.uoc.ictflag.core.model.IIdentifiable;

@Entity
public class UserRolePermissions implements Serializable, IIdentifiable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String module;
	
	private String entity;
	
	private UserRole role;
	
	@Transient
	private List<Permission> permissions;
	
	@Column(name = "permissions")
	private String permissionsStr;
	
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
	
	public String getModule()
	{
		return module;
	}
	
	public void setModule(String module)
	{
		this.module = module;
	}
	
	public String getEntity()
	{
		return entity;
	}
	
	public void setEntity(String entity)
	{
		this.entity = entity;
	}
	
	public UserRole getRole()
	{
		return role;
	}
	
	public void setRole(UserRole role)
	{
		this.role = role;
	}
	
	public List<Permission> getPermissions()
	{
		return permissions;
	}
	
	public void setPermissions(List<Permission> permissions)
	{
		this.permissions = permissions;
	}
	
	@PrePersist
	@PreUpdate
	public void preUpdate()
	{
		if (permissions != null && permissions.size() > 0)
		{
			this.permissionsStr = EnumListHelper.toString(permissions);
		}
	}
	
	@PostLoad
	public void postLoad()
	{
		this.permissions = EnumListHelper.fromString(this.permissionsStr, Permission.class);
	}
}
