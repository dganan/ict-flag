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
import edu.uoc.ictflag.ela.model.Exercise;
import edu.uoc.ictflag.ela.model.ExerciseAction;
import edu.uoc.ictflag.ela.model.ExerciseData;
import edu.uoc.ictflag.ela.model.ExerciseDataRaw;
import edu.uoc.ictflag.ela.model.Outcome;
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
public class ExerciseDataRepository extends ELADataBaseRepository implements IExerciseDataRepository
{
	@Inject
	public ExerciseDataRepository(IToolRepository toolRepository, IUserRepository userRepository, IProgramRepository programRepository, ISubjectRepository subjectRepository, ISemesterRepository semesterRepository, ICourseRepository courseRepository, ICourseGroupRepository courseGroupRepository, IAssignmentRepository assignmentRepository, IExerciseRepository exerciseRepository)
	{
		this.toolRepository = toolRepository;
		this.userRepository = userRepository;
		this.programRepository = programRepository;
		this.subjectRepository = subjectRepository;
		this.semesterRepository = semesterRepository;
		this.courseRepository = courseRepository;
		this.courseGroupRepository = courseGroupRepository;
		this.assignmentRepository = assignmentRepository;
		this.exerciseRepository = exerciseRepository;
	}
	
	@Override
	public void saveExerciseDataRaw(ExerciseDataRaw exerciseDataRaw) throws Exception
	{
		ArrayList<Object> parameters = new ArrayList<Object>();
		
		parameters.add(exerciseDataRaw.getTool());
		parameters.add(exerciseDataRaw.getToolUUID());
		parameters.add(exerciseDataRaw.getUserId());
		parameters.add(exerciseDataRaw.getTimestamp());
		parameters.add(exerciseDataRaw.getProgramCode());
		parameters.add(exerciseDataRaw.getSubjectCode());
		parameters.add(exerciseDataRaw.getSemester());
		parameters.add(exerciseDataRaw.getAction());
		parameters.add(exerciseDataRaw.getAssignment());
		parameters.add(exerciseDataRaw.getExercise());
		parameters.add(exerciseDataRaw.getOutcome());
		parameters.add(exerciseDataRaw.getGrade());
		
		dbHelper.executeUpdateNative(
				"INSERT INTO ExerciseDataRaw (tool, tooluuid, userid, timestamp, programcode, subjectcode, semester, action, assignment, exercise, outcome, grade) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)",
				parameters);
	}
	
	@Override
	public void processExerciseDataRaw(ExerciseDataRaw exerciseDataRaw) throws Exception
	{
		try
		{
			ExerciseData exerciseData = new ExerciseData();

			setupTool(exerciseDataRaw, exerciseData);
			
			setupUser(exerciseDataRaw, exerciseData);
			
			setupProgram(exerciseDataRaw, exerciseData);
			
			setupSubject(exerciseDataRaw, exerciseData);
			
			setupSemester(exerciseDataRaw, exerciseData);
			
			setupCourse(exerciseData);
			
			setupExercise(exerciseDataRaw, exerciseData);
			
			setupAssignment(exerciseDataRaw, exerciseData);
			
			processExerciseAction(exerciseDataRaw, exerciseData);
			
			setRowAsHandled(exerciseDataRaw);
		}
		catch (Exception ex)
		{
			LogHelper.error(ex);
			
			throw ex;
		}
	}

	private void setRowAsHandled(ExerciseDataRaw exerciseDataRaw)
	{
		// SET ROW AS HANLDED
		
		dbHelper.executeUpdateNative("UPDATE ExerciseDataRaw SET handled = TRUE WHERE id = " + exerciseDataRaw.getId(), null);
	}

