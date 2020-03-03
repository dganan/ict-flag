package edu.uoc.ictflag.institution.dal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import edu.uoc.ictflag.core.IctFlagValidationException;
import edu.uoc.ictflag.institution.model.InstitutionValidationErrors;
import edu.uoc.ictflag.institution.model.Program;
import edu.uoc.ictflag.institution.model.Subject;
import edu.uoc.ictflag.security.dal.IUserRepository;
import edu.uoc.ictflag.security.model.User;

@Stateless
@Transactional(TxType.REQUIRED)
public class SubjectRepository extends InstitutionBaseRepository implements ISubjectRepository
{
	IUserRepository userRepository;
	
	@Inject
	public SubjectRepository(IUserRepository userRepository)
	{
		this.userRepository = userRepository;
	}
	
	@Override
	public List<Subject> getSubjectsList(String username) throws Exception
	{
		List<Subject> subjects = null;
		
		User u = userRepository.getUserByUsername(username);
		
		if (u != null)
		{
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("username", username);
			
			switch (u.getSelectedRole())
			{
				case SUPERUSER:
				case ADMIN:
					
					subjects = dbHelper.getQueryResult("SELECT s FROM Subject s", Subject.class);
					
					break;
					
				case PROGRAM_MANAGER:
					
					subjects = dbHelper.getQueryResult(
							"SELECT DISTINCT s FROM Program p JOIN p.subjects s JOIN p.programManager pm WHERE pm.username = :username",
							Subject.class,
							parameters);
					
					break;
					
				case LECTURER:
					
					subjects = dbHelper.getQueryResult(
							"SELECT DISTINCT s FROM Course c JOIN c.lecturer l JOIN c.subject s WHERE l.username = :username",
							Subject.class, parameters);
							
					break;
					
				case INSTRUCTOR:
					
					subjects = dbHelper.getQueryResult(
							"SELECT DISTINCT s FROM CourseGroup cg JOIN cg.instructor i JOIN cg.course c JOIN c.subject s WHERE i.username = :username",
							Subject.class, parameters);
							
					break;

				case STUDENT:
					
					subjects = dbHelper.getQueryResult(
							"SELECT DISTINCT s FROM CourseGroupMember m JOIN m.user u JOIN CourseGroup cg JOIN cg.members ms JOIN cg.instructor i JOIN cg.course c JOIN c.subject s WHERE m.id = ms.id AND u.username = :username",
							Subject.class, parameters);
					
					break;
				
				default:
					
					break;
			}
		}
		
		return subjects;
	}
	
	@Override
	public Subject getSubject(String username, long id) throws Exception
	{
		Subject subject = null;
		
		User u = userRepository.getUserByUsername(username);
		
		if (u != null)
		{
			switch (u.getSelectedRole())
			{
				case SUPERUSER:
				case ADMIN:
					
					subject = dbHelper.getEntity(id, Subject.class);
					
					break;
					
				default:
					
					break;
			}
		}
		
		return subject;
	}
	
	@Override
	public Subject getSubjectByCode(String code) throws Exception
	{
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		parameters.put("subjectCode", code);
		
		Subject s = dbHelper.getFirst("SELECT s FROM Subject s WHERE s.code = :subjectCode", Subject.class, parameters);
		
		return s;
	}
	
	@Override
	public void createOrUpdateSubject(Subject p) throws Exception
	{
		dbHelper.createOrUpdateEntity(p);
	}
	
	@Override
	public void deleteSubject(long id) throws Exception
	{
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		parameters.put("id", id);
		
		if ((long) dbHelper.getScalarValue("SELECT COUNT(c) FROM Course c JOIN FETCH c.subject s WHERE s.id = :id", parameters) > 0)
		{
			throw new IctFlagValidationException(InstitutionValidationErrors.SubjectCannotBeDeletedBecauseDependentCoursesAlreadyExist);
		}
		
		Subject s = dbHelper.getEntity(id, Subject.class);
		
		List<Program> programs = dbHelper.getQueryResult("SELECT p FROM Program p JOIN FETCH p.subjects s WHERE s.id = :id", Program.class,
				parameters);
				
		for (Program p : programs)
		{
			p.getSubjects().remove(s);
			
			dbHelper.createOrUpdateEntity(p);
		}
		
		dbHelper.deleteEntity(id, Subject.class);
	}
}
