package edu.uoc.ictflag.security.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import edu.uoc.ictflag.core.model.IIdentifiable;

@Entity
public class UserSecurePassword implements Serializable, IIdentifiable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true)
	Long userId;
	
	@Embedded
	SecurePassword password;
	
	public Long getId()
	{
		return this.id;
	}
	
	public void setId(Long id)
	{
		this.id = id;
	}
	
	public SecurePassword getPassword()
	{
		return password;
	}
	
	public void setPassword(SecurePassword password)
	{
		this.password = password;
	}
	
	public Long getUserId()
	{
		return userId;
	}
	
	public void setUserId(Long userId)
	{
		this.userId = userId;
	}
}
