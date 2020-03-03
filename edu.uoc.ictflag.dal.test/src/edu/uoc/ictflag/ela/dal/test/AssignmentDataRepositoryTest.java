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
import edu.uoc.ictflag.ela.dal.AssignmentDataRepository;
import edu.uoc.ictflag.ela.dal.AssignmentRepository;
import edu.uoc.ictflag.ela.dal.ReportsEtlRepository;
import edu.uoc.ictflag.ela.dal.ToolRepository;
import edu.uoc.ictflag.ela.model.AssignmentData;
import edu.uoc.ictflag.ela.model.AssignmentDataRaw;
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

public class AssignmentDataRepositoryTest
{
	private static ToolRepository toolRepository;
	private static UserRepository userRepository;
	private static ProgramRepository programRepository;
	private static SubjectRepository subjectRepository;
	private static SemesterRepository semesterRepository;
	private static CourseRepository courseRepository;
	private static CourseGroupRepository courseGroupRepository;
	private static AssignmentRepository assignmentRepository;
	
	private static AssignmentDataRepository assignmentDataRepository;
	
	private static ReportsEtlRepository reportsEtlRepository;
	
	private static Program program;
	private static Semester semester;
	private static User std;
	
	private static Subject subject;
	
	private static Course course;
	private static CourseGroup courseGroup;
	
	@Before
	public void setUpAssignmentDataRepositoryTest() throws Exception
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
		
		assignmentDataRepository = new AssignmentDataRepository(toolRepository, userRepository, programRepository, subjectRepository,
				semesterRepository,
				courseRepository, courseGroupRepository, assignmentRepository);
		assignmentDataRepository.setEntityManager(getEntityManager());
		
