package edu.uoc.ictflag.ela.dal.test;

import static edu.uoc.ictflag.setup.core.DbSetupHelper.commitTransaction;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.createDataRawTables;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.getEntityManager;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.setupCourse;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.setupCourseGroup;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.setupCourseGroupMember;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.setupProgram;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.setupSemester;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.setupSubject;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.setupTool;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.setupUser;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.startTransaction;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import edu.uoc.ictflag.core.dal.DBHelper;
import edu.uoc.ictflag.core.localization.LocalizedString;
import edu.uoc.ictflag.dal.test.DBTestHelper;
import edu.uoc.ictflag.ela.dal.AssignmentRepository;
import edu.uoc.ictflag.ela.dal.ExerciseDataRepository;
import edu.uoc.ictflag.ela.dal.ExerciseRepository;
import edu.uoc.ictflag.ela.dal.ReportsEtlRepository;
import edu.uoc.ictflag.ela.dal.ToolRepository;
import edu.uoc.ictflag.ela.model.ExerciseData;
import edu.uoc.ictflag.ela.model.ExerciseDataRaw;
import edu.uoc.ictflag.ela.model.Outcome;
import edu.uoc.ictflag.institution.dal.CourseGroupRepository;
import edu.uoc.ictflag.institution.dal.CourseRepository;
import edu.uoc.ictflag.institution.dal.ProgramRepository;
import edu.uoc.ictflag.institution.dal.SemesterRepository;
import edu.uoc.ictflag.institution.dal.SubjectRepository;
import edu.uoc.ictflag.institution.model.Course;
import edu.uoc.ictflag.institution.model.CourseGroup;
import edu.uoc.ictflag.institution.model.Program;
import edu.uoc.ictflag.institution.model.Semester;
import edu.uoc.ictflag.institution.model.Subject;
import edu.uoc.ictflag.security.dal.UserRepository;
import edu.uoc.ictflag.security.model.User;
import edu.uoc.ictflag.security.model.UserRole;
import edu.uoc.ictflag.setup.core.DbSetupHelper;

public class ExerciseDataRepositoryTest
{
	private static ToolRepository toolRepository;
	private static UserRepository userRepository;
	private static ProgramRepository programRepository;
	private static SubjectRepository subjectRepository;
	private static SemesterRepository semesterRepository;
	private static CourseRepository courseRepository;
	private static CourseGroupRepository courseGroupRepository;
	private static AssignmentRepository assignmentRepository;
	private static ExerciseRepository exerciseRepository;
	
	private static ExerciseDataRepository exerciseDataRepository;
	
	private static ReportsEtlRepository reportsEtlRepository;
	
	private static Program program;
	private static Semester semester;
	private static User std;
	
	private static Subject subject;
	
	private static Course course;
	private static CourseGroup courseGroup;
	
	@Before
	public void setUpExerciseDataRepositoryTest() throws Exception
	{
		DBTestHelper.reset();
		
		userRepository = new UserRepository();
		userRepository.setEntityManager(getEntityManager());
		
		toolRepository = new ToolRepository(userRepository);
		toolRepository.setEntityManager(getEntityManager());
		
		programRepository = new ProgramRepository(userRepository);
		programRepository.setEntityManager(getEntityManager());
		
		subjectRepository = new SubjectRepository(userRepository);
		subjectRepository.setEntityManager(getEntityManager());
		
		semesterRepository = new SemesterRepository(userRepository);
		semesterRepository.setEntityManager(getEntityManager());
		
		courseRepository = new CourseRepository(userRepository);
		courseRepository.setEntityManager(getEntityManager());
		
		courseGroupRepository = new CourseGroupRepository(userRepository);
		courseGroupRepository.setEntityManager(getEntityManager());
		
		assignmentRepository = new AssignmentRepository(userRepository);
		assignmentRepository.setEntityManager(getEntityManager());
		
		exerciseRepository = new ExerciseRepository(userRepository);
		exerciseRepository.setEntityManager(getEntityManager());
		
		exerciseDataRepository = new ExerciseDataRepository(toolRepository, userRepository, programRepository, subjectRepository, semesterRepository,
				courseRepository, courseGroupRepository, assignmentRepository, exerciseRepository);
		exerciseDataRepository.setEntityManager(getEntityManager());
		
		reportsEtlRepository = new ReportsEtlRepository(exerciseDataRepository, null);
		
		startTransaction();
		
		createDataRawTables();
		
		program = setupProgram("P1", LocalizedString.fromStringFormat("en#Program1"), null);
		
		semester = setupSemester(2015, 2, 2016, 3, 15, 2016, 7, 15);

		std = setupUser("std", "Student", "std", "std@uoc.edu", Arrays.asList(UserRole.STUDENT));
		
		subject = setupSubject(LocalizedString.fromStringFormat("en#Subject1"), "Subject 1");
		course = setupCourse(LocalizedString.fromStringFormat("en#Course1"), "Course 1", semester, "CA", subject, null);
		courseGroup = setupCourseGroup(LocalizedString.fromStringFormat("en#CourseGroup 1"), course, null);
		
		courseGroup.getMembers().add(setupCourseGroupMember(std));
		
		setupTool(LocalizedString.fromStringFormat("ca#Fake FAT"), "FAKEFAT", "afe9ead0-8a55-4bf7-bce9-f78e81afdecb");
		
		commitTransaction();
	}
	
