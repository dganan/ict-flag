package edu.uoc.ictflag.ela.dal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import edu.uoc.ictflag.ela.model.Assignment;
import edu.uoc.ictflag.institution.model.Course;
import edu.uoc.ictflag.security.dal.IUserRepository;
import edu.uoc.ictflag.security.model.User;

@Stateless
@Transactional(TxType.REQUIRED)
public class AssignmentRepository extends ELABaseRepository implements IAssignmentRepository
{
	IUserRepository userRepository;
	
	@Inject
	public AssignmentRepository(IUserRepository userRepository)
	{
		this.userRepository = userRepository;
	}
	
	@Override
	public Assignment getAssignment(Course course, String name) throws Exception
	{
		Assignment assignment = null;
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		if (course != null)
		{
			parameters.put("course_id", course.getId());
			parameters.put("name", name);
			
			assignment = dbHelper.getFirst("SELECT a FROM Assignment a JOIN a.course c WHERE c.id = :course_id AND a.name = :name", Assignment.class,
					parameters);
		}
		else
		{
			parameters.put("name", name);
			
			assignment = dbHelper.getFirst("SELECT a FROM Assignment a LEFT JOIN a.course c WHERE c IS NULL AND a.name = :name", Assignment.class,
					parameters);
		}
		
		return assignment;
	}
	
	@Override
	public Assignment getAssignmentById(long id) throws Exception
	{
		Assignment assignment = null;
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		parameters.put("id", id);
		
		assignment = dbHelper.getFirst("SELECT a FROM Assignment a WHERE a.id = :id", Assignment.class, parameters);
		
		return assignment;
	}
	
	@Override
	public void createOrUpdateAssignment(Assignment p) throws Exception
	{
		dbHelper.createOrUpdateEntity(p);
	}
	
	@Override
	public List<Assignment> getAssignmentsList(String username) throws Exception
	{
		List<Assignment> assignments = null;
		
		User u = userRepository.getUserByUsername(username);
		
		if (u != null)
		{
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("username", username);
			
			switch (u.getSelectedRole())
			{
				case SUPERUSER:
				case ADMIN:
					
					assignments = dbHelper.getQueryResult("SELECT s FROM Assignment s", Assignment.class);
					
					break;
				
				case PROGRAM_MANAGER:
					
					assignments = dbHelper.getQueryResult(
							"SELECT DISTINCT a FROM Assignment a JOIN a.course c JOIN c.subject s1, Program p JOIN p.subjects s2 JOIN p.programManager u WHERE s1.id = s2.id AND u.username = :username",
							Assignment.class, parameters);
					
					break;
				
				case LECTURER:
					
					assignments = dbHelper.getQueryResult(
							"SELECT DISTINCT a FROM Assignment a JOIN a.course c LEFT JOIN c.lecturer l WHERE l.username = :username",
							Assignment.class, parameters);
					
					break;
				
				case INSTRUCTOR:
					
					assignments = dbHelper.getQueryResult(
							"SELECT DISTINCT a FROM Assignment a JOIN a.course c JOIN CourseGroup cg JOIN cg.instructor i JOIN cg.course c2 WHERE c.id = c2.id AND i.username = :username",
							Assignment.class,
							parameters);
					
					break;
				
				case STUDENT:
					
					assignments = dbHelper.getQueryResult(
							"SELECT DISTINCT a FROM Assignment a JOIN a.course c JOIN CourseGroupMember m JOIN m.user u JOIN CourseGroup cg JOIN cg.members ms JOIN cg.course c2 WHERE c.id = c2.id AND m.id = ms.id AND u.username = :username",
							Assignment.class, parameters);
					
					break;
				
				default:
					
					break;
			}
		}
		
		return assignments;
	}
}