		reportsEtlRepository = new ReportsEtlRepository(null, assignmentDataRepository);
		reportsEtlRepository.setEntityManager(getEntityManager());
		
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
	public void processAssignmentDataRawFixUpsTest() throws Exception
	{
		AssignmentDataRaw assignmentDataRaw = new AssignmentDataRaw();
		
		AssignmentData assignmentData;
		
		assignmentDataRaw.setTool("FAKEFAT");
		assignmentDataRaw.setToolUUID("afe9ead0-8a55-4bf7-bce9-f78e81afdecb");
		assignmentDataRaw.setUserId("std");
		

		// TEST 1 
		
		// PROGRAM NULL 				==> NULL
		// SUBJECT NULL 				==> NULL
		// GRADE NULL (NO ASSESSMENT) 	==> NULL
		// DATETIME NULL 				==> NOW
		// SEMESTER NULL 				==> CURRENT / NULL
		
		assignmentDataRaw.setAction("AS");
		assignmentDataRaw.setAssignment("assignment1");
		
		DbSetupHelper.startTransaction();
		
		DbSetupHelper.emptyDataRawTables();
		
		assignmentDataRepository.saveAssignmentDataRaw(assignmentDataRaw);
		
		reportsEtlRepository.assignmentDataEtlProcess();
		
		assignmentData = getLastAssignmentData();
		
		DbSetupHelper.commitTransaction();
		
		// VALIDATE FIX-UPS
		
		assertEquals(std.getId(), assignmentData.getStudent().getId());
		assertFalse(assignmentData.isStudentFixedUp());
		assertNull(assignmentData.getProgram());
		assertFalse(assignmentData.isProgramFixedUp());
		assertNull(assignmentData.getSubject());
		assertFalse(assignmentData.isSubjectFixedUp());
		assertTrue(assignmentData.isGradeNull());
		assertFalse(assignmentData.isGradeIncorrect());
		assertEquals("assignment1", assignmentData.getAssignment());
		assertEquals(semester.getCode(), assignmentData.getSemester().getCode());
		assertTrue(assignmentData.isSemesterFixedUp());
		
		Date startDate = assignmentData.getStartDate();
		
		assertTrue(new Date().getTime() - startDate.getTime() < 100000);
		
		// TEST 2 
		
		// PROGRAM EMPTY 				==> NULL
		// SUBJECT EMPTY 				==> NULL
		// GRADE EMPTY (NO ASSESSMENT) 	==> NULL
		// DATETIME EMPTY 				==> NOW
		// SEMESTER EMPTY 				==> CURRENT / NULL
		
		assignmentDataRaw.setProgramCode("");
		assignmentDataRaw.setSubjectCode("");
		assignmentDataRaw.setGrade("");
		assignmentDataRaw.setTimestamp("");
		assignmentDataRaw.setSemester("");
		
		DbSetupHelper.startTransaction();
		
		DbSetupHelper.emptyDataRawTables();
		
		assignmentDataRepository.saveAssignmentDataRaw(assignmentDataRaw);
		
		reportsEtlRepository.assignmentDataEtlProcess();
		
		assignmentData = getLastAssignmentData();
		
		DbSetupHelper.commitTransaction();
		
		// VALIDATE FIX-UPS
		
		assertEquals(std.getId(), assignmentData.getStudent().getId());
		assertFalse(assignmentData.isStudentFixedUp());
		assertNull(assignmentData.getProgram());
		assertTrue(assignmentData.isProgramFixedUp());
		assertNull(assignmentData.getSubject());
		assertTrue(assignmentData.isSubjectFixedUp());
		assertTrue(assignmentData.isGradeNull());
		assertFalse(assignmentData.isGradeIncorrect());
		assertEquals("assignment1", assignmentData.getAssignment());
		assertEquals(semester.getCode(), assignmentData.getSemester().getCode());
		assertTrue(assignmentData.isSemesterFixedUp());
		
		startDate = assignmentData.getStartDate();
		
		assertTrue(new Date().getTime() - startDate.getTime() < 100000);
		
		// TEST 3
		
		// PROGRAM NOT EXISTS 			==> NULL
		// SUBJECT NOT EXISTS 			==> CREATES IT
		// USER NOT EXISTS 				==> CREATES IT
		// ASSIGNMENT NULL 				==> NULL
		// SEMESTER NOT VALID			==> CURRENT / NULL
		
		// VALIDATE FIX-UPS
		// COURSE AND COURSEGROUP ARE NULL
		
		assignmentDataRaw.setProgramCode("P");
		assignmentDataRaw.setSubjectCode("S");
		assignmentDataRaw.setUserId("nostd");
		assignmentDataRaw.setAssignment(null);
		assignmentDataRaw.setSemester("2015--2");
		
		DbSetupHelper.startTransaction();
		
		DbSetupHelper.emptyDataRawTables();
		
		assignmentDataRepository.saveAssignmentDataRaw(assignmentDataRaw);
		
		reportsEtlRepository.assignmentDataEtlProcess();
				
		assignmentData = getLastAssignmentData();
		
		DbSetupHelper.commitTransaction();
		
		// VALIDATE FIX-UPS
				
		assertNotNull(userRepository.getUserByUsername("nostd"));
		assertTrue(assignmentData.isStudentFixedUp());
		assertNull(assignmentData.getProgram());
		assertTrue(assignmentData.isProgramFixedUp());
		assertNotNull(subjectRepository.getSubjectByCode("S"));
		assertTrue(assignmentData.isSubjectFixedUp());
		assertTrue(assignmentData.isGradeNull());
		assertFalse(assignmentData.isGradeIncorrect());
		assertNull(assignmentData.getAssignment());
		assertEquals(semester.getCode(), assignmentData.getSemester().getCode());
		assertTrue(assignmentData.isSemesterFixedUp());
		assertNull(assignmentData.getCourse());
		assertNull(assignmentData.getCourseGroup());
				
		startDate = assignmentData.getStartDate();
				
		assertTrue(new Date().getTime() - startDate.getTime() < 100000);
		
		// TEST 4
		
		// EXERCISEACTION END
		
		assignmentDataRaw.setAction("AE");
		
		// reset fixedup flags
		assignmentData.setStudentFixedUp(false);
		assignmentData.setSubjectFixedUp(false);
		
		DbSetupHelper.startTransaction();
		
		DbSetupHelper.emptyDataRawTables();
		
		assignmentDataRepository.saveAssignmentDataRaw(assignmentDataRaw);
		
		reportsEtlRepository.assignmentDataEtlProcess();
		
		assignmentData = getLastAssignmentData();
		
		DbSetupHelper.commitTransaction();
		
		// VALIDATE FIX-UPS
		
		assertFalse(assignmentData.isStudentFixedUp());
		assertFalse(assignmentData.isSubjectFixedUp());
		
		Date endDate = assignmentData.getEndDate();
		
		assertTrue(new Date().getTime() - endDate.getTime() < 100000);

		// TEST 6
		
		// EXERCISEACTION ASSESSMENT
		// GRADE OUTSIDE RANGE 0..1		==> NO CHANGE
		
		assignmentDataRaw.setAction("AA");
		assignmentDataRaw.setGrade("12");
		
		DbSetupHelper.startTransaction();
		
		DbSetupHelper.emptyDataRawTables();
		
		assignmentDataRepository.saveAssignmentDataRaw(assignmentDataRaw);
		
		reportsEtlRepository.assignmentDataEtlProcess();
		
		assignmentData = getLastAssignmentData();
		
		DbSetupHelper.commitTransaction();
		
		// VALIDATE FIX-UPS
		
		assertFalse(assignmentData.isStudentFixedUp());
		assertFalse(assignmentData.isSubjectFixedUp());
		assertTrue(assignmentData.isGradeIncorrect());
		
		endDate = assignmentData.getEndDate();
		
		assertTrue(new Date().getTime() - endDate.getTime() < 100000);
		
		// TEST 7
		
		// SUBMISSION ALL OK (COURSE AND COURSEGROUP CALCULABLE)
		
		// reset fixedup flags
		assignmentData.setGradeIncorrect(false);
		
		assignmentDataRaw.setAction("AE");
		assignmentDataRaw.setUserId("std");
		assignmentDataRaw.setSemester("2015-2");
		
		DbSetupHelper.startTransaction();
		
		DbSetupHelper.emptyDataRawTables();
		
		assignmentDataRepository.saveAssignmentDataRaw(assignmentDataRaw);
		
		reportsEtlRepository.assignmentDataEtlProcess();
		
		assignmentData = getLastAssignmentData();
		
		DbSetupHelper.commitTransaction();
		
		// VALIDATE FIX-UPS
		
		assertFalse(assignmentData.isStudentFixedUp());
		assertFalse(assignmentData.isSubjectFixedUp());
		assertNull(assignmentData.getCourse());
		assertNull(assignmentData.getCourseGroup());
		assertEquals(semester.getCode(), assignmentData.getSemester().getCode());
		assertFalse(assignmentData.isSemesterFixedUp());
		
		endDate = assignmentData.getEndDate();
		
		assertTrue(new Date().getTime() - endDate.getTime() < 100000);
		
		// TEST 8
		
		// ASSESSMENT ALL OK (COURSE AND COURSEGROUP CALCULABLE)
		
		assignmentDataRaw.setAction("AA");
		assignmentDataRaw.setGrade("5");
		assignmentDataRaw.setSubjectCode("Subject 1");
		assignmentDataRaw.setSemester("20152");
		
		DbSetupHelper.startTransaction();
		
		DbSetupHelper.emptyDataRawTables();
		
		assignmentDataRepository.saveAssignmentDataRaw(assignmentDataRaw);
		
		reportsEtlRepository.assignmentDataEtlProcess();
		
		assignmentData = getLastAssignmentData();
		
		DbSetupHelper.commitTransaction();
		
		// VALIDATE FIX-UPS
		
		assertFalse(assignmentData.isGradeIncorrect());
		
		assertEquals(course.getId(), assignmentData.getCourse().getId());
		assertEquals(courseGroup.getId(), assignmentData.getCourseGroup().getId());
		assertEquals(semester.getCode(), assignmentData.getSemester().getCode());
		assertFalse(assignmentData.isSemesterFixedUp());
		
		endDate = assignmentData.getEndDate();
		
		assertTrue(new Date().getTime() - endDate.getTime() < 100000);
		
		// TEST 9
		
		// PROGRAM EXISTS BUT SUBJECT NOT
		
		assignmentDataRaw.setProgramCode("P1");
		assignmentDataRaw.setSubjectCode("Subject 2");
		
		DbSetupHelper.startTransaction();
		
		DbSetupHelper.emptyDataRawTables();
		
		assignmentDataRepository.saveAssignmentDataRaw(assignmentDataRaw);
		
		reportsEtlRepository.assignmentDataEtlProcess();
		
		assignmentData = getLastAssignmentData();
		
		DbSetupHelper.commitTransaction();
		
		// VALIDATE FIX-UPS
		
		assertEquals(program.getId(), assignmentData.getProgram().getId());
		assertEquals("Subject 2", assignmentData.getSubject().getCode());
		assertEquals(((Subject) program.getSubjects().toArray()[0]).getId(), assignmentData.getSubject().getId());
		
		endDate = assignmentData.getEndDate();
		
		assertTrue(new Date().getTime() - endDate.getTime() < 100000);
	}
	
