package edu.uoc.ictflag.ela.dal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import edu.uoc.ictflag.ela.model.Exercise;
import edu.uoc.ictflag.ela.model.Tool;
import edu.uoc.ictflag.security.dal.IUserRepository;
import edu.uoc.ictflag.security.model.User;

@Stateless
@Transactional(TxType.REQUIRED)
public class ExerciseRepository extends ELABaseRepository implements IExerciseRepository
{
	IUserRepository userRepository;
	
	@Inject
	public ExerciseRepository(IUserRepository userRepository)
	{
		this.userRepository = userRepository;
	}
	
	@Override
	public Exercise getExercise(Tool tool, String name) throws Exception
	{
		Exercise exercise = null;
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		parameters.put("tool_id", tool.getId());
		parameters.put("name", name);
		
		exercise = dbHelper.getFirst("SELECT e FROM Exercise e JOIN e.tool t WHERE t.id = :tool_id AND e.name = :name", Exercise.class, parameters);
		
		return exercise;
	}
	
	@Override
	public Exercise getExerciseById(long id) throws Exception
	{
		Exercise exercise = null;
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		parameters.put("id", id);
		
		exercise = dbHelper.getFirst("SELECT e FROM Exercise e WHERE e.id = :id", Exercise.class, parameters);
		
		return exercise;
	}
	
	@Override
	public void createOrUpdateExercise(Exercise p) throws Exception
	{
		dbHelper.createOrUpdateEntity(p);
	}
	
	@Override
	public List<Exercise> getExercisesList(String username) throws Exception
	{
		List<Exercise> exercises = null;
		
		User u = userRepository.getUserByUsername(username);
		
		if (u != null)
		{
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("username", username);
			
			switch (u.getSelectedRole())
			{
				case SUPERUSER:
				case ADMIN:
					
					exercises = dbHelper.getQueryResult("SELECT e FROM Exercise e", Exercise.class);
					
					break;
				
				case PROGRAM_MANAGER:
					
					exercises = dbHelper.getQueryResult(
							"SELECT DISTINCT e FROM Exercise e JOIN Assignment a JOIN a.exercises e2 JOIN a.course c JOIN c.subject s1 JOIN Program p JOIN p.subjects s2 JOIN p.programManager u WHERE e.id = e2.id AND s1.id = s2.id AND u.username = :username",
							Exercise.class, parameters);
					
					break;
				
				case LECTURER:
					
					exercises = dbHelper.getQueryResult(
							"SELECT DISTINCT e FROM Exercise e JOIN Assignment a JOIN a.exercises e2 JOIN a.course c JOIN c.lecturer l WHERE e.id = e2.id AND l.username = :username",
							Exercise.class, parameters);
					
					break;
				
				case INSTRUCTOR:
					
					exercises = dbHelper.getQueryResult(
							"SELECT DISTINCT e FROM Exercise e JOIN Assignment a JOIN a.exercises e2 JOIN a.course c JOIN CourseGroup cg JOIN cg.course c2 JOIN cg.instructor i WHERE e.id = e2.id AND c.id = c2.id AND i.username = :username",
							Exercise.class, parameters);
					
					break;
				
				case STUDENT:
					
					exercises = dbHelper.getQueryResult(
							"SELECT DISTINCT e FROM Exercise e JOIN Assignment a JOIN a.exercises e2 JOIN a.course c JOIN CourseGroup cg JOIN cg.course c2 JOIN cg.members m JOIN m.user u WHERE e.id = e2.id AND c.id = c2.id AND u.username = :username",
							Exercise.class, parameters);
					
					break;
				
				default:
					
					break;
			}
		}
		
		return exercises;
	}
}