	private void processExerciseAction(ExerciseDataRaw exerciseDataRaw, ExerciseData exerciseData) throws Exception
	{
		// Process exercise action
		
		ExerciseAction exerciseAction = ExerciseAction.fromString(exerciseDataRaw.getAction());
		
		Date date = new Date();
		
		try
		{
			if (exerciseDataRaw.getTimestamp() != null)
			{
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				date = formatter.parse(exerciseDataRaw.getTimestamp());
			}
		}
		catch (ParseException pe)
		{
			
		}
		
		exerciseData.setLastAttempt(true);
		
		switch (exerciseAction)
		{
			case EXERCISE_START:
				
				exerciseData.setStartDate(date);
				exerciseData.setGrade(-1);
				
				processExerciseStart(exerciseData);
				
				break;
			
			case EXERCISE_SUBMISSION:
				
				exerciseData.setEndDate(date);
				exerciseData.setGrade(-1);
				
				Outcome outcome = Outcome.ERROR;
				
				try
				{
					outcome = Outcome.fromString(exerciseDataRaw.getOutcome());
				}
				catch (Exception e)
				{
					exerciseData.setOutcomeFixedUp(true);
				}
				
				exerciseData.setOutcome(outcome);
				
				processExerciseSubmission(exerciseData);
				
				break;
			
			case EXERCISE_ASSESSMENT:
				
				exerciseData.setAssessmentDate(date);
				
				double grade = Double.parseDouble(exerciseDataRaw.getGrade());
				
				exerciseData.setGrade(grade);
				exerciseData.setGradeIncorrect(grade < 0 || grade > 10);
				
				processExerciseAssessment(exerciseData);
				
				break;
		}
	}

	private void setupAssignment(ExerciseDataRaw exerciseDataRaw, ExerciseData exerciseData) throws Exception
	{
		// Search the assignment and assign it if exists
		
		if (exerciseDataRaw.getAssignment() != null && exerciseDataRaw.getAssignment().trim().length() > 0)
		{
			Assignment assignment = assignmentRepository.getAssignment(exerciseData.getCourse(), exerciseDataRaw.getAssignment());
			
			// If does not exist, create and assign it
			if (assignment == null)
			{
				assignment = new Assignment();
				assignment.setName(exerciseDataRaw.getAssignment());
				assignment.setCourse(exerciseData.getCourse());
				
				assignmentRepository.createOrUpdateAssignment(assignment);
			}
			
			exerciseData.setAssignment(assignment);
			
			// Add exercise to assignment if not exists
			
			if (!assignment.getExercises().contains(exerciseData.getExercise()))
			{
				assignment.getExercises().add(exerciseData.getExercise());
			}
			
			dbHelper.flush();
		}
	}

	private void setupExercise(ExerciseDataRaw exerciseDataRaw, ExerciseData exerciseData) throws Exception
	{
		// Find exercise for the specified tool or create if it does not exist
		
		Exercise exercise = exerciseRepository.getExercise(exerciseData.getTool(), exerciseDataRaw.getExercise());
		
		if (exercise == null)
		{
			exercise = new Exercise();
			exercise.setName(exerciseDataRaw.getExercise());
			exercise.setTool(exerciseData.getTool());
			
			exerciseRepository.createOrUpdateExercise(exercise);
			
			dbHelper.flush();
		}
		
		exerciseData.setExercise(exercise);
	}

	private void setupCourse(ExerciseData exerciseData) throws Exception
	{
		// Calculate course if possible
		
		if (exerciseData.getSubject() != null && exerciseData.getSemester() != null)
		{
			Course course = courseRepository.getCourse(exerciseData.getSubject(), exerciseData.getSemester());
		
			if (course == null)
			{
				course = fixUpCourse(exerciseData.getSubject(), exerciseData.getSemester());
				
				exerciseData.setCourseFixedUp(true);
			}
				
			exerciseData.setCourse(course);
			
			exerciseData.setCourseGroup(courseGroupRepository.getStudentCourseGroup(course, exerciseData.getStudent()));
		}
	}

	private void setupSemester(ExerciseDataRaw exerciseDataRaw, ExerciseData exerciseData) throws Exception
	{
		// Set semester
		
		String semesterCode = exerciseDataRaw.getSemester();

		Semester sem = null;
		
		if (semesterCode != null)
		{
			sem = semesterRepository.getSemesterByCode(semesterCode);
		}

		if (sem == null)
		{
			exerciseData.setSemesterFixedUp(true);
			
			// Get current semester (if any)
			sem = semesterRepository.getSemesterByDate(new Date());
		}
		
		exerciseData.setSemester(sem);
	}