	@Test
	public void processAssignmentDataRawETLProcessTest() throws Exception
	{
		AssignmentDataRaw assignmentDataRaw = new AssignmentDataRaw();
		
		AssignmentData assignmentData;
		
		assignmentDataRaw.setTool("FAKEFAT");
		assignmentDataRaw.setToolUUID("afe9ead0-8a55-4bf7-bce9-f78e81afdecb");
		
		// TEST 1 
		
		// START - END - ASSESSMENT
		
		// TEST 1.1 START  
		
		assignmentDataRaw.setUserId("std 1");
		assignmentDataRaw.setAction("AS");
		assignmentDataRaw.setAssignment("assignment1");
		
		DbSetupHelper.startTransaction();
		
		DbSetupHelper.emptyDataRawTables();
		
		assignmentDataRepository.saveAssignmentDataRaw(assignmentDataRaw);
		
		reportsEtlRepository.assignmentDataEtlProcess();
		
		assignmentData = getLastAssignmentData();
		
		DbSetupHelper.commitTransaction();
		
		// VALIDATE FIX-UPS
		
		Date startDate = assignmentData.getStartDate();
		
		assertTrue(new Date().getTime() - startDate.getTime() < 100000);
		
		assertNull(assignmentData.getEndDate());
		assertEquals(-1, assignmentData.getDuration_sec());
		
		Thread.sleep(1000);
		
		// TEST 1.2 END  
		
		DbSetupHelper.startTransaction();
		
		assignmentDataRaw.setAction("AE");
		
		assignmentDataRepository.saveAssignmentDataRaw(assignmentDataRaw);
		
		reportsEtlRepository.assignmentDataEtlProcess();
		
		assignmentData = getLastAssignmentData();
		
		DbSetupHelper.commitTransaction();
		
		// VALIDATE FIX-UPS
		
		startDate = assignmentData.getStartDate();
		
		assertTrue(new Date().getTime() - startDate.getTime() < 100000);
		
		Date endDate = assignmentData.getEndDate();
		
		assertTrue(new Date().getTime() - endDate.getTime() < 100000);
		
		assertTrue(assignmentData.getDuration_sec() > 0);
		
		// TEST 1.3 ASSESSMENT  
		
		DbSetupHelper.startTransaction();
		
		assignmentDataRaw.setAction("AA");
		assignmentDataRaw.setGrade("9");
		
		assignmentDataRepository.saveAssignmentDataRaw(assignmentDataRaw);
		
		reportsEtlRepository.assignmentDataEtlProcess();
		
		assignmentData = getLastAssignmentData();
		
		DbSetupHelper.commitTransaction();
		
		// VALIDATE FIX-UPS
		
		startDate = assignmentData.getStartDate();
		
		assertTrue(new Date().getTime() - startDate.getTime() < 100000);
		
		endDate = assignmentData.getEndDate();
		
		assertTrue(new Date().getTime() - endDate.getTime() < 100000);
		
		assertTrue(assignmentData.getDuration_sec() > 0);
		
		Thread.sleep(1000);
		
		// TEST 2 
		
		// END - ASSESSMENT
		
		// TEST 2.1 END  
		
		DbSetupHelper.startTransaction();
		
		assignmentDataRaw.setAction("AE");
		
		assignmentDataRepository.saveAssignmentDataRaw(assignmentDataRaw);
		
		reportsEtlRepository.assignmentDataEtlProcess();
		
		assignmentData = getLastAssignmentData();
		
		DbSetupHelper.commitTransaction();
		
		// VALIDATE FIX-UPS
		
		startDate = assignmentData.getStartDate();
		
		assertTrue(new Date().getTime() - startDate.getTime() < 100000);
		
		assertEquals(assignmentData.getStartDate(), assignmentData.getEndDate());
		
		assertTrue(assignmentData.getDuration_sec() == 0);
		
		assertTrue(assignmentData.isStartDateFixedUp());
		
		// TEST 2.2 ASSESSMENT  
		
		DbSetupHelper.startTransaction();
		
		assignmentDataRaw.setAction("AA");
		assignmentDataRaw.setGrade("5");
		
		assignmentDataRepository.saveAssignmentDataRaw(assignmentDataRaw);
		
		reportsEtlRepository.assignmentDataEtlProcess();
		
		assignmentData = getLastAssignmentData();
		
		DbSetupHelper.commitTransaction();
		
		// VALIDATE FIX-UPS
		
		startDate = assignmentData.getStartDate();
		
		assertTrue(new Date().getTime() - startDate.getTime() < 100000);
		
		endDate = assignmentData.getEndDate();
		
		assertTrue(new Date().getTime() - endDate.getTime() < 100000);
		
		assertTrue(assignmentData.getDuration_sec() == 0);
		
		Thread.sleep(1000);
		
		// TEST 3 
		
		// ASSESSMENT
		
		// TEST 3.1 ASSESSMENT  
		
		DbSetupHelper.startTransaction();
		
		assignmentDataRaw.setAction("AA");
		assignmentDataRaw.setGrade("5");
		
		assignmentDataRepository.saveAssignmentDataRaw(assignmentDataRaw);
		
		reportsEtlRepository.assignmentDataEtlProcess();
		
		assignmentData = getLastAssignmentData();
		
		DbSetupHelper.commitTransaction();
		
		// VALIDATE FIX-UPS
		
		startDate = assignmentData.getStartDate();
		
		assertTrue(new Date().getTime() - startDate.getTime() < 100000);
		
		endDate = assignmentData.getEndDate();
		
		assertTrue(new Date().getTime() - endDate.getTime() < 100000);
		
		assertTrue(assignmentData.getDuration_sec() == 0);
	}
	
	private AssignmentData getLastAssignmentData() throws Exception
	{
		return new DBHelper(getEntityManager()).getFirst("SELECT s FROM AssignmentData s ORDER BY s.startDate DESC", AssignmentData.class);
	}
}
