package edu.uoc.ictflag.institution.dal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import edu.uoc.ictflag.core.IctFlagValidationException;
import edu.uoc.ictflag.institution.model.Course;
import edu.uoc.ictflag.institution.model.InstitutionValidationErrors;
import edu.uoc.ictflag.institution.model.Semester;
import edu.uoc.ictflag.institution.model.Subject;
import edu.uoc.ictflag.security.dal.IUserRepository;
import edu.uoc.ictflag.security.model.User;

@Stateless
@Transactional(TxType.REQUIRED)
public class CourseRepository extends InstitutionBaseRepository implements ICourseRepository
{
	IUserRepository userRepository;
	
	@Inject
	public CourseRepository(IUserRepository userRepository)
	{
		this.userRepository = userRepository;
	}
	
	@Override
	public List<Course> getCoursesList(String username) throws Exception
	{
		List<Course> courses = null;
		
		User u = userRepository.getUserByUsername(username);
		
		if (u != null)
		{
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("username", username);
			
			switch (u.getSelectedRole())
			{
				case SUPERUSER:
				case ADMIN:
					
					courses = dbHelper.getQueryResult("SELECT s FROM Course s", Course.class);
					
					break;
					
				case PROGRAM_MANAGER:
					
					courses = dbHelper.getQueryResult(
							"SELECT DISTINCT c FROM Course c JOIN c.subject s1, Program p JOIN p.subjects s2 JOIN p.programManager u WHERE s1.id = s2.id AND u.username = :username",
							Course.class, parameters);
							
					break;
					
				case LECTURER:
					
					courses = dbHelper.getQueryResult(
							"SELECT DISTINCT c FROM Course c LEFT JOIN c.lecturer l WHERE l.username = :username", Course.class,
							parameters);
							
					break;
					
				case INSTRUCTOR:
					
					courses = dbHelper.getQueryResult(
							"SELECT DISTINCT c FROM CourseGroup cg JOIN cg.instructor i JOIN cg.course c WHERE i.username = :username", Course.class,
							parameters);
							
					break;
					
				case STUDENT:
					
					courses = dbHelper.getQueryResult(
							"SELECT DISTINCT c FROM CourseGroupMember m JOIN m.user u JOIN CourseGroup cg JOIN cg.members ms JOIN cg.course c WHERE m.id = ms.id AND u.username = :username",
							Course.class, parameters);
					
					break;
				
				default:
					
					break;
			}
		}
		
		return courses;
	}
	
	@Override
	public Course getCourse(String username, long id) throws Exception
	{
		Course course = null;
		
		User u = userRepository.getUserByUsername(username);
		
		if (u != null)
		{
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("username", username);
			parameters.put("id", id);
			
			switch (u.getSelectedRole())
			{
				case SUPERUSER:
				case ADMIN:
					
					course = dbHelper.getEntity(id, Course.class);
					
					break;
					
				case PROGRAM_MANAGER:
					
					course = dbHelper.getFirst(
							"SELECT DISTINCT c FROM Course c JOIN c.subject s1, Program p JOIN p.subjects s2 JOIN p.programManager u WHERE s1.id = s2.id AND u.username = :username AND c.id = :id",
							Course.class, parameters);
							
					break;
					
				case LECTURER:
					
					course = dbHelper.getFirst(
							"SELECT DISTINCT c FROM Course c LEFT JOIN c.lecturer l WHERE c.id = :id AND l.username = :username",
							Course.class,
							parameters);
							
					break;
					
				case INSTRUCTOR:
					
					course = dbHelper.getFirst(
							"SELECT DISTINCT c FROM CourseGroup cg JOIN cg.instructor i JOIN cg.course c WHERE i.username = :username AND c.id = :id",
							Course.class,
							parameters);
							
					break;
					
				default:
					
					break;
			}
		}
		
		return course;
	}
	
	@Override
	public Course getCourse(Subject subject, Semester semester) throws Exception
	{
		Course course = null;
		
		Map<String, Object> parameters = new HashMap<String, Object>();
			
		parameters.put("subject_id", subject.getId());
		parameters.put("semester_id", semester.getId());
			
		course = dbHelper.getFirst("SELECT c FROM Course c WHERE c.subject.id = :subject_id AND c.semester.id = :semester_id", Course.class,
				parameters);
							
		return course;
	}
	
	@Override
	public Course getCourseById(long id) throws Exception
	{
		Course course = null;
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		parameters.put("id", id);
		
		course = dbHelper.getFirst("SELECT c FROM Course c WHERE c.id = :id", Course.class, parameters);
		
		return course;
	}
	
	@Override
	public void createOrUpdateCourse(Course p) throws Exception
	{
		dbHelper.createOrUpdateEntity(p);
	}
	
	@Override
	public void deleteCourse(long id) throws Exception
	{
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		parameters.put("id", id);
		
		if ((long) dbHelper.getScalarValue("SELECT COUNT(cg) FROM CourseGroup cg JOIN FETCH cg.course c WHERE c.id = :id", parameters) > 0)
		{
			throw new IctFlagValidationException(InstitutionValidationErrors.CourseCannotBeDeletedBecauseDependentCourseGroupsAlreadyExist);
		}
		
		dbHelper.deleteEntity(id, Course.class);
	}
}