	private void setupSubject(ExerciseDataRaw exerciseDataRaw, ExerciseData exerciseData) throws Exception
	{
		// Set subject
		
		String subjectCode = exerciseDataRaw.getSubjectCode();
		
		if (subjectCode != null)
		{
			if (exerciseDataRaw.getSubjectCode().trim().equals(""))
			{
				// is fixed up because the specified subject code is empty so set subject = null
				exerciseData.setSubjectFixedUp(true);
			}
			else
			{
				Subject s = subjectRepository.getSubjectByCode(subjectCode);
				
				if (s == null)
				{
					s = fixUpSubject(exerciseData.getProgram(), subjectCode);
					
					exerciseData.setSubjectFixedUp(true);
				}
				
				exerciseData.setSubject(s);
			}
		}
	}

	private void setupProgram(ExerciseDataRaw exerciseDataRaw, ExerciseData exerciseData) throws Exception
	{
		// Set program
		
		if (exerciseDataRaw.getProgramCode() != null)
		{
			Program p = programRepository.getProgramByCode(exerciseDataRaw.getProgramCode());
			
			if (p == null)
			{
				// is fixed up because the specified program code is not valid so set program = null
				exerciseData.setProgramFixedUp(true);
			}
			else
			{
				exerciseData.setProgram(p);
			}
		}
	}

	private void setupUser(ExerciseDataRaw exerciseDataRaw, ExerciseData exerciseData) throws Exception
	{
		// Set user
		User u = getStudent(exerciseDataRaw.getUserId());
		
		if (u == null)
		{
			u = fixUpStudent(exerciseDataRaw.getUserId());
			
			exerciseData.setStudentFixedUp(true);
		}
		
		exerciseData.setStudent(u);
	}

	private void setupTool(ExerciseDataRaw exerciseDataRaw, ExerciseData exerciseData) throws Exception
	{
		// Set tool
		exerciseData.setTool(toolRepository.getToolByCode(exerciseDataRaw.getTool()));
	}

	private void processExerciseStart(ExerciseData exerciseData) throws Exception
	{
		// Get last attempt
		
		ExerciseData lastAttempt = getLastExerciseData(exerciseData.getProgram(), exerciseData.getSubject(), exerciseData.getTool(),
				exerciseData.getSemester(), exerciseData.getStudent(), exerciseData.getAssignment(), exerciseData.getExercise());
		
		if (lastAttempt == null)
		{
			exerciseData.setTimeFromLastAttempt_sec(0);
			exerciseData.setAttemptNumber(1);
		}
		else
		{
			if (lastAttempt.getEndDate() != null)
			{
				exerciseData.setTimeFromLastAttempt_sec((exerciseData.getStartDate().getTime() - lastAttempt.getEndDate().getTime()) / 1000);
			}
			else
			{
				exerciseData.setTimeFromLastAttempt_sec((exerciseData.getStartDate().getTime() - lastAttempt.getStartDate().getTime()) / 1000);
			}
			
			exerciseData.setAttemptNumber(lastAttempt.getAttemptNumber() + 1);
			
			lastAttempt.setLastAttempt(false);
		}
		
		exerciseData.setDuration_sec(0);
		
		dbHelper.addEntity(exerciseData);
		
		dbHelper.flush();
	}
	
