package edu.uoc.ictflag.institution.dal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import edu.uoc.ictflag.institution.model.Program;
import edu.uoc.ictflag.security.dal.IUserRepository;
import edu.uoc.ictflag.security.model.User;

@Stateless
@Transactional(TxType.REQUIRED)
public class ProgramRepository extends InstitutionBaseRepository implements IProgramRepository
{
	IUserRepository userRepository;
	
	@Inject
	public ProgramRepository(IUserRepository userRepository)
	{
		this.userRepository = userRepository;
	}
	
	@Override
	public List<Program> getProgramsList(String username) throws Exception
	{
		List<Program> programs = null;
		
		User u = userRepository.getUserByUsername(username);
		
		if (u != null)
		{
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("username", username);
			
			switch (u.getSelectedRole())
			{
				case SUPERUSER:
				case ADMIN:
					
					programs = dbHelper.getQueryResult("SELECT p FROM Program p", Program.class);
					
					break;
					
				case PROGRAM_MANAGER:
					
					programs = dbHelper.getQueryResult(
							"SELECT DISTINCT p FROM Program p LEFT JOIN p.programManager u WHERE u.username = :username", Program.class,
							parameters);
							
					break;

				case LECTURER:
					
					programs = dbHelper.getQueryResult(
							"SELECT DISTINCT p FROM Course c JOIN c.subject s JOIN c.lecturer l JOIN Program p JOIN p.subjects sp WHERE s.id = sp.id AND l.username = :username",
							Program.class,
							parameters);
							
					break;

				case INSTRUCTOR:
					
					programs = dbHelper.getQueryResult(
							"SELECT DISTINCT p FROM CourseGroup cg JOIN cg.course c JOIN cg.instructor i JOIN c.subject s JOIN Program p JOIN p.subjects sp WHERE s.id = sp.id AND i.username = :username",
							Program.class, parameters);
					
					break;

				case STUDENT:
					
					programs = dbHelper.getQueryResult(
							"SELECT DISTINCT p FROM CourseGroup cg JOIN cg.members m JOIN m.user u JOIN cg.course c JOIN c.subject s JOIN c.lecturer l JOIN Program p JOIN p.subjects sp WHERE s.id = sp.id AND u.username = :username",
							Program.class, parameters);
					
					break;
				
				default:
					
					break;
			}
		}
		
		return programs;
	}
	
	@Override
	public Program getProgram(String username, long id) throws Exception
	{
		Program program = null;
		
		User u = userRepository.getUserByUsername(username);
		
		if (u != null)
		{
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("id", id);
			
			switch (u.getSelectedRole())
			{
				case SUPERUSER:
				case ADMIN:
					
					program = dbHelper.getFirst("SELECT p FROM Program p JOIN FETCH p.subjects WHERE p.id = :id", Program.class, parameters);
					
					break;
					
				case PROGRAM_MANAGER:
					
					parameters.put("username", username);
					
					program = dbHelper.getFirst(
							"SELECT p FROM Program p JOIN FETCH p.subjects JOIN p.programManager u WHERE p.id = :id AND u.username = :username",
							Program.class,
							parameters);
							
					break;
					
				default:
					
					break;
			}
		}
		
		return program;
	}
	
	@Override
	public Program getProgramByCode(String code) throws Exception
	{
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		parameters.put("code", code);
		
		Program p = dbHelper.getFirst("SELECT p FROM Program p WHERE p.code = :code", Program.class, parameters);
		
		return p;
	}
	
	@Override
	public void createOrUpdateProgram(Program p) throws Exception
	{
		dbHelper.createOrUpdateEntity(p);
	}
	
	@Override
	public void deleteProgram(long id) throws Exception
	{
		dbHelper.deleteEntity(id, Program.class);
	}
}
