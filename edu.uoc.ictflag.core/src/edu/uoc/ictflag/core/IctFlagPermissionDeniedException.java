package edu.uoc.ictflag.core;

public class IctFlagPermissionDeniedException extends Exception
{
	private static final long serialVersionUID = 1L;

	private String username;
	
	public IctFlagPermissionDeniedException(String username)
	{
		this(username, "");
	}
	
	public IctFlagPermissionDeniedException(String username, String message)
	{
		super(message);
		
		this.username = username;
	}
	
	@Override
	public String getMessage()
	{
		return "ACCESS DENIAL user: " + username + ". " + super.getMessage();
	}
}
