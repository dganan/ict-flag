package edu.uoc.ictflag.ela.dal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import edu.uoc.ictflag.ela.model.Tool;
import edu.uoc.ictflag.security.dal.IUserRepository;
import edu.uoc.ictflag.security.model.User;

@Stateless
@Transactional(TxType.REQUIRED)
public class ToolRepository extends ELABaseRepository implements IToolRepository
{
	IUserRepository userRepository;
	
	@Inject
	public ToolRepository(IUserRepository userRepository)
	{
		this.userRepository = userRepository;
	}
	
	@Override
	public List<Tool> getToolsList(String username) throws Exception
	{
		List<Tool> tools = null;
		
		User u = userRepository.getUserByUsername(username);
		
		if (u != null)
		{
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("username", username);
			
			switch (u.getSelectedRole())
			{
				case SUPERUSER:
				case ADMIN:
					
					tools = dbHelper.getQueryResult("SELECT t FROM Tool t", Tool.class);
					
					break;
				
				case PROGRAM_MANAGER:
					
					tools = dbHelper.getQueryResult(
							"SELECT distinct t FROM Exercise e JOIN e.tool t JOIN Assignment a JOIN a.exercises e2 JOIN a.course c JOIN c.subject s1 JOIN Program p JOIN p.subjects s2 JOIN p.programManager u WHERE e.id = e2.id AND s1.id = s2.id AND u.username = :username",
							Tool.class, parameters);
					
					break;
				
				case LECTURER:
					
					tools = dbHelper.getQueryResult(
							"SELECT distinct t FROM Exercise e JOIN e.tool t JOIN Assignment a JOIN a.exercises e2 JOIN a.course c JOIN c.lecturer l WHERE e.id = e2.id AND l.username = :username",
							Tool.class, parameters);
					
					break;
				
				case INSTRUCTOR:
					
					tools = dbHelper.getQueryResult(
							"SELECT distinct t FROM Exercise e JOIN e.tool t JOIN Assignment a JOIN a.exercises e2 JOIN a.course c JOIN CourseGroup cg JOIN cg.course c2 JOIN cg.instructor i WHERE e.id = e2.id AND c.id = c2.id AND i.username = :username",
							Tool.class, parameters);
					
					break;
				
				case STUDENT:
					
					tools = dbHelper.getQueryResult(
							"SELECT distinct t FROM Exercise e JOIN e.tool t JOIN Assignment a JOIN a.exercises e2 JOIN a.course c JOIN CourseGroup cg JOIN cg.course c2 JOIN cg.members m JOIN m.user u WHERE e.id = e2.id AND c.id = c2.id AND u.username = :username",
							Tool.class, parameters);
					
					break;

				default:
					
					break;
			}
		}
		
		return tools;
	}
	
	@Override
	public Tool getTool(String username, long id) throws Exception
	{
		Tool tool = null;
		
		User u = userRepository.getUserByUsername(username);
		
		if (u != null)
		{
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("id", id);
			
			switch (u.getSelectedRole())
			{
				case SUPERUSER:
				case ADMIN:
					
					tool = dbHelper.getFirst("SELECT t FROM Tool t WHERE t.id = :id", Tool.class, parameters);
					
					break;

				default:
					
					break;
			}
		}
		
		return tool;
	}
	
	@Override
	public Tool getToolById(long id) throws Exception
	{
		Map<String, Object> parameters = new HashMap<String, Object>();
			
		parameters.put("id", id);
			
		Tool tool = dbHelper.getFirst("SELECT t FROM Tool t WHERE t.id = :id", Tool.class, parameters);
		
		return tool;
	}
	
	@Override
	public Tool getToolByCode(String code) throws Exception
	{
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		parameters.put("code", code);
		
		Tool s = dbHelper.getFirst("SELECT t FROM Tool t WHERE t.code = :code", Tool.class, parameters);
		
		return s;
	}
	
	@Override
	public void createOrUpdateTool(Tool p) throws Exception
	{
		dbHelper.createOrUpdateEntity(p);
	}
	
	@Override
	public void deleteTool(long id) throws Exception
	{
		dbHelper.deleteEntity(id, Tool.class);
	}
}
