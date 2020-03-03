package edu.uoc.ictflag.ela.dal;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import edu.uoc.ictflag.core.log.LogHelper;
import edu.uoc.ictflag.ela.model.Assignment;
import edu.uoc.ictflag.ela.model.AssignmentAction;
import edu.uoc.ictflag.ela.model.AssignmentData;
import edu.uoc.ictflag.ela.model.AssignmentDataRaw;
import edu.uoc.ictflag.ela.model.Tool;
import edu.uoc.ictflag.institution.dal.ICourseGroupRepository;
import edu.uoc.ictflag.institution.dal.ICourseRepository;
import edu.uoc.ictflag.institution.dal.IProgramRepository;
import edu.uoc.ictflag.institution.dal.ISemesterRepository;
import edu.uoc.ictflag.institution.dal.ISubjectRepository;
import edu.uoc.ictflag.institution.model.Course;
import edu.uoc.ictflag.institution.model.Program;
import edu.uoc.ictflag.institution.model.Semester;
import edu.uoc.ictflag.institution.model.Subject;
import edu.uoc.ictflag.security.dal.IUserRepository;
import edu.uoc.ictflag.security.model.User;

@Stateless
@Transactional(TxType.REQUIRES_NEW)
public class AssignmentDataRepository extends ELADataBaseRepository implements IAssignmentDataRepository
{
	@Inject
	public AssignmentDataRepository(IToolRepository toolRepository, IUserRepository userRepository, IProgramRepository programRepository, ISubjectRepository subjectRepository, ISemesterRepository semesterRepository, ICourseRepository courseRepository, ICourseGroupRepository courseGroupRepository, IAssignmentRepository assignmentRepository)
	{
		this.toolRepository = toolRepository;
		this.userRepository = userRepository;
		this.programRepository = programRepository;
		this.subjectRepository = subjectRepository;
		this.semesterRepository = semesterRepository;
		this.courseRepository = courseRepository;
		this.courseGroupRepository = courseGroupRepository;
		this.assignmentRepository = assignmentRepository;
	}
	
	@Override
	public void saveAssignmentDataRaw(AssignmentDataRaw assignmentDataRaw) throws Exception
	{
		ArrayList<Object> parameters = new ArrayList<Object>();
		
		parameters.add(assignmentDataRaw.getTool());
		parameters.add(assignmentDataRaw.getToolUUID());
		parameters.add(assignmentDataRaw.getUserId());
		parameters.add(assignmentDataRaw.getTimestamp());
		parameters.add(assignmentDataRaw.getProgramCode());
		parameters.add(assignmentDataRaw.getSubjectCode());
		parameters.add(assignmentDataRaw.getSemester());
		parameters.add(assignmentDataRaw.getAction());
		parameters.add(assignmentDataRaw.getAssignment());
		parameters.add(assignmentDataRaw.getGrade());
		
		dbHelper.executeUpdateNative(
				"INSERT INTO AssignmentDataRaw  (tool, tooluuid, userid, timestamp, programcode, subjectcode, semester, action, assignment, grade) VALUES (?,?,?,?,?,?,?,?,?,?)",
				parameters);
	}
		
	@Override
	public void processAssignmentDataRaw(AssignmentDataRaw assignmentDataRaw) throws Exception
	{
		try
		{
			AssignmentData assignmentData = new AssignmentData();
			
			setupTool(assignmentDataRaw, assignmentData);
			
			setupUser(assignmentDataRaw, assignmentData);
			
			setupProgram(assignmentDataRaw, assignmentData);
			
			setupSubject(assignmentDataRaw, assignmentData);
			
			setupSemester(assignmentDataRaw, assignmentData);
			
			setupCourse(assignmentData);
			
			setupAssignment(assignmentDataRaw, assignmentData);
			
			processAssignmentAction(assignmentDataRaw, assignmentData);
			
			setRowAsHandled(assignmentDataRaw);
			
			dbHelper.flush();
		}
		catch (Exception ex)
		{
			LogHelper.error(ex);
			
			throw ex;
		}
	}
	
	private void setRowAsHandled(AssignmentDataRaw assignmentDataRaw)
	{
		// SET ROW AS HANLDED
		
		dbHelper.executeUpdateNative("UPDATE AssignmentDataRaw SET handled = TRUE WHERE id = " + assignmentDataRaw.getId(), null);
	}
	
