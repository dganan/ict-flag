package edu.uoc.ictflag.ela.dal;

import java.util.List;

import edu.uoc.ictflag.ela.model.Assignment;
import edu.uoc.ictflag.institution.model.Course;

public interface IAssignmentRepository
{
	//	List<Assignment> getAssignmentsList(String username) throws Exception;
	
	//	Assignment getAssignment(String username, long id) throws Exception;
	
	Assignment getAssignment(Course course, String name) throws Exception;
	
	void createOrUpdateAssignment(Assignment assignment) throws Exception;
	
	Assignment getAssignmentById(long id) throws Exception;
	
	List<Assignment> getAssignmentsList(String username) throws Exception;
	
	//	void deleteAssignment(long id) throws Exception;
	
	//	Assignment getStudentAssignment(Course course, User student) throws Exception;
}
