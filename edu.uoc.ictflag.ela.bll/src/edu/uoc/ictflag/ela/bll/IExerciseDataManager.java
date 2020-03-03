package edu.uoc.ictflag.ela.bll;

import java.util.List;

import edu.uoc.ictflag.ela.model.ExerciseDataRaw;

public interface IExerciseDataManager
{
	List<String> processExerciseDataRaw(ExerciseDataRaw exerciseDataRaw) throws Exception;
}
