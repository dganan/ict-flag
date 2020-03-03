package edu.uoc.ictflag.ela.dal;

import java.util.List;

import edu.uoc.ictflag.ela.model.Exercise;
import edu.uoc.ictflag.ela.model.Tool;

public interface IExerciseRepository
{
	//	List<Exercise> getExercisesList(String username) throws Exception;
	
	//	Exercise getExercise(String username, long id) throws Exception;
	
	Exercise getExercise(Tool tool, String name) throws Exception;
	
	Exercise getExerciseById(long id) throws Exception;
	
	void createOrUpdateExercise(Exercise exercise) throws Exception;
	
	List<Exercise> getExercisesList(String username) throws Exception;
	
	//	void deleteExercise(long id) throws Exception;
	
	//	Exercise getStudentExercise(Course course, User student) throws Exception;
}
