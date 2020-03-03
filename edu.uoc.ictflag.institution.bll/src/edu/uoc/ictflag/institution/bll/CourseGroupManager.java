package edu.uoc.ictflag.institution.bll;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import edu.com.ictflag.security.bll.IAuthorizationManager;
import edu.uoc.ictflag.core.IctFlagPermissionDeniedException;
import edu.uoc.ictflag.core.dal.PartialSearchResultWithTotalCount;
import edu.uoc.ictflag.core.dal.SearchParameters;
import edu.uoc.ictflag.institution.dal.ICourseGroupRepository;
import edu.uoc.ictflag.institution.model.CourseGroup;
import edu.uoc.ictflag.security.dal.IUserRepository;
import edu.uoc.ictflag.security.model.Permission;
import edu.uoc.ictflag.security.model.User;
import edu.uoc.ictflag.security.model.UserRole;

@Stateless
public class CourseGroupManager implements ICourseGroupManager, Serializable
{
	private static final long serialVersionUID = 1L;
	
	ICourseGroupRepository courseGroupRepository;
	IAuthorizationManager authorizationManager;
	IUserRepository userRepository;
	
	@Inject
	public CourseGroupManager(ICourseGroupRepository courseGroupRepository, IAuthorizationManager authorizationManager, IUserRepository userRepository)
	{
		this.courseGroupRepository = courseGroupRepository;
		this.authorizationManager = authorizationManager;
		this.userRepository = userRepository;
	}
	
	public List<CourseGroup> getCourseGroupsList(String username) throws Exception
	{
		List<CourseGroup> courseGroupsList = null;
		
		User ui = userRepository.getUserByUsername(username);
		
		if (authorizationManager.userHasPermission(ui, "institution", "CourseGroup", Permission.READ))
		{
			courseGroupsList = courseGroupRepository.getCourseGroupsList(username);
		}
		
		return courseGroupsList;
	}
	
	@Override
	public CourseGroup getCourseGroup(String username, long id) throws Exception
	{
		CourseGroup courseGroup = null;
		
		User ui = userRepository.getUserByUsername(username);
		
		if (authorizationManager.userHasPermission(ui, "institution", "CourseGroup", Permission.READ))
		{
			courseGroup = courseGroupRepository.getCourseGroup(username, id);
		}
		
		if (courseGroup == null)
		{
			throw new IctFlagPermissionDeniedException(username);
		}
		
		return courseGroup;
	}
	
	@Override
	public void createOrUpdateCourseGroup(String username, CourseGroup courseGroup) throws Exception
	{
		User ui = userRepository.getUserByUsername(username);
		
		if (authorizationManager.userHasPermission(ui, "institution", "CourseGroup", Permission.EDIT))
		{
			courseGroupRepository.createOrUpdateCourseGroup(courseGroup);
		}
	}
	
	@Override
	public void deleteCourseGroup(String username, long id) throws Exception
	{
		User ui = userRepository.getUserByUsername(username);
		
		if (authorizationManager.userHasPermission(ui, "institution", "CourseGroup", Permission.DELETE))
		{
			courseGroupRepository.deleteCourseGroup(id);
		}
	}
	
	@Override
	public List<User> getInstructors(String username) throws Exception
	{
		User ui = userRepository.getUserByUsername(username);
		
		if (authorizationManager.userHasPermission(ui, "institution", "CourseGroup", Permission.EDIT))
		{
			return userRepository.getUsersByRole(UserRole.INSTRUCTOR);
		}
		
		return null;
	}
	
	@Override
	public PartialSearchResultWithTotalCount<User> getAvailableMembersList(String username, SearchParameters searchParameters) throws Exception
	{
		User ui = userRepository.getUserByUsername(username);
		
		if (authorizationManager.userHasPermission(ui, "institution", "CourseGroup", Permission.EDIT))
		{
			return userRepository.getUsersByRole(UserRole.STUDENT, searchParameters);
		}
		
		return null;
	}
}
