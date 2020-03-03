package edu.uoc.ictflag.ela.model;

public enum ExerciseAction
{
	EXERCISE_START, EXERCISE_SUBMISSION, EXERCISE_ASSESSMENT;
	
	@Override
	public String toString()
	{
		switch (this)
		{
			case EXERCISE_START:
				return "ES";
			case EXERCISE_SUBMISSION:
				return "EU";
			case EXERCISE_ASSESSMENT:
				return "EA";
		}
		
		return null;
	}
	
	public static ExerciseAction fromString(String str)
	{
		if (str == null)
		{
			throw new IllegalArgumentException("ExerciseAction cannot be null");
		}
		
		switch (str)
		{
			case "ES":
				return ExerciseAction.EXERCISE_START;
			case "EU":
				return ExerciseAction.EXERCISE_SUBMISSION;
			case "EA":
				return ExerciseAction.EXERCISE_ASSESSMENT;
			default:
				throw new IllegalArgumentException("Unknown ExerciseAction: " + str);
		}
	}
}
