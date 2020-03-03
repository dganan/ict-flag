package edu.uoc.ictflag.security.model;

import javax.persistence.Embeddable;

@Embeddable
public class SecurePassword
{
	String password;
	String salt;
	
	public String getPassword()
	{
		return password;
	}
	
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	public String getSalt()
	{
		return salt;
	}
	
	public void setSalt(String salt)
	{
		this.salt = salt;
	}
}
