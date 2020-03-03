package edu.uoc.ictflag.security.model;

public enum UserRole
{
	SUPERUSER, ADMIN, PROGRAM_MANAGER, LECTURER, INSTRUCTOR, STUDENT;
	
	@Override
	public String toString()
	{
		switch (this)
		{
			case SUPERUSER:
				return "U";
			case ADMIN:
				return "A";
			case PROGRAM_MANAGER:
				return "M";
			case LECTURER:
				return "L";
			case INSTRUCTOR:
				return "I";
			case STUDENT:
				return "S";
		}
		
		return null;
	}
	
	public static UserRole fromString(String str)
	{
		switch (str)
		{
			case "U":
				return UserRole.SUPERUSER;
			case "A":
				return UserRole.ADMIN;
			case "M":
				return UserRole.PROGRAM_MANAGER;
			case "L":
				return UserRole.LECTURER;
			case "I":
				return UserRole.INSTRUCTOR;
			case "S":
				return UserRole.STUDENT;
			default:
				throw new IllegalArgumentException("Unknown UserRole: " + str);
		}
	}
}