	private void processAssignmentAction(AssignmentDataRaw assignmentDataRaw, AssignmentData assignmentData) throws Exception
	{
		// Process assignment action
		
		AssignmentAction assignmentAction = AssignmentAction.fromString(assignmentDataRaw.getAction());
		
		Date date = new Date();
		
		try
		{
			if (assignmentDataRaw.getTimestamp() != null)
			{
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				date = formatter.parse(assignmentDataRaw.getTimestamp());
			}
		}
		catch (ParseException pe)
		{
			
		}
		
		switch (assignmentAction)
		{
			case ASSIGNMENT_START:
				
				assignmentData.setStartDate(date);
				assignmentData.setGrade(-1);
				processAssignmentStart(assignmentData);
				break;
			
			case ASSIGNMENT_END:
				
				assignmentData.setEndDate(date);
				assignmentData.setGrade(-1);
				processAssignmentEnd(assignmentData);
				break;
			
			case ASSIGNMENT_ASSESSMENT:
				
				assignmentData.setAssessmentDate(date);
				
				double grade = Double.parseDouble(assignmentDataRaw.getGrade());
				
				assignmentData.setGrade(grade);
				assignmentData.setGradeIncorrect(grade < 0 || grade > 10);
				processAssignmentAssessment(assignmentData);
				break;
		}
	}
	
	private void setupAssignment(AssignmentDataRaw assignmentDataRaw, AssignmentData assignmentData) throws Exception
	{
		// Set assignment
		
		Assignment assignment = assignmentRepository.getAssignment(assignmentData.getCourse(), assignmentDataRaw.getAssignment());
		
		// If does not exist, create and assign it
		if (assignment == null)
		{
			assignment = new Assignment();
			assignment.setName(assignmentDataRaw.getAssignment());
			assignment.setCourse(assignmentData.getCourse());
			
			assignmentRepository.createOrUpdateAssignment(assignment);

			dbHelper.flush();
		}

		assignmentData.setAssignment(assignment);
	}
	
	private void setupCourse(AssignmentData assignmentData) throws Exception
	{
		// Calculate course if possible
		
		if (assignmentData.getSubject() != null && assignmentData.getSemester() != null)
		{
			Course course = courseRepository.getCourse(assignmentData.getSubject(), assignmentData.getSemester());
			
			if (course == null)
			{
				course = fixUpCourse(assignmentData.getSubject(), assignmentData.getSemester());
				
				assignmentData.setCourseFixedUp(true);
			}
			
			assignmentData.setCourse(course);
			
			assignmentData.setCourseGroup(courseGroupRepository.getStudentCourseGroup(course, assignmentData.getStudent()));
		}
	}
	
	private void setupSemester(AssignmentDataRaw assignmentDataRaw, AssignmentData assignmentData) throws Exception
	{
		// Set semester
		
		String semesterCode = assignmentDataRaw.getSemester();
		
		Semester sem = null;
		
		if (semesterCode != null)
		{
			sem = semesterRepository.getSemesterByCode(semesterCode);
		}
		
		if (sem == null)
		{
			assignmentData.setSemesterFixedUp(true);
			
			// Get current semester (if any)
			sem = semesterRepository.getSemesterByDate(new Date());
		}
		
		assignmentData.setSemester(sem);
	}

	private void setupSubject(AssignmentDataRaw assignmentDataRaw, AssignmentData assignmentData) throws Exception
	{
		// Set subject
		
		String subjectCode = assignmentDataRaw.getSubjectCode();
		
		if (subjectCode != null)
		{
			if (assignmentDataRaw.getSubjectCode().trim().equals(""))
			{
				// is fixed up because the specified subject code is empty so set subject = null
				assignmentData.setSubjectFixedUp(true);
			}
			else
			{
				Subject s = subjectRepository.getSubjectByCode(subjectCode);
				
				if (s == null)
				{
					s = fixUpSubject(assignmentData.getProgram(), subjectCode);
					
					assignmentData.setSubjectFixedUp(true);
				}
				
				assignmentData.setSubject(s);
			}
		}
	}
	
	private void setupProgram(AssignmentDataRaw assignmentDataRaw, AssignmentData assignmentData) throws Exception
	{
		// Set program
		
		if (assignmentDataRaw.getProgramCode() != null)
		{
			Program p = programRepository.getProgramByCode(assignmentDataRaw.getProgramCode());
			
			if (p == null)
			{
				// is fixed up because the specified program code is not valid so set program = null
				assignmentData.setProgramFixedUp(true);
			}
			else
			{
				assignmentData.setProgram(p);
			}
		}
	}
	