	private void processExerciseSubmission(ExerciseData exerciseData) throws Exception
	{
		// Get last attempt
		
		ExerciseData lastAttempt = getLastExerciseData(exerciseData.getProgram(), exerciseData.getSubject(), exerciseData.getTool(),
				exerciseData.getSemester(), exerciseData.getStudent(), exerciseData.getAssignment(), exerciseData.getExercise());
		
		if (lastAttempt == null || lastAttempt.getEndDate() != null)
		{
			exerciseData.setStartDate(exerciseData.getEndDate());
			exerciseData.setStartDateFixedUp(true);
			exerciseData.setEndDate(exerciseData.getEndDate());
			
			if (lastAttempt == null)
			{
				exerciseData.setTimeFromLastAttempt_sec(0);
				exerciseData.setAttemptNumber(1);
			}
			else
			{
				exerciseData.setTimeFromLastAttempt_sec((exerciseData.getStartDate().getTime() - lastAttempt.getEndDate().getTime()) / 1000);
				exerciseData.setAttemptNumber(lastAttempt.getAttemptNumber() + 1);
				
				lastAttempt.setLastAttempt(false);
			}
			
			exerciseData.setDuration_sec(0);
			
			lastAttempt = exerciseData;
			
			// The row is new so it must be created in database
			dbHelper.addEntity(lastAttempt);
		}
		else
		{
			lastAttempt.setEndDate(exerciseData.getEndDate());
			
			lastAttempt.setDuration_sec((lastAttempt.getEndDate().getTime() - lastAttempt.getStartDate().getTime()) / 1000);
		}
		
		lastAttempt.setOutcome(exerciseData.getOutcome());
		lastAttempt.setOutcomeFixedUp(exerciseData.isOutcomeFixedUp());
		
		dbHelper.flush();
	}

	private void processExerciseAssessment(ExerciseData exerciseData) throws Exception
	{
		// Get last attempt
		
		ExerciseData lastAttempt = getLastExerciseData(exerciseData.getProgram(), exerciseData.getSubject(), exerciseData.getTool(),
				exerciseData.getSemester(), exerciseData.getStudent(), exerciseData.getAssignment(), exerciseData.getExercise());
		
		if (lastAttempt == null || !lastAttempt.isGradeNull())
		{
			exerciseData.setStartDate(exerciseData.getAssessmentDate());
			exerciseData.setStartDateFixedUp(true);
			exerciseData.setEndDate(exerciseData.getAssessmentDate());
			exerciseData.setEndDateFixedUp(true);
			
			if (lastAttempt == null)
			{
				exerciseData.setTimeFromLastAttempt_sec(0);
				exerciseData.setAttemptNumber(1);
			}
			else
			{
				exerciseData.setTimeFromLastAttempt_sec((exerciseData.getStartDate().getTime() - lastAttempt.getEndDate().getTime()) / 1000);
				exerciseData.setAttemptNumber(lastAttempt.getAttemptNumber() + 1);
				
				lastAttempt.setLastAttempt(false);
			}
			
			exerciseData.setDuration_sec(0);
			
			lastAttempt = exerciseData;
			
			// The row is new so it must be created in database
			dbHelper.addEntity(lastAttempt);
		}
		else
		{
			if (lastAttempt.getEndDate() == null)
			{
				lastAttempt.setEndDate(exerciseData.getAssessmentDate());
				lastAttempt.setEndDateFixedUp(true);
			}
			
			lastAttempt.setDuration_sec((lastAttempt.getEndDate().getTime() - lastAttempt.getStartDate().getTime()) / 1000);
		}
		
		lastAttempt.setAssessmentDate(exerciseData.getAssessmentDate());
		lastAttempt.setGrade(exerciseData.getGrade());
		lastAttempt.setGradeIncorrect(exerciseData.isGradeIncorrect());
		
		dbHelper.flush();
	}
	
	private ExerciseData getLastExerciseData(Program program, Subject subject, Tool tool, Semester semester, User student, Assignment assignment,
			Exercise exercise) throws Exception
	{
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		StringBuilder sb = new StringBuilder("SELECT d FROM ExerciseData d WHERE ");
		
		addParameterSafely(sb, parameters, "program", program, true);
		addParameterSafely(sb, parameters, "subject", subject, true);
		addParameterSafely(sb, parameters, "tool", tool, true);
		addParameterSafely(sb, parameters, "semester", semester, true);
		addParameterSafely(sb, parameters, "student", student, true);
		addParameterSafely(sb, parameters, "assignment", assignment, true);
		addParameterSafely(sb, parameters, "exercise", exercise, false);
	
		sb.append(" AND d.lastAttempt = true");
		//sb.append("ORDER BY d.startDate DESC");
		
		return dbHelper.getFirst(sb.toString(), ExerciseData.class, parameters, false);
	}
}
