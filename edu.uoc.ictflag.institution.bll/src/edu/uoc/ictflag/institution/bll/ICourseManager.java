package edu.uoc.ictflag.institution.bll;

import java.util.List;

import edu.uoc.ictflag.institution.model.Course;
import edu.uoc.ictflag.institution.model.Semester;
import edu.uoc.ictflag.security.model.User;

public interface ICourseManager
{
	List<Course> getCoursesList(String username) throws Exception;
	
	void deleteCourse(String username, long id) throws Exception;
	
	Course getCourse(String username, long id) throws Exception;
	
	void createOrUpdateCourse(String username, Course course) throws Exception;
	
	List<User> getLecturers(String username) throws Exception;
	
	List<Semester> getSemesters(String username) throws Exception;
}
