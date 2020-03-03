package edu.uoc.ictflag.ela.dal;

import edu.uoc.ictflag.ela.model.ExerciseDataRaw;

public interface IExerciseDataRepository
{
	void saveExerciseDataRaw(ExerciseDataRaw exerciseDataRaw) throws Exception;
	
	void processExerciseDataRaw(ExerciseDataRaw exerciseDataRaw) throws Exception;
}
