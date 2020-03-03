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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.uoc.ictflag.core.IctFlagValidationException;
import edu.uoc.ictflag.core.dal.DBHelper;
import edu.uoc.ictflag.core.localization.LocalizedString;
import edu.uoc.ictflag.dal.test.DBTestHelper;
import edu.uoc.ictflag.institution.dal.CourseRepository;
import edu.uoc.ictflag.institution.dal.ProgramRepository;
import edu.uoc.ictflag.institution.dal.SubjectRepository;
import edu.uoc.ictflag.institution.model.Course;
import edu.uoc.ictflag.institution.model.Program;
import edu.uoc.ictflag.institution.model.Semester;
import edu.uoc.ictflag.institution.model.Subject;
import edu.uoc.ictflag.security.dal.UserRepository;
import edu.uoc.ictflag.security.model.User;
import edu.uoc.ictflag.security.model.UserRole;

public class SubjectRepositoryTest
{
	private static List<Subject> subjects;
	private static UserRepository ur;
	private static ProgramRepository pr;
	private static SubjectRepository sr;
	private static CourseRepository cr;
	
	private static Program p1;
	private static Program p2;
	private static Program p3;
	
	private static Semester s1;
	
	@BeforeClass
	public static void setUpSubjectRepositoryTest() throws Exception
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
		
		setupUser("su", "Super user", "su", "su@uoc.edu", Arrays.asList(UserRole.SUPERUSER));
		User pm1 = setupUser("pm1", "Program manager 1", "pm1", "pm1@uoc.edu", Arrays.asList(UserRole.PROGRAM_MANAGER));
		User pm2 = setupUser("pm2", "Program manager 2", "pm2", "pm2@uoc.edu", Arrays.asList(UserRole.PROGRAM_MANAGER));
		User lect = setupUser("lect", "Lecturer", "lect", "lext@uoc.edu", Arrays.asList(UserRole.LECTURER));
		User inst = setupUser("inst", "Instructor", "inst", "inst@uoc.edu", Arrays.asList(UserRole.INSTRUCTOR));
		
		subjects = new ArrayList<Subject>();
		
		p1 = setupProgram("P1", LocalizedString.fromStringFormat("ca#Program 1;en#Program 1"), pm1);
		p2 = setupProgram("P2", LocalizedString.fromStringFormat("ca#Program 2;en#Program 2"), pm1);
		p3 = setupProgram("P3", LocalizedString.fromStringFormat("ca#Program 3;en#Program 3"), pm2);
		
		subjects.add(setupSubject(LocalizedString.fromStringFormat("ca#Assignatura 1;en#Subject 1"), "12.000"));
		subjects.add(setupSubject(LocalizedString.fromStringFormat("ca#Assignatura 2;en#Subject 2"), "14.000"));
		subjects.add(setupSubject(LocalizedString.fromStringFormat("ca#Assignatura 3;en#Subject 3"), "16.000"));
		
		List<Course> courses = new ArrayList<Course>();
		
		s1 = setupSemester(2016, 1);
		
		courses.add(setupCourse(LocalizedString.fromStringFormat("en#Course 1;ca#Curs 1"), "C1", s1, "ca", subjects.get(0), lect));
		courses.add(setupCourse(LocalizedString.fromStringFormat("en#Course 2;ca#Curs 2"), "C2", s1, "ca", subjects.get(1), lect));
		
