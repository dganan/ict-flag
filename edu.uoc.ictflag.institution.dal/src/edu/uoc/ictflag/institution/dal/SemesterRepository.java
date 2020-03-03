package edu.uoc.ictflag.institution.dal;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import edu.uoc.ictflag.institution.model.Semester;
import edu.uoc.ictflag.security.dal.IUserRepository;
import edu.uoc.ictflag.security.model.User;

@Stateless
@Transactional(TxType.REQUIRED)
public class SemesterRepository extends InstitutionBaseRepository implements ISemesterRepository
{
	IUserRepository userRepository;
	
	@Inject
	public SemesterRepository(IUserRepository userRepository)
	{
		this.userRepository = userRepository;
	}
	
	@Override
	public List<Semester> getSemestersList(String username) throws Exception
	{
		List<Semester> semesters = null;
		
		User u = userRepository.getUserByUsername(username);
		
		if (u != null)
		{
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("username", username);
			
			switch (u.getSelectedRole())
			{
				case SUPERUSER:
				case ADMIN:
					
					semesters = dbHelper.getQueryResult("SELECT s FROM Semester s", Semester.class);
					
					break;

				case PROGRAM_MANAGER:
					
					semesters = dbHelper.getQueryResult(
							"SELECT DISTINCT s FROM Course c JOIN c.semester s JOIN c.subject s1 JOIN Program p JOIN p.subjects s2 JOIN p.programManager u WHERE s1.id = s2.id AND u.username = :username",
							Semester.class, parameters);
					
					break;
				
				case LECTURER:
					
					semesters = dbHelper.getQueryResult(
							"SELECT DISTINCT s FROM Course c JOIN c.semester s JOIN c.lecturer l WHERE l.username = :username",
							Semester.class, parameters);
					
					break;
				
				case INSTRUCTOR:
					
					semesters = dbHelper.getQueryResult(
							"SELECT DISTINCT s FROM CourseGroup cg JOIN cg.instructor i JOIN cg.course c JOIN c.semester s WHERE i.username = :username",
							Semester.class, parameters);
					
					break;
				
				case STUDENT:
					
					semesters = dbHelper.getQueryResult(
							"SELECT DISTINCT s FROM CourseGroupMember m JOIN m.user u JOIN CourseGroup cg JOIN cg.members ms JOIN cg.course c JOIN c.semester s WHERE m.id = ms.id AND u.username = :username",
							Semester.class, parameters);
					
					break;
				
				default:
					
					break;
			}
		}
		
		return semesters;
	}
	
	@Override
	public Semester getSemester(String username, long id) throws Exception
	{
		Semester semester = null;
		
		User u = userRepository.getUserByUsername(username);
		
		if (u != null)
		{
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("username", username);
			parameters.put("id", id);
			
			switch (u.getSelectedRole())
			{
				case SUPERUSER:
					
					semester = dbHelper.getEntity(id, Semester.class);
					
					break;
					
				default:
					
					break;
			}
		}
		
		return semester;
	}
	
	@Override
	public void createOrUpdateSemester(Semester p) throws Exception
	{
		dbHelper.createOrUpdateEntity(p);
	}
	
	@Override
	public void deleteSemester(long id) throws Exception
	{
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		parameters.put("id", id);
		
		dbHelper.deleteEntity(id, Semester.class);
	}
	
	@Override
	public Semester getSemester(int academicYear, int number) throws Exception
	{
		Map<String, Object> parameters = new HashMap<String, Object>();
			
		parameters.put("academicYear", academicYear);
		parameters.put("number", number);
		
		return dbHelper.getFirst("SELECT s FROM Semester s WHERE s.academicYear = :academicYear AND s.number = :number", Semester.class, parameters);
	}
	
	@Override
	public Semester getSemesterByDate(Date date) throws Exception
	{
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		parameters.put("date", date);
		
		return dbHelper.getFirst("SELECT s FROM Semester s WHERE s.startDate <= :date AND s.endDate >= :date", Semester.class, parameters);
	}
	
	@Override
	public List<Semester> getSemesters(String username) throws Exception
	{
		List<Semester> semesters = null;
		
		User u = userRepository.getUserByUsername(username);
		
		if (u != null)
		{
			semesters = dbHelper.getQueryResult("SELECT s FROM Semester s ORDER BY s.academicYear, s.number", Semester.class);
		}
		
		return semesters;
	}
	
	@Override
	public Semester getSemesterByCode(String semesterCode) throws Exception
	{
		Semester s = null;
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		String[] parts = semesterCode.split("-");
		
		if (parts.length == 2)
		{
			try
			{
				parameters.put("academicYear", Integer.parseInt(parts[0]));
				parameters.put("number", Integer.parseInt(parts[1]));
			}
			catch (NumberFormatException e)
			{
				return s;
			}
			
			s = dbHelper.getFirst("SELECT s FROM Semester s WHERE s.academicYear = :academicYear AND s.number = :number", Semester.class, parameters);
		}
		else if (parts.length == 1 && parts[0].length() == 5)
		{
			try
			{
				parameters.put("academicYear", Integer.parseInt(parts[0].substring(0, 4)));
				parameters.put("number", Integer.parseInt(parts[0].substring(4)));
			}
			catch (NumberFormatException e)
			{
				return s;
			}
			
			s = dbHelper.getFirst("SELECT s FROM Semester s WHERE s.academicYear = :academicYear AND s.number = :number", Semester.class, parameters);
		}
		
		return s;
	}
}
