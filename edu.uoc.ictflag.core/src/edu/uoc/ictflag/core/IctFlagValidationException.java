package edu.uoc.ictflag.core;

public class IctFlagValidationException extends Exception
{
	private static final long serialVersionUID = 1L;

	private String errorKey;
	
	public IctFlagValidationException(String errorKey)
	{
		this.errorKey = errorKey;
	}
	
	public String getErrorKey()
	{
		return errorKey;
	}
}