	@Test
	public void processExerciseDataRawFixUpsTest() throws Exception
	{
		ExerciseDataRaw exerciseDataRaw = new ExerciseDataRaw();
		
		ExerciseData exerciseData;
		
		exerciseDataRaw.setTool("FAKEFAT");
		exerciseDataRaw.setToolUUID("afe9ead0-8a55-4bf7-bce9-f78e81afdecb");
		exerciseDataRaw.setUserId("std");
		

		// TEST 1 
		
		// PROGRAM NULL 				==> NULL
		// SUBJECT NULL 				==> NULL
		// GRADE NULL (NO ASSESSMENT) 	==> NULL
		// ASSIGNMENT NULL 				==> NULL
		// DATETIME NULL 				==> NOW
		// SEMESTER NULL 				==> CURRENT / NULL
		
		exerciseDataRaw.setAction("ES");
		exerciseDataRaw.setExercise("exercise1");
		
		DbSetupHelper.startTransaction();
		
		DbSetupHelper.emptyDataRawTables();
		
		exerciseDataRepository.saveExerciseDataRaw(exerciseDataRaw);
		
		reportsEtlRepository.exerciseDataEtlProcess();
		
		exerciseData = getLastExerciseData();
		
		DbSetupHelper.commitTransaction();
		
		// VALIDATE FIX-UPS
		
		assertEquals(std.getId(), exerciseData.getStudent().getId());
		assertFalse(exerciseData.isStudentFixedUp());
		assertNull(exerciseData.getProgram());
		assertFalse(exerciseData.isProgramFixedUp());
		assertNull(exerciseData.getSubject());
		assertFalse(exerciseData.isSubjectFixedUp());
		assertTrue(exerciseData.isGradeNull());
		assertFalse(exerciseData.isGradeIncorrect());
		assertNull(exerciseData.getAssignment());
		assertEquals(semester.getCode(), exerciseData.getSemester().getCode());
		assertTrue(exerciseData.isSemesterFixedUp());
		
		Date startDate = exerciseData.getStartDate();
		
		assertTrue(new Date().getTime() - startDate.getTime() < 100000);
		
		// TEST 2 
		
		// PROGRAM EMPTY 				==> NULL
		// SUBJECT EMPTY 				==> NULL
		// GRADE EMPTY (NO ASSESSMENT) 	==> NULL
		// ASSIGNMENT EMPTY 			==> NULL
		// DATETIME EMPTY 				==> NOW
		// SEMESTER EMPTY 				==> CURRENT / NULL
		
		exerciseDataRaw.setProgramCode("");
		exerciseDataRaw.setSubjectCode("");
		exerciseDataRaw.setGrade("");
		exerciseDataRaw.setAssignment("");
		exerciseDataRaw.setTimestamp("");
		exerciseDataRaw.setSemester("");
		
		DbSetupHelper.startTransaction();
		
		DbSetupHelper.emptyDataRawTables();
		
		exerciseDataRepository.saveExerciseDataRaw(exerciseDataRaw);
		
		reportsEtlRepository.exerciseDataEtlProcess();
		
		exerciseData = getLastExerciseData();
		
		DbSetupHelper.commitTransaction();
		
		// VALIDATE FIX-UPS
		
		assertEquals(std.getId(), exerciseData.getStudent().getId());
		assertFalse(exerciseData.isStudentFixedUp());
		assertNull(exerciseData.getProgram());
		assertTrue(exerciseData.isProgramFixedUp());
		assertNull(exerciseData.getSubject());
		assertTrue(exerciseData.isSubjectFixedUp());
		assertTrue(exerciseData.isGradeNull());
		assertFalse(exerciseData.isGradeIncorrect());
		assertNull(exerciseData.getAssignment());
		assertEquals(semester.getCode(), exerciseData.getSemester().getCode());
		assertTrue(exerciseData.isSemesterFixedUp());
		
		startDate = exerciseData.getStartDate();
		
		assertTrue(new Date().getTime() - startDate.getTime() < 100000);
		
		// TEST 3
		
		// PROGRAM NOT EXISTS 			==> NULL
		// SUBJECT NOT EXISTS 			==> CREATES IT
		// USER NOT EXISTS 				==> CREATES IT
		// ASSIGNMENT NULL 				==> NULL
		// SEMESTER NOT VALID			==> CURRENT / NULL
		
		// VALIDATE FIX-UPS
		// COURSE AND COURSEGROUP ARE NULL
		
		exerciseDataRaw.setProgramCode("P");
		exerciseDataRaw.setSubjectCode("S");
		exerciseDataRaw.setUserId("nostd");
		exerciseDataRaw.setAssignment(null);
		exerciseDataRaw.setSemester("2015--2");
		
		DbSetupHelper.startTransaction();
		
		DbSetupHelper.emptyDataRawTables();
		
		exerciseDataRepository.saveExerciseDataRaw(exerciseDataRaw);
		
		reportsEtlRepository.exerciseDataEtlProcess();
				
		exerciseData = getLastExerciseData();
		
		DbSetupHelper.commitTransaction();
		
		// VALIDATE FIX-UPS
				
		assertNotNull(userRepository.getUserByUsername("nostd"));
		assertTrue(exerciseData.isStudentFixedUp());
		assertNull(exerciseData.getProgram());
		assertTrue(exerciseData.isProgramFixedUp());
		assertNotNull(subjectRepository.getSubjectByCode("S"));
		assertTrue(exerciseData.isSubjectFixedUp());
		assertTrue(exerciseData.isGradeNull());
		assertFalse(exerciseData.isGradeIncorrect());
		assertNull(exerciseData.getAssignment());
		assertEquals(semester.getCode(), exerciseData.getSemester().getCode());
		assertTrue(exerciseData.isSemesterFixedUp());
		assertNull(exerciseData.getCourse());
		assertNull(exerciseData.getCourseGroup());
				
		startDate = exerciseData.getStartDate();
				
		assertTrue(new Date().getTime() - startDate.getTime() < 100000);
		
		// TEST 4
		
		// EXERCISEACTION SUBMISSION
		// OUTCOME NULL					==> ERROR
		
		exerciseDataRaw.setAction("EU");
		
		// reset fixedup flags
		exerciseData.setStudentFixedUp(false);
		exerciseData.setSubjectFixedUp(false);
		
		DbSetupHelper.startTransaction();
		
		DbSetupHelper.emptyDataRawTables();
		
		exerciseDataRepository.saveExerciseDataRaw(exerciseDataRaw);
		
		reportsEtlRepository.exerciseDataEtlProcess();
		
		exerciseData = getLastExerciseData();
		
		DbSetupHelper.commitTransaction();
		
		// VALIDATE FIX-UPS
		
		assertFalse(exerciseData.isStudentFixedUp());
		assertFalse(exerciseData.isSubjectFixedUp());
		assertTrue(exerciseData.isOutcomeFixedUp());
		assertEquals(Outcome.ERROR, exerciseData.getOutcome());
		
		Date endDate = exerciseData.getEndDate();
		
		assertTrue(new Date().getTime() - endDate.getTime() < 100000);

		// TEST 5
		
		// EXERCISEACTION SUBMISSION
		// OUTCOME EMPTY				==> ERROR
		
		exerciseDataRaw.setOutcome("");
		
		DbSetupHelper.startTransaction();
		
		DbSetupHelper.emptyDataRawTables();
		
		exerciseDataRepository.saveExerciseDataRaw(exerciseDataRaw);
		
		reportsEtlRepository.exerciseDataEtlProcess();
		
		exerciseData = getLastExerciseData();
		
		DbSetupHelper.commitTransaction();
		
		// VALIDATE FIX-UPS
		
		assertFalse(exerciseData.isStudentFixedUp());
		assertFalse(exerciseData.isSubjectFixedUp());
		assertTrue(exerciseData.isOutcomeFixedUp());
		assertEquals(Outcome.ERROR, exerciseData.getOutcome());
		
		endDate = exerciseData.getEndDate();
		
		assertTrue(new Date().getTime() - endDate.getTime() < 100000);
		
		// TEST 6
		
		// EXERCISEACTION ASSESSMENT
		// GRADE OUTSIDE RANGE 0..1		==> NO CHANGE
		
		exerciseDataRaw.setAction("EA");
		exerciseDataRaw.setGrade("12");
		
		DbSetupHelper.startTransaction();
		
		DbSetupHelper.emptyDataRawTables();
		
		exerciseDataRepository.saveExerciseDataRaw(exerciseDataRaw);
		
		reportsEtlRepository.exerciseDataEtlProcess();
		
		exerciseData = getLastExerciseData();
		
		DbSetupHelper.commitTransaction();
		
		// VALIDATE FIX-UPS
		
		assertFalse(exerciseData.isStudentFixedUp());
		assertFalse(exerciseData.isSubjectFixedUp());
		assertTrue(exerciseData.isOutcomeFixedUp());
		assertEquals(Outcome.ERROR, exerciseData.getOutcome());
		assertTrue(exerciseData.isGradeIncorrect());
		
		endDate = exerciseData.getEndDate();
		
		assertTrue(new Date().getTime() - endDate.getTime() < 100000);
		
		// TEST 7
		
		// SUBMISSION ALL OK (COURSE AND COURSEGROUP CALCULABLE)
		
		// reset fixedup flags
		exerciseData.setGradeIncorrect(false);
		
		exerciseDataRaw.setAction("EU");
		exerciseDataRaw.setOutcome(Outcome.RIGHT.toString());
		exerciseDataRaw.setUserId("std");
		exerciseDataRaw.setSemester("2015-2");
		
		DbSetupHelper.startTransaction();
		
		DbSetupHelper.emptyDataRawTables();
		
		exerciseDataRepository.saveExerciseDataRaw(exerciseDataRaw);
		
		reportsEtlRepository.exerciseDataEtlProcess();
		
		exerciseData = getLastExerciseData();
		
		DbSetupHelper.commitTransaction();
		
		// VALIDATE FIX-UPS
		
		assertFalse(exerciseData.isStudentFixedUp());
		assertFalse(exerciseData.isSubjectFixedUp());
		assertFalse(exerciseData.isOutcomeFixedUp());
		assertEquals(Outcome.RIGHT, exerciseData.getOutcome());
		assertNull(exerciseData.getCourse());
		assertNull(exerciseData.getCourseGroup());
		assertEquals(semester.getCode(), exerciseData.getSemester().getCode());
		assertFalse(exerciseData.isSemesterFixedUp());
		
		endDate = exerciseData.getEndDate();
		
		assertTrue(new Date().getTime() - endDate.getTime() < 100000);
		
		// TEST 8
		
		// ASSESSMENT ALL OK (COURSE AND COURSEGROUP CALCULABLE)
		
		exerciseDataRaw.setAction("EA");
		exerciseDataRaw.setGrade("5");
		exerciseDataRaw.setSubjectCode("Subject 1");
		exerciseDataRaw.setSemester("20152");
		
		DbSetupHelper.startTransaction();
		
		DbSetupHelper.emptyDataRawTables();
		
		exerciseDataRepository.saveExerciseDataRaw(exerciseDataRaw);
		
		reportsEtlRepository.exerciseDataEtlProcess();
		
		exerciseData = getLastExerciseData();
		
		DbSetupHelper.commitTransaction();
		
		// VALIDATE FIX-UPS
		
		assertFalse(exerciseData.isGradeIncorrect());
		
		assertEquals(course.getId(), exerciseData.getCourse().getId());
		assertEquals(courseGroup.getId(), exerciseData.getCourseGroup().getId());
		assertEquals(semester.getCode(), exerciseData.getSemester().getCode());
		assertFalse(exerciseData.isSemesterFixedUp());
		
		endDate = exerciseData.getEndDate();
		
		assertTrue(new Date().getTime() - endDate.getTime() < 100000);
		
		// TEST 9
		
		// PROGRAM EXISTS BUT SUBJECT NOT
		
		exerciseDataRaw.setProgramCode("P1");
		exerciseDataRaw.setSubjectCode("Subject 2");
		
		DbSetupHelper.startTransaction();
		
		DbSetupHelper.emptyDataRawTables();
		
		exerciseDataRepository.saveExerciseDataRaw(exerciseDataRaw);
		
		reportsEtlRepository.exerciseDataEtlProcess();
		
		exerciseData = getLastExerciseData();
		
		DbSetupHelper.commitTransaction();
		
		// VALIDATE FIX-UPS
		
		assertEquals(program.getId(), exerciseData.getProgram().getId());
		assertEquals("Subject 2", exerciseData.getSubject().getCode());
		assertEquals(((Subject) program.getSubjects().toArray()[0]).getId(), exerciseData.getSubject().getId());
		
		endDate = exerciseData.getEndDate();
		
		assertTrue(new Date().getTime() - endDate.getTime() < 100000);
	}
	
