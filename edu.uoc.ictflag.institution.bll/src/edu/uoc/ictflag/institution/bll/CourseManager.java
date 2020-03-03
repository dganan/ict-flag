package edu.uoc.ictflag.institution.bll;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import edu.com.ictflag.security.bll.IAuthorizationManager;
import edu.uoc.ictflag.core.IctFlagPermissionDeniedException;
import edu.uoc.ictflag.institution.dal.ICourseRepository;
import edu.uoc.ictflag.institution.dal.ISemesterRepository;
import edu.uoc.ictflag.institution.model.Course;
import edu.uoc.ictflag.institution.model.Semester;
import edu.uoc.ictflag.security.dal.IUserRepository;
import edu.uoc.ictflag.security.model.Permission;
import edu.uoc.ictflag.security.model.User;
import edu.uoc.ictflag.security.model.UserRole;

@Stateless
public class CourseManager implements ICourseManager, Serializable
{
	private static final long serialVersionUID = 1L;
	
	ICourseRepository courseRepository;
	ISemesterRepository semesterRepository;
	IAuthorizationManager authorizationManager;
	IUserRepository userRepository;
	
	@Inject
	public CourseManager(ICourseRepository courseRepository, IAuthorizationManager authorizationManager, IUserRepository userRepository, ISemesterRepository semesterRepository)
	{
		this.courseRepository = courseRepository;
		this.semesterRepository = semesterRepository;
		this.authorizationManager = authorizationManager;
		this.userRepository = userRepository;
	}
	
	public List<Course> getCoursesList(String username) throws Exception
	{
		List<Course> coursesList = null;
		
		User ui = userRepository.getUserByUsername(username);
		
		if (authorizationManager.userHasPermission(ui, "institution", "Course", Permission.READ))
		{
			coursesList = courseRepository.getCoursesList(username);
		}
		
		return coursesList;
	}
	
	public List<Semester> getSemesters(String username) throws Exception
	{
		List<Semester> semesters = null;
		
		User ui = userRepository.getUserByUsername(username);
		
		if (authorizationManager.userHasPermission(ui, "institution", "Semester", Permission.READ))
		{
			semesters = semesterRepository.getSemesters(username);
		}
		
		return semesters;
	}
	
	@Override
	public Course getCourse(String username, long id) throws Exception
	{
		Course course = null;
		
		User ui = userRepository.getUserByUsername(username);
		
		if (authorizationManager.userHasPermission(ui, "institution", "Course", Permission.READ))
		{
			course = courseRepository.getCourse(username, id);
		}
		
		if (course == null)
		{
			throw new IctFlagPermissionDeniedException(username);
		}
		
		return course;
	}
	
	@Override
	public void createOrUpdateCourse(String username, Course course) throws Exception
	{
		User ui = userRepository.getUserByUsername(username);
		
		if (authorizationManager.userHasPermission(ui, "institution", "Course", Permission.EDIT))
		{
			if (ui.getSelectedRole() == UserRole.LECTURER)
			{
				if (course.getLecturer().getUsername().equals(username))
				{
					courseRepository.createOrUpdateCourse(course);
				}
			}
			else
			{
				courseRepository.createOrUpdateCourse(course);
			}
		}
	}
	
	@Override
	public void deleteCourse(String username, long id) throws Exception
	{
		User ui = userRepository.getUserByUsername(username);
		
		if (authorizationManager.userHasPermission(ui, "institution", "Course", Permission.DELETE))
		{
			if (ui.getSelectedRole() == UserRole.LECTURER)
			{
				Course p = courseRepository.getCourse(username, id);
				
				if (p.getLecturer().getUsername().equals(username))
				{
					courseRepository.deleteCourse(id);
				}
			}
			else
			{
				courseRepository.deleteCourse(id);
			}
		}
	}
	
	@Override
	public List<User> getLecturers(String username) throws Exception
	{
		User ui = userRepository.getUserByUsername(username);
		
		if (authorizationManager.userHasPermission(ui, "institution", "Course", Permission.EDIT))
		{
			if (ui.getSelectedRole() == UserRole.LECTURER)
			{
				List<User> ul = new ArrayList<User>();
				
				ul.add(userRepository.getUserByUsername(username));
				
				return ul;
			}
			else
			{
				return userRepository.getUsersByRole(UserRole.LECTURER);
			}
		}
		
		return null;
	}
}
