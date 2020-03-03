package edu.uoc.ictflag.security.model;

public enum Permission
{
	READ, EDIT, DELETE;
	
	@Override
	public String toString()
	{
		switch (this)
		{
			case READ:
				return "R";
			case EDIT:
				return "E";
			case DELETE:
				return "D";
			default:
				throw new IllegalArgumentException("Unknown" + this);
		}
	}
	
	public static Permission fromString(String str)
	{
		switch (str)
		{
			case "R":
				return Permission.READ;
			case "E":
				return Permission.EDIT;
			case "D":
				return Permission.DELETE;
			default:
				throw new IllegalArgumentException("Unknown" + str);
		}
	}
}
