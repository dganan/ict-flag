package edu.uoc.ictflag.institution.dal;

import java.util.List;

import edu.uoc.ictflag.institution.model.Course;
import edu.uoc.ictflag.institution.model.Semester;
import edu.uoc.ictflag.institution.model.Subject;

public interface ICourseRepository
{
	List<Course> getCoursesList(String username) throws Exception;
	
	Course getCourse(String username, long id) throws Exception;
	
	Course getCourse(Subject subject, Semester semester) throws Exception;
	
	void createOrUpdateCourse(Course Course) throws Exception;
	
	void deleteCourse(long id) throws Exception;
	
	Course getCourseById(long id) throws Exception;
}
