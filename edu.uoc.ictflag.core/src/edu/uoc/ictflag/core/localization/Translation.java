package edu.uoc.ictflag.core.localization;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class Translation implements Comparable<Translation>, Serializable
{
	private static final long serialVersionUID = 1L;
	
	@NotNull
	private String code;
	
	@NotNull
	private String translation;
	
	public Translation()
	{
	}
	
	public Translation(String code, String translation)
	{
		this.code = code;
		this.translation = translation;
	}
	
	public String getCode()
	{
		return code;
	}
	
	public void setCode(String code)
	{
		this.code = code;
	}
	
	public String getTranslation()
	{
		return translation;
	}
	
	public void setTranslation(String translation)
	{
		this.translation = translation;
	}
	
	@Override
	public int compareTo(Translation t)
	{
		return this.code.compareTo(t.code);
	}
}