		setupCourseGroup(LocalizedString.fromStringFormat("en#Course 1 - Room 1;ca#Curs 1 - Aula 1"), courses.get(0), inst);
		setupCourseGroup(LocalizedString.fromStringFormat("en#Course 1 - Room 2;ca#Curs 1 - Aula 2"), courses.get(0), null);
		setupCourseGroup(LocalizedString.fromStringFormat("en#Course 2 - Room 1;ca#Curs 2 - Aula 1"), courses.get(1), null);
		setupCourseGroup(LocalizedString.fromStringFormat("en#Course 2 - Room 2;ca#Curs 2 - Aula 2"), courses.get(1), null);
		
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
	public void getSubjectsListTest() throws Exception
	{
		// getAll returns all the subjects on DB
		
		List<Subject> ps = sr.getSubjectsList("su");
		
		assertEquals(3, ps.size());
		
		for (Subject s : ps)
		{
			assertTrue(s.getId().equals(subjects.get(0).getId()) || s.getId().equals(subjects.get(1).getId())
					|| s.getId().equals(subjects.get(2).getId()));
		}
		
		// gets only the Subjects tied to the programManager 1
		
		ps = sr.getSubjectsList("pm1");
		
		assertEquals(3, ps.size());
		
		for (Subject s : ps)
		{
			assertTrue(s.getId().equals(subjects.get(0).getId()) || s.getId().equals(subjects.get(1).getId())
					|| s.getId().equals(subjects.get(2).getId()));
		}
		
		// gets only the Subjects tied to the programManager 2
		
		ps = sr.getSubjectsList("pm2");
		
		assertEquals(2, ps.size());
		
		for (Subject s : ps)
		{
			assertTrue(s.getId().equals(subjects.get(0).getId()) || s.getId().equals(subjects.get(1).getId()));
		}
		
		// gets only the Subjects tied to the lecturer
		
		ps = sr.getSubjectsList("lect");
		
		assertEquals(2, ps.size());
		
		for (Subject s : ps)
		{
			assertTrue(s.getId().equals(subjects.get(0).getId()) || s.getId().equals(subjects.get(1).getId()));
		}
		
		// gets only the Subjects tied to the instructor
		
		ps = sr.getSubjectsList("inst");
		
		assertEquals(1, ps.size());
		
		for (Subject s : ps)
		{
			assertTrue(s.getId().equals(subjects.get(0).getId()));
		}
	}
	
	@Test
	public void deleteSubjectTest() throws Exception
	{
		Subject s = setupSubject(LocalizedString.fromStringFormat("ca#Assignatura 4;en#Subject 4"), "K4");
		
		startTransaction();
		
		p1.getSubjects().add(s);
		p2.getSubjects().add(s);
		
		pr.createOrUpdateProgram(p1);
		pr.createOrUpdateProgram(p2);
		
		commitTransaction();
		
		Course course1 = setupCourse(LocalizedString.fromStringFormat("en#Course 4a;ca#Curs 4a"), "C4a", s1, "ca", s, null);
		Course course2 = setupCourse(LocalizedString.fromStringFormat("en#Course 4b;ca#Curs 4b"), "C4b", s1, "ca", s, null);
		
		assertEquals(3, pr.getProgram("su", p1.getId()).getSubjects().size());
		assertEquals(2, pr.getProgram("su", p2.getId()).getSubjects().size());
		
		Long id = s.getId();
		
		assertEquals(id, course1.getSubject().getId());
		assertEquals(id, course2.getSubject().getId());
		
		try
		{
			startTransaction();
			
			sr.deleteSubject(id);
			
			commitTransaction();
			
			fail();
		}
		catch (Exception e)
		{
			assertTrue(e instanceof IctFlagValidationException);
		}
		
		cr.deleteCourse(course1.getId());
		cr.deleteCourse(course2.getId());
		
		sr.deleteSubject(id);
		
		assertNull(sr.getSubject("su", id));
		
		DBHelper h = new DBHelper(getEntityManager());
		
		ArrayList<Object> parameters = new ArrayList<Object>();
		
		parameters.add(id);
		
		long count = (long) h.getScalarValueNative("SELECT count(*) FROM program_subject ps WHERE ps.subject_id = ?", parameters);
		
		assertEquals(0, count);
		
		assertEquals(2, pr.getProgram("su", p1.getId()).getSubjects().size());
		assertEquals(1, pr.getProgram("su", p2.getId()).getSubjects().size());
		
		Course c1 = cr.getCourse("su", course1.getId());
		Course c2 = cr.getCourse("su", course2.getId());
		
		assertNull(c1);
		assertNull(c2);
	}
}
