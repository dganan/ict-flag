package edu.uoc.ictflag.institution.dal.test;

import static edu.uoc.ictflag.setup.core.DbSetupHelper.commitTransaction;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.getEntityManager;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.setupCourse;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.setupCourseGroup;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.setupProgram;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.setupSemester;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.setupSubject;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.setupUser;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.startTransaction;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.uoc.ictflag.core.IctFlagValidationException;
import edu.uoc.ictflag.core.localization.LocalizedString;
import edu.uoc.ictflag.dal.test.DBTestHelper;
import edu.uoc.ictflag.institution.dal.CourseGroupRepository;
import edu.uoc.ictflag.institution.dal.CourseRepository;
import edu.uoc.ictflag.institution.dal.ProgramRepository;
import edu.uoc.ictflag.institution.dal.SubjectRepository;
import edu.uoc.ictflag.institution.model.Course;
import edu.uoc.ictflag.institution.model.CourseGroup;
import edu.uoc.ictflag.institution.model.Program;
import edu.uoc.ictflag.institution.model.Semester;
import edu.uoc.ictflag.institution.model.Subject;
import edu.uoc.ictflag.security.dal.UserRepository;
import edu.uoc.ictflag.security.model.User;
import edu.uoc.ictflag.security.model.UserRole;

public class CourseRepositoryTest
{
	private static List<Subject> subjects;
	private static List<Course> courses;
	
	private static UserRepository ur;
	private static ProgramRepository pr;
	private static SubjectRepository sr;
	private static CourseRepository cr;
	private static CourseGroupRepository cgr;
	
	private static User lect2;
	
	private static Program p1;
	private static Program p2;
	private static Program p3;
	
	private static Semester s1;
	private static Semester s2;
	
	@BeforeClass
	public static void setUpCourseRepositoryTest() throws Exception
	{
		DBTestHelper.reset();
		
		ur = new UserRepository();
		ur.setEntityManager(getEntityManager());
		
		sr = new SubjectRepository(ur);
		sr.setEntityManager(getEntityManager());
		
		pr = new ProgramRepository(ur);
		pr.setEntityManager(getEntityManager());
		
		cr = new CourseRepository(ur);
		cr.setEntityManager(getEntityManager());
		
		cgr = new CourseGroupRepository(ur);
		cgr.setEntityManager(getEntityManager());
		
		setupUser("su", "Super user", "su", "su@uoc.edu", Arrays.asList(UserRole.SUPERUSER));
		User pm1 = setupUser("pm1", "Program manager 1", "pm1", "pm1@uoc.edu", Arrays.asList(UserRole.PROGRAM_MANAGER));
		User pm2 = setupUser("pm2", "Program manager 2", "pm2", "pm2@uoc.edu", Arrays.asList(UserRole.PROGRAM_MANAGER));
		User lect1 = setupUser("lect1", "Lecturer 1", "lect1", "lect1@uoc.edu", Arrays.asList(UserRole.LECTURER));
		lect2 = setupUser("lect2", "Lecturer 2", "lect2", "lect2@uoc.edu", Arrays.asList(UserRole.LECTURER));
		User inst1 = setupUser("inst1", "Instructor 1", "inst1", "inst1@uoc.edu", Arrays.asList(UserRole.INSTRUCTOR));
		User inst2 = setupUser("inst2", "Instructor 2", "inst2", "inst2@uoc.edu", Arrays.asList(UserRole.INSTRUCTOR));
		
		subjects = new ArrayList<Subject>();
		
		p1 = setupProgram("P1", LocalizedString.fromStringFormat("ca#Program 1;en#Program 1"), pm1);
		p2 = setupProgram("P2", LocalizedString.fromStringFormat("ca#Program 2;en#Program 2"), pm1);
		p3 = setupProgram("P3", LocalizedString.fromStringFormat("ca#Program 3;en#Program 3"), pm2);
		
		subjects.add(setupSubject(LocalizedString.fromStringFormat("ca#Assignatura 1;en#Subject 1"), "12.000"));
		subjects.add(setupSubject(LocalizedString.fromStringFormat("ca#Assignatura 2;en#Subject 2"), "14.000"));
		subjects.add(setupSubject(LocalizedString.fromStringFormat("ca#Assignatura 3;en#Subject 3"), "16.000"));
		
		s1 = setupSemester(2016, 1);
		s2 = setupSemester(2016, 2);
		
		courses = new ArrayList<Course>();
		
		courses.add(setupCourse(LocalizedString.fromStringFormat("en#Course 1;ca#Curs 1"), "C1", s1, "ca", subjects.get(0), lect1));
		courses.add(setupCourse(LocalizedString.fromStringFormat("en#Course 2;ca#Curs 2"), "C2", s1, "ca", subjects.get(1), null));
		courses.add(setupCourse(LocalizedString.fromStringFormat("en#Course 2b;ca#Curs 2b"), "C2b", s2, "ca", subjects.get(1), lect2));
		
		setupCourseGroup(LocalizedString.fromStringFormat("en#Course 1 - Room 1;ca#Curs 1 - Aula 1"), courses.get(0), inst1);
		setupCourseGroup(LocalizedString.fromStringFormat("en#Course 1 - Room 2;ca#Curs 1 - Aula 2"), courses.get(0), null);
		setupCourseGroup(LocalizedString.fromStringFormat("en#Course 2 - Room 1;ca#Curs 2 - Aula 1"), courses.get(1), inst2);
		setupCourseGroup(LocalizedString.fromStringFormat("en#Course 2 - Room 2;ca#Curs 2 - Aula 2"), courses.get(1), inst2);
		
		startTransaction();
		
		p1.getSubjects().add(subjects.get(0));
		p1.getSubjects().add(subjects.get(2));
		p2.getSubjects().add(subjects.get(2));
		
		p3.getSubjects().add(subjects.get(0));
		p3.getSubjects().add(subjects.get(1));
		
		pr.createOrUpdateProgram(p1);
		pr.createOrUpdateProgram(p2);
		pr.createOrUpdateProgram(p3);
		
		commitTransaction();
	}
	
