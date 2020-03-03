package edu.uoc.ictflag.ela.model;

public enum AssignmentAction
{
	ASSIGNMENT_START, ASSIGNMENT_END, ASSIGNMENT_ASSESSMENT;
	
	@Override
	public String toString()
	{
		switch (this)
		{
			case ASSIGNMENT_START:
				return "AS";
			case ASSIGNMENT_END:
				return "AE";
			case ASSIGNMENT_ASSESSMENT:
				return "AA";
		}
		
		return null;
	}
	
	public static AssignmentAction fromString(String str)
	{
		if (str == null)
		{
			throw new IllegalArgumentException("AssignmentAction cannot be null");
		}
		
		switch (str)
		{
			case "AS":
				return AssignmentAction.ASSIGNMENT_START;
			case "AE":
				return AssignmentAction.ASSIGNMENT_END;
			case "AA":
				return AssignmentAction.ASSIGNMENT_ASSESSMENT;
			default:
				throw new IllegalArgumentException("Unknown AssignmentAction: " + str);
		}
	}
}
