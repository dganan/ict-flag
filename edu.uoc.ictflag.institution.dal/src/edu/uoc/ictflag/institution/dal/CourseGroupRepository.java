package edu.uoc.ictflag.institution.dal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import edu.uoc.ictflag.core.log.LogHelper;
import edu.uoc.ictflag.institution.model.Course;
import edu.uoc.ictflag.institution.model.CourseGroup;
import edu.uoc.ictflag.institution.model.CourseGroupMember;
import edu.uoc.ictflag.security.dal.IUserRepository;
import edu.uoc.ictflag.security.model.User;

@Stateless
@Transactional(TxType.REQUIRED)
public class CourseGroupRepository extends InstitutionBaseRepository implements ICourseGroupRepository
{
	IUserRepository userRepository;
	
	@Inject
	public CourseGroupRepository(IUserRepository userRepository)
	{
		this.userRepository = userRepository;
	}
	
	@Override
	public List<CourseGroup> getCourseGroupsList(String username) throws Exception
	{
		List<CourseGroup> courseGroups = null;
		
		User u = userRepository.getUserByUsername(username);
		
		if (u != null)
		{
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("username", username);
			
			switch (u.getSelectedRole())
			{
				case SUPERUSER:
				case ADMIN:
					
					courseGroups = dbHelper.getQueryResult("SELECT p FROM CourseGroup p", CourseGroup.class);
					
					break;
					
				case PROGRAM_MANAGER:
					
					courseGroups = dbHelper.getQueryResult(
							"SELECT DISTINCT cg FROM CourseGroup cg JOIN cg.course c JOIN c.subject s1, Program p JOIN p.subjects s2 JOIN p.programManager u WHERE s1.id = s2.id AND u.username = :username",
							CourseGroup.class, parameters);
							
					break;
					
				case LECTURER:
					
					courseGroups = dbHelper.getQueryResult(
							"SELECT DISTINCT cg FROM CourseGroup cg JOIN cg.course c JOIN c.lecturer l WHERE l.username = :username",
							CourseGroup.class, parameters);
							
					break;
					
				case INSTRUCTOR:
					
					courseGroups = dbHelper.getQueryResult(
							"SELECT DISTINCT cg FROM CourseGroup cg LEFT JOIN cg.instructor i WHERE i.username = :username",
							CourseGroup.class, parameters);
							
					break;
					
				case STUDENT:
					
					courseGroups = dbHelper.getQueryResult(
							"SELECT DISTINCT cg FROM CourseGroupMember m JOIN m.user u JOIN CourseGroup cg JOIN cg.members ms WHERE m.id = ms.id AND u.username = :username",
							CourseGroup.class, parameters);
					
					break;
				
				default:
					
					break;
			}
		}
		
		return courseGroups;
	}
	
	@Override
	public CourseGroup getCourseGroup(String username, long id) throws Exception
	{
		CourseGroup courseGroup = null;
		
		User u = userRepository.getUserByUsername(username);
		
		if (u != null)
		{
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("id", id);
			
			switch (u.getSelectedRole())
			{
				case SUPERUSER:
				case ADMIN:
					
					courseGroup = dbHelper.getFirst("SELECT p FROM CourseGroup p JOIN FETCH p.members WHERE p.id = :id", CourseGroup.class,
							parameters);
					
					break;
					
				case PROGRAM_MANAGER:
					
					parameters.put("username", username);
					
					courseGroup = dbHelper.getFirst(
							"SELECT DISTINCT cg FROM CourseGroup cg JOIN cg.course c JOIN c.subject s1, Program p JOIN p.subjects s2 JOIN p.programManager u WHERE s1.id = s2.id AND u.username = :username AND cg.id = :id",
							CourseGroup.class, parameters);
							
					break;
					
				case LECTURER:
					
					parameters.put("username", username);
					
					courseGroup = dbHelper.getFirst(
							"SELECT DISTINCT cg FROM CourseGroup cg JOIN cg.course c JOIN c.lecturer l WHERE l.username = :username AND cg.id = :id",
							CourseGroup.class, parameters);
							
					break;
					
				case INSTRUCTOR:
					
					parameters.put("username", username);
					
					courseGroup = dbHelper.getFirst(
							"SELECT DISTINCT cg FROM CourseGroup cg LEFT JOIN cg.instructor i WHERE cg.id = :id AND i.username = :username",
							CourseGroup.class, parameters);
							
					break;
					
				default:
					
					break;
			}
		}
		
		return courseGroup;
	}
	
	@Override
	public CourseGroup getStudentCourseGroup(Course course, User student) throws Exception
	{
		CourseGroup courseGroup = null;
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		parameters.put("course_id", course.getId());
		parameters.put("student_id", student.getId());
		
		courseGroup = dbHelper.getFirst(
				"SELECT cg FROM CourseGroup cg INNER JOIN cg.members m WHERE cg.course.id = :course_id AND m.id = :student_id",
				CourseGroup.class,
				parameters);
				
		return courseGroup;
	}
	
	@Override
	public void createOrUpdateCourseGroup(CourseGroup p) throws Exception
	{
		try
		{
			if (p.getId() == null || p.getId() <= 0)
			{
				for (CourseGroupMember gm : p.getMembers())
				{
					dbHelper.addEntity(gm);
				}
				
				dbHelper.addEntity(p);
			}
			else
			{
				CourseGroup cg = em.find(CourseGroup.class, p.getId());

				List<CourseGroupMember> delete = cg.getMembers().stream().filter(x -> !p.getMembers().contains(x)).collect(Collectors.toList());
				
				for (CourseGroupMember gm : delete)
				{
					em.remove(gm);
				}
				
				List<CourseGroupMember> create = p.getMembers().stream().filter(x -> x.getId() == null || x.getId() <= 0)
						.collect(Collectors.toList());
				
				for (CourseGroupMember gm : create)
				{
					em.persist(gm);
				}
				
				dbHelper.updateEntity(p);
			}
		}
		catch (Exception e)
		{
			LogHelper.error(e);
			
			throw e;
		}
	}
	
	@Override
	public void deleteCourseGroup(long id) throws Exception
	{
		//		Course s = getCourse(id);
		//		
		//		Map<String, Object> parameters = new HashMap<String, Object>();
		//		
		//		parameters.put("id", id);
		//		
		//		List<Program> programs = dbHelper.getQueryResult("SELECT p FROM Program p JOIN FETCH p.courses s WHERE s.id = :id", Program.class,
		//				parameters);
		//		
		//		for (Program p : programs)
		//		{
		//			p.getCourses().remove(s);
		//			
		//			dbHelper.createOrUpdateEntity(p);
		//		}
		//		
		dbHelper.deleteEntity(id, CourseGroup.class);
	}
}
