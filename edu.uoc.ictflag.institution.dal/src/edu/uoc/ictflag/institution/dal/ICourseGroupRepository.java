package edu.uoc.ictflag.institution.dal;

import java.util.List;

import edu.uoc.ictflag.institution.model.Course;
import edu.uoc.ictflag.institution.model.CourseGroup;
import edu.uoc.ictflag.security.model.User;

public interface ICourseGroupRepository
{
	List<CourseGroup> getCourseGroupsList(String username) throws Exception;
	
	CourseGroup getCourseGroup(String username, long id) throws Exception;
	
	void createOrUpdateCourseGroup(CourseGroup courseGroup) throws Exception;
	
	void deleteCourseGroup(long id) throws Exception;
	
	CourseGroup getStudentCourseGroup(Course course, User student) throws Exception;
}