	private void setupUser(AssignmentDataRaw assignmentDataRaw, AssignmentData assignmentData) throws Exception
	{
		// Set user
		User u = getStudent(assignmentDataRaw.getUserId());
		
		if (u == null)
		{
			u = fixUpStudent(assignmentDataRaw.getUserId());
			
			assignmentData.setStudentFixedUp(true);
		}
		
		assignmentData.setStudent(u);
	}
	
	private void setupTool(AssignmentDataRaw assignmentDataRaw, AssignmentData assignmentData) throws Exception
	{
		// Set tool
		assignmentData.setTool(toolRepository.getToolByCode(assignmentDataRaw.getTool()));
	}
	
	private void processAssignmentStart(AssignmentData assignmentData) throws Exception
	{
		assignmentData.setDuration_sec(0);
		
		dbHelper.addEntity(assignmentData);
		
		dbHelper.flush();
	}
	
	private void processAssignmentEnd(AssignmentData assignmentData) throws Exception
	{
		// Get last attempt
		
		AssignmentData lastAttempt = getLastAssignmentData(assignmentData.getProgram(), assignmentData.getSubject(), assignmentData.getTool(),
				assignmentData.getSemester(), assignmentData.getStudent(), assignmentData.getAssignment());
				
		if (lastAttempt == null || lastAttempt.getEndDate() != null)
		{
			assignmentData.setStartDate(assignmentData.getEndDate());
			assignmentData.setStartDateFixedUp(true);
			assignmentData.setEndDate(assignmentData.getEndDate());
			
			assignmentData.setDuration_sec(0);
			
			lastAttempt = assignmentData;
			// The row is new so it must be created in database
			dbHelper.addEntity(lastAttempt);
		}
		else
		{
			lastAttempt.setEndDate(assignmentData.getEndDate());
			
			lastAttempt.setDuration_sec((lastAttempt.getEndDate().getTime() - lastAttempt.getStartDate().getTime()) / 1000);
		}
		
		
		dbHelper.flush();
	}
	
	private void processAssignmentAssessment(AssignmentData assignmentData) throws Exception
	{
		// Get last attempt
		
		AssignmentData lastAttempt = getLastAssignmentData(assignmentData.getProgram(), assignmentData.getSubject(), assignmentData.getTool(),
				assignmentData.getSemester(), assignmentData.getStudent(), assignmentData.getAssignment());
				
		if (lastAttempt == null || !lastAttempt.isGradeNull())
		{
			assignmentData.setStartDate(assignmentData.getAssessmentDate());
			assignmentData.setStartDateFixedUp(true);
			assignmentData.setEndDate(assignmentData.getAssessmentDate());
			assignmentData.setEndDateFixedUp(true);
			
			assignmentData.setDuration_sec(0);
			
			lastAttempt = assignmentData;
			// The row is new so it must be created in database
			dbHelper.addEntity(lastAttempt);
		}
		else
		{
			if (lastAttempt.getEndDate() == null)
			{
				lastAttempt.setEndDate(assignmentData.getAssessmentDate());
				lastAttempt.setEndDateFixedUp(true);
			}
			
			lastAttempt.setDuration_sec((lastAttempt.getEndDate().getTime() - lastAttempt.getStartDate().getTime()) / 1000);
		}
		
		lastAttempt.setAssessmentDate(assignmentData.getAssessmentDate());
		lastAttempt.setGrade(assignmentData.getGrade());
		lastAttempt.setGradeIncorrect(assignmentData.isGradeIncorrect());
		
		dbHelper.flush();
	}
	
	private AssignmentData getLastAssignmentData(Program program, Subject subject, Tool tool, Semester semester, User student, Assignment assignment)
			throws Exception
	{
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		StringBuilder sb = new StringBuilder("SELECT d FROM AssignmentData d WHERE ");
		
		addParameterSafely(sb, parameters, "program", program, true);
		addParameterSafely(sb, parameters, "subject", subject, true);
		addParameterSafely(sb, parameters, "tool", tool, true);
		addParameterSafely(sb, parameters, "semester", semester, true);
		addParameterSafely(sb, parameters, "student", student, true);
		addParameterSafely(sb, parameters, "assignment", assignment, false);
		
		sb.append("ORDER BY d.startDate DESC");
		
		return dbHelper.getFirst(sb.toString(), AssignmentData.class, parameters, false);
	}
}
