package edu.uoc.ictflag.ela.bll;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import edu.uoc.ictflag.core.dal.SearchParameters;
import edu.uoc.ictflag.ela.dal.IReportRepository;
import edu.uoc.ictflag.ela.model.Assignment;
import edu.uoc.ictflag.ela.model.Exercise;
import edu.uoc.ictflag.ela.model.ReportDataItem;
import edu.uoc.ictflag.ela.model.Tool;
import edu.uoc.ictflag.institution.model.Course;
import edu.uoc.ictflag.institution.model.CourseGroup;
import edu.uoc.ictflag.institution.model.Program;
import edu.uoc.ictflag.institution.model.Semester;
import edu.uoc.ictflag.institution.model.Subject;

@Stateless
public class ReportManager<D extends ReportDataItem> implements IReportManager<D>, Serializable
{
	private static final long serialVersionUID = 1L;
	
	IReportRepository<D> reportRepository;
	
	@Override
	public List<Tool> getTools(String username) throws Exception
	{
		return reportRepository.getTools(username);
	}
	
	@Override
	public List<Program> getPrograms(String username) throws Exception
	{
		return reportRepository.getPrograms(username);
	}
	
	@Override
	public List<Subject> getSubjects(String username) throws Exception
	{
		return reportRepository.getSubjects(username);
	}
	
	@Override
	public List<Course> getCourses(String username) throws Exception
	{
		return reportRepository.getCourses(username);
	}
	
	@Override
	public List<Assignment> getAssignments(String username) throws Exception
	{
		return reportRepository.getAssignments(username);
	}
	
	@Override
	public List<CourseGroup> getCourseGroups(String username) throws Exception
	{
		return reportRepository.getCourseGroups(username);
	}
	
	@Override
	public List<Semester> getSemesters(String username) throws Exception
	{
		return reportRepository.getSemesters(username);
	}
	
	@Override
	public List<Exercise> getExercises(String username) throws Exception
	{
		return reportRepository.getExercises(username);
	}
	
	@Override
	public int getMaxNumberOfAttempts(String username) throws Exception
	{
		return reportRepository.getMaxNumberOfAttempts(username);
	}
	
	@Override
	public Map<Tool, List<Exercise>> getToolsWithExercises(String username) throws Exception
	{
		return reportRepository.getToolsWithExercises(username);
	}
	
	@Override
	public Map<Course, List<Assignment>> getCoursesWithAssignments(String username) throws Exception
	{
		return reportRepository.getCoursesWithAssignments(username);
	}
	
	@Override
	public List<D> getReportData(String username, SearchParameters searchParameters) throws Exception
	{
		return reportRepository.getReportData(username, searchParameters);
	}
}