	@Test
	public void getCoursesListTest() throws Exception
	{
		List<Course> cs = cr.getCoursesList("su");
		
		assertEquals(3, cs.size());
		
		for (Course c : cs)
		{
			assertTrue(
					c.getId().equals(courses.get(0).getId()) || c.getId().equals(courses.get(1).getId()) || c.getId().equals(courses.get(2).getId()));
		}
		
		// gets only the Courses tied to the programManager 1
		
		cs = cr.getCoursesList("pm1");
		
		assertEquals(1, cs.size());
		
		for (Course s : cs)
		{
			assertTrue(s.getId().equals(courses.get(0).getId()));
		}
		
		// gets only the Courses tied to the programManager 2
		
		cs = cr.getCoursesList("pm2");
		
		assertEquals(3, cs.size());
		
		for (Course s : cs)
		{
			assertTrue(
					s.getId().equals(courses.get(0).getId()) || s.getId().equals(courses.get(1).getId()) || s.getId().equals(courses.get(2).getId()));
		}
		
		// gets only the Courses tied to the lecturer 1
		
		cs = cr.getCoursesList("lect1");
		
		assertEquals(1, cs.size());
		
		for (Course s : cs)
		{
			assertTrue(s.getId().equals(courses.get(0).getId()));
		}
		
		// gets only the Courses tied to the lecturer 2
		
		cs = cr.getCoursesList("lect2");
		
		assertEquals(1, cs.size());
		
		for (Course s : cs)
		{
			assertTrue(s.getId().equals(courses.get(2).getId()));
		}
		
		// gets only the Courses tied to the instructor 1
		
		cs = cr.getCoursesList("inst1");
		
		assertEquals(1, cs.size());
		
		for (Course s : cs)
		{
			assertTrue(s.getId().equals(courses.get(0).getId()));
		}
		
		// gets only the Courses tied to the instructor 2
		
		cs = cr.getCoursesList("inst2");
		
		assertEquals(1, cs.size());
		
		for (Course s : cs)
		{
			assertTrue(s.getId().equals(courses.get(1).getId()));
		}
	}
	
	@Test
	public void deleteCourseTest() throws Exception
	{
		Course c = setupCourse(LocalizedString.fromStringFormat("en#Course 3;ca#Curs 3"), "C3", s2, "ca", subjects.get(2), lect2);
		
		CourseGroup cg1 = setupCourseGroup(LocalizedString.fromStringFormat("en#Course 3 - Room 1;ca#Curs 3 - Aula 1"), c, null);
		CourseGroup cg2 = setupCourseGroup(LocalizedString.fromStringFormat("en#Course 3 - Room 2;ca#Curs 3 - Aula 2"), c, null);
		
		Long id = c.getId();
		
		try
		{
			startTransaction();
		
			cr.deleteCourse(id);
		
			commitTransaction();
			
			fail();
		}
		catch (Exception e)
		{
			assertTrue(e instanceof IctFlagValidationException);
		}

		cgr.deleteCourseGroup(cg1.getId());
		cgr.deleteCourseGroup(cg2.getId());
		
		cr.deleteCourse(id);
	}
}
