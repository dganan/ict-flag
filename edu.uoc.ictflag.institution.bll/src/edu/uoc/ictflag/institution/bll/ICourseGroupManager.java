package edu.uoc.ictflag.institution.bll;

import java.util.List;

import edu.uoc.ictflag.core.dal.PartialSearchResultWithTotalCount;
import edu.uoc.ictflag.core.dal.SearchParameters;
import edu.uoc.ictflag.institution.model.CourseGroup;
import edu.uoc.ictflag.security.model.User;

public interface ICourseGroupManager
{
	List<CourseGroup> getCourseGroupsList(String username) throws Exception;
	
	void deleteCourseGroup(String username, long id) throws Exception;
	
	CourseGroup getCourseGroup(String username, long id) throws Exception;
	
	void createOrUpdateCourseGroup(String username, CourseGroup courseGroup) throws Exception;
	
	List<User> getInstructors(String username) throws Exception;
	
	PartialSearchResultWithTotalCount<User> getAvailableMembersList(String username, SearchParameters searchParameters) throws Exception;
}