	@Test
	public void processExerciseDataRawETLProcessTest() throws Exception
	{
		ExerciseDataRaw exerciseDataRaw = new ExerciseDataRaw();
		
		ExerciseData exerciseData;
		
		exerciseDataRaw.setTool("FAKEFAT");
		exerciseDataRaw.setToolUUID("afe9ead0-8a55-4bf7-bce9-f78e81afdecb");
		
		// TEST 1 
		
		// START - SUBMISSION - ASSESSMENT
		
		// TEST 1.1 START  
		
		exerciseDataRaw.setUserId("std 1");
		exerciseDataRaw.setAction("ES");
		exerciseDataRaw.setExercise("exercise1");
		
		DbSetupHelper.startTransaction();
		
		DbSetupHelper.emptyDataRawTables();
		
		exerciseDataRepository.saveExerciseDataRaw(exerciseDataRaw);
		
		reportsEtlRepository.exerciseDataEtlProcess();
		
		exerciseData = getLastExerciseData();
		
		DbSetupHelper.commitTransaction();
		
		// VALIDATE FIX-UPS
		
		Date startDate = exerciseData.getStartDate();
		
		assertTrue(new Date().getTime() - startDate.getTime() < 100000);
		
		assertNull(exerciseData.getEndDate());
		assertEquals(0, exerciseData.getTimeFromLastAttempt_sec());
		assertEquals(1, exerciseData.getAttemptNumber());
		assertEquals(-1, exerciseData.getDuration_sec());
		
		Thread.sleep(1000);
		
		// TEST 1.2 SUBMISSION  
		
		DbSetupHelper.startTransaction();
		
		exerciseDataRaw.setAction("EU");
		exerciseDataRaw.setOutcome("R");
		
		exerciseDataRepository.saveExerciseDataRaw(exerciseDataRaw);
		
		reportsEtlRepository.exerciseDataEtlProcess();
		
		exerciseData = getLastExerciseData();
		
		DbSetupHelper.commitTransaction();
		
		// VALIDATE FIX-UPS
		
		startDate = exerciseData.getStartDate();
		
		assertTrue(new Date().getTime() - startDate.getTime() < 100000);
		
		Date endDate = exerciseData.getEndDate();
		
		assertTrue(new Date().getTime() - endDate.getTime() < 100000);
		
		assertEquals(0, exerciseData.getTimeFromLastAttempt_sec());
		assertEquals(1, exerciseData.getAttemptNumber());
		assertTrue(exerciseData.getDuration_sec() > 0);
		
		// TEST 1.3 ASSESSMENT  
		
		DbSetupHelper.startTransaction();
		
		exerciseDataRaw.setAction("EA");
		exerciseDataRaw.setGrade("9");
		
		exerciseDataRepository.saveExerciseDataRaw(exerciseDataRaw);
		
		reportsEtlRepository.exerciseDataEtlProcess();
		
		exerciseData = getLastExerciseData();
		
		DbSetupHelper.commitTransaction();
		
		// VALIDATE FIX-UPS
		
		startDate = exerciseData.getStartDate();
		
		assertTrue(new Date().getTime() - startDate.getTime() < 100000);
		
		endDate = exerciseData.getEndDate();
		
		assertTrue(new Date().getTime() - endDate.getTime() < 100000);
		
		assertEquals(0, exerciseData.getTimeFromLastAttempt_sec());
		assertEquals(1, exerciseData.getAttemptNumber());
		assertTrue(exerciseData.getDuration_sec() > 0);
		
		Thread.sleep(1000);
		
		// TEST 2 
		
		// SUBMISSION - ASSESSMENT
		
		// TEST 2.1 SUBMISSION  
		
		DbSetupHelper.startTransaction();
		
		exerciseDataRaw.setAction("EU");
		exerciseDataRaw.setOutcome("R");
		
		exerciseDataRepository.saveExerciseDataRaw(exerciseDataRaw);
		
		reportsEtlRepository.exerciseDataEtlProcess();
		
		exerciseData = getLastExerciseData();
		
		DbSetupHelper.commitTransaction();
		
		// VALIDATE FIX-UPS
		
		startDate = exerciseData.getStartDate();
		
		assertTrue(new Date().getTime() - startDate.getTime() < 100000);
		
		assertEquals(exerciseData.getStartDate(), exerciseData.getEndDate());
		
		assertTrue(exerciseData.getTimeFromLastAttempt_sec() > 0);
		assertEquals(2, exerciseData.getAttemptNumber());
		assertTrue(exerciseData.getDuration_sec() == 0);
		
		assertTrue(exerciseData.isStartDateFixedUp());
		
		// TEST 2.2 ASSESSMENT  
		
		DbSetupHelper.startTransaction();
		
		exerciseDataRaw.setAction("EA");
		exerciseDataRaw.setGrade("5");
		
		exerciseDataRepository.saveExerciseDataRaw(exerciseDataRaw);
		
		reportsEtlRepository.exerciseDataEtlProcess();
		
		exerciseData = getLastExerciseData();
		
		DbSetupHelper.commitTransaction();
		
		// VALIDATE FIX-UPS
		
		startDate = exerciseData.getStartDate();
		
		assertTrue(new Date().getTime() - startDate.getTime() < 100000);
		
		endDate = exerciseData.getEndDate();
		
		assertTrue(new Date().getTime() - endDate.getTime() < 100000);
		
		assertTrue(exerciseData.getTimeFromLastAttempt_sec() > 0);
		assertEquals(2, exerciseData.getAttemptNumber());
		assertTrue(exerciseData.getDuration_sec() == 0);
		
		Thread.sleep(1000);
		
		// TEST 3 
		
		// ASSESSMENT
		
		// TEST 3.1 ASSESSMENT  
		
		DbSetupHelper.startTransaction();
		
		exerciseDataRaw.setAction("EA");
		exerciseDataRaw.setGrade("5");
		
		exerciseDataRepository.saveExerciseDataRaw(exerciseDataRaw);
		
		reportsEtlRepository.exerciseDataEtlProcess();
		
		exerciseData = getLastExerciseData();
		
		DbSetupHelper.commitTransaction();
		
		// VALIDATE FIX-UPS
		
		startDate = exerciseData.getStartDate();
		
		assertTrue(new Date().getTime() - startDate.getTime() < 100000);
		
		endDate = exerciseData.getEndDate();
		
		assertTrue(new Date().getTime() - endDate.getTime() < 100000);
		
		assertTrue(exerciseData.getTimeFromLastAttempt_sec() > 0);
		assertEquals(3, exerciseData.getAttemptNumber());
		assertTrue(exerciseData.getDuration_sec() == 0);
	}
	
	private ExerciseData getLastExerciseData() throws Exception
	{
		return new DBHelper(getEntityManager()).getFirst("SELECT s FROM ExerciseData s ORDER BY s.startDate DESC", ExerciseData.class);
	}
}
