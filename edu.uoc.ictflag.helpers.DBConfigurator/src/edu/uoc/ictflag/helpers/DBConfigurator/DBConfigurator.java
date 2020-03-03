package edu.uoc.ictflag.helpers.DBConfigurator;

import static edu.uoc.ictflag.setup.core.DbSetupHelper.createDataRawTables;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.setupCourse;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.setupCourseGroup;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.setupCourseGroupMember;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.setupPage;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.setupProgram;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.setupRolePageAccess;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.setupRolePermission;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.setupSubject;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.setupTool;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.setupUser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import edu.uoc.ictflag.core.localization.LocalizedString;
import edu.uoc.ictflag.ela.model.Assignment;
import edu.uoc.ictflag.ela.model.Exercise;
import edu.uoc.ictflag.ela.model.Outcome;
import edu.uoc.ictflag.ela.model.Tool;
import edu.uoc.ictflag.institution.model.Course;
import edu.uoc.ictflag.institution.model.CourseGroup;
import edu.uoc.ictflag.institution.model.Institution;
import edu.uoc.ictflag.institution.model.Program;
import edu.uoc.ictflag.institution.model.Semester;
import edu.uoc.ictflag.institution.model.Subject;
import edu.uoc.ictflag.institution.model.VLE;
import edu.uoc.ictflag.security.model.Page;
import edu.uoc.ictflag.security.model.Permission;
import edu.uoc.ictflag.security.model.User;
import edu.uoc.ictflag.security.model.UserRole;
import edu.uoc.ictflag.setup.core.DbSetupHelper;

public class DBConfigurator
{
	private static EntityManager em;
	
	private static Semester s20151;
	
	public static void main(String[] args)
	{
		try
		{
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("edu.uoc.ictflag.helpers.DBConfigurator");
			
			em = emf.createEntityManager();
			
			DbSetupHelper.setEntityManager(em);
			
			DbSetupHelper.startTransaction();
			
			setupDevelopmentDatabase();

			// setupProductionDatabase();
			
			DbSetupHelper.commitTransaction();
		}
		catch (Exception e)
		{
			if (em != null && em.getTransaction() != null)
			{
				em.getTransaction().rollback();
			}
			
			System.out.println(e.getMessage());
			e.printStackTrace(System.out);
		}
	}
	
	//	private static void setupProductionDatabase() throws Exception
	//	{
	//		// TODO Auto-generated method stub
	//	}
	
	private static void setupDevelopmentDatabase() throws Exception
	{
		createDataRawTables();
		
		setupUser("su", "Super user", "su", "su@uoc.edu", Arrays.asList(UserRole.SUPERUSER, UserRole.STUDENT));
		setupUser("admin", "Admin", "admin", "admin@uoc.edu", Arrays.asList(UserRole.ADMIN));
		User pm1 = setupUser("pm1", "Program manager 1", "pm1", "pm1@uoc.edu", Arrays.asList(UserRole.PROGRAM_MANAGER));
		User pm2 = setupUser("pm2", "Program manager 2", "pm2", "pm2@uoc.edu", Arrays.asList(UserRole.PROGRAM_MANAGER));
		setupUser("lect", "Lecturer", "lect", "lect@uoc.edu", Arrays.asList(UserRole.LECTURER));
		setupUser("inst", "Instructor", "inst", "inst@uoc.edu", Arrays.asList(UserRole.INSTRUCTOR));
		
		setupInstitution();
		
		List<Subject> subjects = setupSubjects();
		
		List<Program> programs = setupPrograms();
		
		programs.get(0).setProgramManager(pm1);
		programs.get(1).setProgramManager(pm1);
		programs.get(2).setProgramManager(pm2);
		
		programs.get(0).getSubjects().add(subjects.get(0));
		programs.get(0).getSubjects().add(subjects.get(2));
		
		programs.get(1).getSubjects().add(subjects.get(0));
		programs.get(1).getSubjects().add(subjects.get(1));
		
		programs.get(2).getSubjects().add(subjects.get(1));
		
		setupPermissions();
		
		List<Semester> semesters = setupSemesters();
		
		List<Course> courses = setupCourses(subjects, semesters);
		
		List<CourseGroup> groups = setupGroups(courses);
		
		List<User> students = setupGroupMembers(groups);
		
		List<Tool> tools = setupTools();
		
		setupAssignmentsData(tools, students, programs, subjects, courses, groups);
		setupExercisesData(tools, students, programs, subjects, courses, groups);
	}
	
	private static void setupExercisesData(List<Tool> tools, List<User> students, List<Program> programs, List<Subject> subjects,
			List<Course> courses, List<CourseGroup> groups) throws ParseException, Exception
	{
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Exercise ex1 = DbSetupHelper.setupExercise(tools.get(0), "EX1");
		Exercise ex2 = DbSetupHelper.setupExercise(tools.get(0), "EX2");
		Exercise ex3 = DbSetupHelper.setupExercise(tools.get(0), "EX3");
		
		Assignment a1 = DbSetupHelper.setupAssignment(courses.get(0), "PAC1", ex1, ex2, ex3);
		
		DbSetupHelper.setupExerciseData(tools.get(0), students.get(0), programs.get(0), subjects.get(0), courses.get(0), groups.get(0), a1, ex1,
				Outcome.WRONG, 0, formatter.parse("2016-01-31 10:00:00"), formatter.parse("2016-01-31 10:30:00"),
				formatter.parse("2016-01-31 10:30:00"), 1800, 0, 1);
				
		DbSetupHelper.setupExerciseData(tools.get(0), students.get(0), programs.get(0), subjects.get(0), courses.get(0), groups.get(0), a1, ex1,
				Outcome.RIGHT, 5, formatter.parse("2016-01-31 10:30:00"), formatter.parse("2016-01-31 11:00:00"),
				formatter.parse("2016-01-31 11:00:00"), 1800, 0, 2);
				
		DbSetupHelper.setupExerciseData(tools.get(0), students.get(0), programs.get(0), subjects.get(0), courses.get(0), groups.get(0), a1, ex1,
				Outcome.RIGHT, 8, formatter.parse("2016-01-31 12:00:00"), formatter.parse("2016-01-31 12:30:00"),
				formatter.parse("2016-01-31 12:30:00"), 1800, 3600, 3);
		
		DbSetupHelper.setupExerciseData(tools.get(0), students.get(0), programs.get(0), subjects.get(0), courses.get(0), groups.get(0), a1, ex2,
				Outcome.RIGHT, 6.5, formatter.parse("2016-02-02 12:00:00"), formatter.parse("2016-02-02 12:30:00"),
				formatter.parse("2016-02-02 12:30:00"), 1800, 0, 1);
		
		DbSetupHelper.setupExerciseData(tools.get(0), students.get(1), programs.get(0), subjects.get(0), courses.get(0), groups.get(0), a1, ex1,
				Outcome.RIGHT, 9, formatter.parse("2016-01-31 10:00:00"), formatter.parse("2016-01-31 10:15:00"),
				formatter.parse("2016-01-31 10:15:00"), 900, 0, 1);
				
		DbSetupHelper.setupExerciseData(tools.get(0), students.get(1), programs.get(0), subjects.get(0), courses.get(0), groups.get(0), a1, ex2,
				Outcome.RIGHT, 6.5, formatter.parse("2016-02-03 12:00:00"), formatter.parse("2016-02-03 12:30:00"),
				formatter.parse("2016-02-03 12:30:00"), 1800, 0, 1);
				
		DbSetupHelper.setupExerciseData(tools.get(0), students.get(1), programs.get(0), subjects.get(0), courses.get(0), groups.get(0), a1, ex3,
				Outcome.WRONG, 4, formatter.parse("2016-02-04 12:00:00"), formatter.parse("2016-02-04 12:30:00"),
				formatter.parse("2016-02-04 12:30:00"), 1800, 0, 1);
	}

	private static void setupAssignmentsData(List<Tool> tools, List<User> students, List<Program> programs, List<Subject> subjects,
			List<Course> courses, List<CourseGroup> groups) throws ParseException, Exception
	{
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Assignment a2 = DbSetupHelper.setupAssignment(courses.get(0), "PAC2");
		Assignment a3 = DbSetupHelper.setupAssignment(courses.get(0), "PAC3");
		Assignment a4 = DbSetupHelper.setupAssignment(courses.get(0), "PAC4");
		
		DbSetupHelper.setupAssignmentData(tools.get(0), students.get(0), programs.get(0), subjects.get(0), courses.get(0), groups.get(0), a2, 0, formatter.parse("2016-01-31 10:00:00"), formatter.parse("2016-01-31 10:30:00"),				formatter.parse("2016-01-31 10:30:00"), 1800);
				
		DbSetupHelper.setupAssignmentData(tools.get(0), students.get(0), programs.get(0), subjects.get(0), courses.get(0), groups.get(0), a2, 5,
				formatter.parse("2016-01-31 10:30:00"), formatter.parse("2016-01-31 11:00:00"), formatter.parse("2016-01-31 11:00:00"), 1800);
				
		DbSetupHelper.setupAssignmentData(tools.get(0), students.get(0), programs.get(0), subjects.get(0), courses.get(0), groups.get(0), a2, 8,
				formatter.parse("2016-01-31 12:00:00"), formatter.parse("2016-01-31 12:30:00"), formatter.parse("2016-01-31 12:30:00"), 1800);
		
		DbSetupHelper.setupAssignmentData(tools.get(0), students.get(0), programs.get(0), subjects.get(0), courses.get(0), groups.get(0), a3, 6.5,
				formatter.parse("2016-02-02 12:00:00"), formatter.parse("2016-02-02 12:30:00"), formatter.parse("2016-02-02 12:30:00"), 1800);
		
		DbSetupHelper.setupAssignmentData(tools.get(0), students.get(1), programs.get(0), subjects.get(0), courses.get(0), groups.get(0), a3, 9,
				formatter.parse("2016-01-31 10:00:00"), formatter.parse("2016-01-31 10:15:00"), formatter.parse("2016-01-31 10:15:00"), 900);
				
		DbSetupHelper.setupAssignmentData(tools.get(0), students.get(1), programs.get(0), subjects.get(0), courses.get(0), groups.get(0), a4, 6.5,
				formatter.parse("2016-02-03 12:00:00"), formatter.parse("2016-02-03 12:30:00"), formatter.parse("2016-02-03 12:30:00"), 1800);
				
		DbSetupHelper.setupAssignmentData(tools.get(0), students.get(1), programs.get(0), subjects.get(0), courses.get(0), groups.get(0), a4, 4,
				formatter.parse("2016-02-04 12:00:00"), formatter.parse("2016-02-04 12:30:00"), formatter.parse("2016-02-04 12:30:00"), 1800);
	}

	//	static Random rnd = new Random();
	//	
	//	private static void setupAssignmentData(List<Tool> tools, List<User> students, List<Program> programs, List<Subject> subjects,
	//			List<Semester> semesters)
	//	{
	//		rnd = new Random();
	//		
	//		// FakeFAT tool
	//		
	//		Tool fakeTool = tools.get(0);
	//		
	//		// 3 semestres
	//		
	//		for (int sem = 0; sem < 3; sem++)
	//		{
	//			Semester semester = semesters.get(sem);
	//			
	//			// Varios programas
	//			// Varias asignaturas
	//			
	//			setupAssignmentData(fakeTool, programs.get(0), subjects.get(0), semester);
	//			setupAssignmentData(fakeTool, programs.get(0), subjects.get(2), semester);
	//			
	//			setupAssignmentData(fakeTool, programs.get(1), subjects.get(0), semester);
	//			setupAssignmentData(fakeTool, programs.get(1), subjects.get(1), semester);
	//			
	//			setupAssignmentData(fakeTool, programs.get(2), subjects.get(1), semester);
	//		}
	//	}
	//	
	//	private static void setupAssignmentData(Tool tool, Program program, Subject subject, Semester semester)
	//	{
	//		// int assignmentCount = rnd.nextInt(4);
	//		
	//		// estudiants matriculats en l'assignatura ==> no es necessari forçar-ho ja que no es valida actualment.
	//		
	//		// dos o tres actividades con 2 o 3 ejercicios cada una - marcar fecha inicio y fin de la actividad para cada semestre (dividir el periodo del semestre en trozos mas o menos regulares).
	//		
	//		// para cada estudiante aleatorizar las fechas de las diferentes acciones (pero que estén entre la fecha de inicio y la de fin y que sigan un orden)
	//		
	//		// en algunos casos repetir submissions de ejercicios?
	//	}
	
	private static List<Semester> setupSemesters() throws Exception
	{
		List<Semester> semesters = new ArrayList<Semester>();
		
		DbSetupHelper.startTransaction();
		
		Semester s = DbSetupHelper.setupSemester(2010, 1, 2010, 10, 1, 2011, 2, 28);
		semesters.add(s);
		
		s = DbSetupHelper.setupSemester(2010, 2, 2011, 3, 1, 2011, 7, 31);
		semesters.add(s);
		
		s = DbSetupHelper.setupSemester(2011, 1, 2011, 10, 1, 2012, 2, 28);
		semesters.add(s);
		
		s = DbSetupHelper.setupSemester(2011, 2, 2012, 3, 1, 2012, 7, 31);
		semesters.add(s);
		
		s = DbSetupHelper.setupSemester(2012, 1, 2012, 10, 1, 2013, 2, 28);
		semesters.add(s);
		
		s = DbSetupHelper.setupSemester(2012, 2, 2013, 3, 1, 2013, 7, 31);
		semesters.add(s);
		
		s = DbSetupHelper.setupSemester(2013, 1, 2013, 10, 1, 2014, 2, 28);
		semesters.add(s);
		
		s = DbSetupHelper.setupSemester(2013, 2, 2014, 3, 1, 2014, 7, 31);
		semesters.add(s);
		
		s = DbSetupHelper.setupSemester(2014, 1, 2014, 10, 1, 2015, 2, 28);
		semesters.add(s);
		
		s = DbSetupHelper.setupSemester(2014, 2, 2015, 3, 1, 2015, 7, 31);
		semesters.add(s);
		
		s20151 = DbSetupHelper.setupSemester(2015, 1, 2015, 10, 1, 2016, 2, 28);
		semesters.add(s20151);
		
		s = DbSetupHelper.setupSemester(2015, 2, 2016, 3, 1, 2016, 7, 31);
		semesters.add(s);
		
		s = DbSetupHelper.setupSemester(2016, 1, 2016, 10, 1, 2017, 2, 28);
		semesters.add(s);
		
		s = DbSetupHelper.setupSemester(2016, 2, 2017, 3, 1, 2017, 7, 31);
		semesters.add(s);
		
		s = DbSetupHelper.setupSemester(2017, 1, 2017, 10, 1, 2018, 2, 28);
		semesters.add(s);
		
		s = DbSetupHelper.setupSemester(2017, 2, 2018, 3, 1, 2018, 7, 31);
		semesters.add(s);
		
		s = DbSetupHelper.setupSemester(2018, 1, 2018, 10, 1, 2019, 2, 28);
		semesters.add(s);
		
		s = DbSetupHelper.setupSemester(2018, 2, 2019, 3, 1, 2019, 7, 31);
		semesters.add(s);
		
		s = DbSetupHelper.setupSemester(2019, 1, 2019, 10, 1, 2020, 2, 28);
		semesters.add(s);
		
		s = DbSetupHelper.setupSemester(2019, 2, 2020, 3, 1, 2020, 7, 31);
		semesters.add(s);
		
		s = DbSetupHelper.setupSemester(2020, 1, 2020, 10, 1, 2021, 2, 28);
		semesters.add(s);
		
		s = DbSetupHelper.setupSemester(2020, 2, 2021, 3, 1, 2021, 7, 31);
		semesters.add(s);
		
		DbSetupHelper.commitTransaction();
		
		return semesters;
	}
	
	private static void setupInstitution()
	{
		DbSetupHelper.startTransaction();
		
		Institution i = new Institution();
		i.setName(LocalizedString.fromStringFormat("ca#Universitat Oberta de Catalunya"));
		i.setMainSiteUrl("www.uoc.edu");
		
		em.persist(i);
		
		VLE vle1 = new VLE();
		
		vle1.setName(LocalizedString.fromStringFormat("ca#Campus virtual"));
		vle1.setUrl("www.uoc.edu");
		
		em.persist(vle1);
		
		DbSetupHelper.commitTransaction();
		
		DbSetupHelper.startTransaction();
		
		//		i.getVLEs().add(vle1);
		
		DbSetupHelper.commitTransaction();
	}
	
	private static List<Tool> setupTools() throws Exception
	{
		List<Tool> tools = new ArrayList<Tool>();
		
		tools.add(setupTool(LocalizedString.fromStringFormat("ca#Fake FAT"), "FAKEFAT", "afe9ead0-8a55-4bf7-bce9-f78e81afdecb"));
		tools.add(setupTool(LocalizedString.fromStringFormat("ca#VerilUOC"), "VERILUOC", "bbe72f18-2944-481b-a0cc-fb8318c9f065"));
		tools.add(setupTool(LocalizedString.fromStringFormat("ca#Alura"), "ALURA", "6186296d-2d49-481b-a15f-b26133c08756"));
		
		return tools;
	}
	
	private static List<Course> setupCourses(List<Subject> subjects, List<Semester> semesters) throws Exception
	{
		List<Course> courses = new ArrayList<Course>();
		
		User lect1 = setupUser("lect1", "Lecturer 1", "lect1", "lect1@uoc.edu", Arrays.asList(UserRole.LECTURER));
		User lect2 = setupUser("lect2", "Lecturer 2", "lect2", "lect2@uoc.edu", Arrays.asList(UserRole.LECTURER));
		User lect3 = setupUser("lect3", "Lecturer 3", "lect3", "lect3@uoc.edu", Arrays.asList(UserRole.LECTURER));
		
		courses.add(setupCourse(LocalizedString.fromStringFormat("en#Course 1a;ca#Curs 1a"), "C1a", s20151, "ca", subjects.get(0), lect1));
		courses.add(setupCourse(LocalizedString.fromStringFormat("en#Course 1b;ca#Curs 1b"), "C1b", s20151, "ca", subjects.get(0), lect1));
		courses.add(setupCourse(LocalizedString.fromStringFormat("en#Course 2;ca#Curs 2"), "C2", s20151, "ca", subjects.get(1), lect2));
		courses.add(setupCourse(LocalizedString.fromStringFormat("en#Course 3;ca#Curs 3"), "C3", s20151, "ca", subjects.get(2), lect3));

		return courses;
	}
	
	private static List<CourseGroup> setupGroups(List<Course> courses) throws Exception
	{
		List<CourseGroup> groups = new ArrayList<CourseGroup>();

		User inst1 = setupUser("inst1", "Instructor 1", "inst1", "inst1@uoc.edu", Arrays.asList(UserRole.INSTRUCTOR));
		User inst2 = setupUser("inst2", "Instructor 2", "inst2", "inst2@uoc.edu", Arrays.asList(UserRole.INSTRUCTOR));
		User inst3 = setupUser("inst3", "Instructor 3", "inst3", "inst3@uoc.edu", Arrays.asList(UserRole.INSTRUCTOR));
		User inst4 = setupUser("inst4", "Instructor 4", "inst4", "inst4@uoc.edu", Arrays.asList(UserRole.INSTRUCTOR));
		User inst5 = setupUser("inst5", "Instructor 5", "inst5", "inst5@uoc.edu", Arrays.asList(UserRole.INSTRUCTOR));
		User inst6 = setupUser("inst6", "Instructor 6", "inst6", "inst6@uoc.edu", Arrays.asList(UserRole.INSTRUCTOR));

		DbSetupHelper.startTransaction();

		groups.add(setupCourseGroup(LocalizedString.fromStringFormat("en#Course 1a - Room 1;ca#Curs 1a - Aula 1"), courses.get(0), inst1));
		groups.add(setupCourseGroup(LocalizedString.fromStringFormat("en#Course 1a - Room 2;ca#Curs 1a - Aula 2"), courses.get(0), inst2));
		groups.add(setupCourseGroup(LocalizedString.fromStringFormat("en#Course 1b - Room 1;ca#Curs 1b - Aula 1"), courses.get(1), inst1));
		groups.add(setupCourseGroup(LocalizedString.fromStringFormat("en#Course 1b - Room 2;ca#Curs 1b - Aula 2"), courses.get(1), inst3));

		groups.add(setupCourseGroup(LocalizedString.fromStringFormat("en#Course 2 - Room 1;ca#Curs 2 - Aula 1"), courses.get(2), inst4));
		groups.add(setupCourseGroup(LocalizedString.fromStringFormat("en#Course 2 - Room 2;ca#Curs 2 - Aula 2"), courses.get(2), inst5));
		groups.add(setupCourseGroup(LocalizedString.fromStringFormat("en#Course 3 - Room 1;ca#Curs 3 - Aula 1"), courses.get(3), inst6));

		DbSetupHelper.commitTransaction();
		
		return groups;
	}
	
	private static List<User> setupGroupMembers(List<CourseGroup> groups) throws Exception
	{
		List<User> students = new ArrayList<User>();
		
		User std1 = setupUser("std1", "Student 1", "std1", "std1@uoc.edu", Arrays.asList(UserRole.STUDENT));
		students.add(std1);
		User std2 = setupUser("std2", "Student 2", "std2", "std2@uoc.edu", Arrays.asList(UserRole.STUDENT));
		students.add(std2);
		User std3 = setupUser("std3", "Student 3", "std3", "std3@uoc.edu", Arrays.asList(UserRole.STUDENT));
		students.add(std3);
		User std4 = setupUser("std4", "Student 4", "std4", "std4@uoc.edu", Arrays.asList(UserRole.STUDENT));
		students.add(std4);
		User std5 = setupUser("std5", "Student 5", "std5", "std5@uoc.edu", Arrays.asList(UserRole.STUDENT));
		students.add(std5);
		User std6 = setupUser("std6", "Student 6", "std6", "std6@uoc.edu", Arrays.asList(UserRole.STUDENT));
		students.add(std6);
		User std7 = setupUser("std7", "Student 7", "std7", "std7@uoc.edu", Arrays.asList(UserRole.STUDENT));
		students.add(std7);
		User std8 = setupUser("std8", "Student 8", "std8", "std8@uoc.edu", Arrays.asList(UserRole.STUDENT));
		students.add(std8);
		User std9 = setupUser("std9", "Student 9", "std9", "std9@uoc.edu", Arrays.asList(UserRole.STUDENT));
		students.add(std9);
		User std10 = setupUser("std10", "Student 10", "std10", "std10@uoc.edu", Arrays.asList(UserRole.STUDENT));
		students.add(std10);
		User std11 = setupUser("std11", "Student 11", "std11", "std11@uoc.edu", Arrays.asList(UserRole.STUDENT));
		students.add(std11);
		User std12 = setupUser("std12", "Student 12", "std12", "std12@uoc.edu", Arrays.asList(UserRole.STUDENT));
		students.add(std12);
		
		DbSetupHelper.startTransaction();
		
		groups.get(0).getMembers().add(setupCourseGroupMember(std1));
		groups.get(0).getMembers().add(setupCourseGroupMember(std2));
		groups.get(0).getMembers().add(setupCourseGroupMember(std3));

		groups.get(1).getMembers().add(setupCourseGroupMember(std4));
		groups.get(1).getMembers().add(setupCourseGroupMember(std5));
		groups.get(1).getMembers().add(setupCourseGroupMember(std6));

		groups.get(2).getMembers().add(setupCourseGroupMember(std7));
		groups.get(2).getMembers().add(setupCourseGroupMember(std8));
		groups.get(2).getMembers().add(setupCourseGroupMember(std9));

		groups.get(3).getMembers().add(setupCourseGroupMember(std10));
		groups.get(3).getMembers().add(setupCourseGroupMember(std11));
		groups.get(3).getMembers().add(setupCourseGroupMember(std12));
		
		groups.get(4).getMembers().add(setupCourseGroupMember(std1));
		groups.get(4).getMembers().add(setupCourseGroupMember(std3));
		groups.get(4).getMembers().add(setupCourseGroupMember(std4));
		
		groups.get(5).getMembers().add(setupCourseGroupMember(std7));
		groups.get(5).getMembers().add(setupCourseGroupMember(std9));
		groups.get(5).getMembers().add(setupCourseGroupMember(std12));
		
		Set<User> team1 = new HashSet<User>();
		team1.add(std1);
		team1.add(std6);
		team1.add(std12);
		
		Set<User> team2 = new HashSet<User>();
		team2.add(std9);
		team2.add(std10);
		
		Set<User> team3 = new HashSet<User>();
		team3.add(std4);
		team3.add(std5);
		team3.add(std7);
		
		//		groups.get(6).getMembers().add(setupCourseGroupMember(team1));
		//		groups.get(6).getMembers().add(setupCourseGroupMember(team2));
		//		groups.get(6).getMembers().add(setupCourseGroupMember(team3));
		
		DbSetupHelper.commitTransaction();
		
		return students;
	}

	private static List<Subject> setupSubjects() throws Exception
	{
		List<Subject> subjects = new ArrayList<Subject>();
		
		subjects.add(setupSubject(LocalizedString.fromStringFormat("en#Subject 1;es#Asignatura 1;ca#Assignatura 1"), "A1"));
		subjects.add(setupSubject(LocalizedString.fromStringFormat("en#Subject 2"), "A2"));
		subjects.add(setupSubject(LocalizedString.fromStringFormat("en#Subject 3"), "A3"));
		
		return subjects;
	}
	
	private static List<Program> setupPrograms() throws Exception
	{
		List<Program> programs = new ArrayList<Program>();
		
		programs.add(setupProgram("P1", LocalizedString.fromStringFormat("en#Program 1;es#Programa 1;ca#Programa 1"), null));
		programs.add(setupProgram("P2", LocalizedString.fromStringFormat("en#Program 2"), null));
		programs.add(setupProgram("P3", LocalizedString.fromStringFormat("en#Program 3"), null));
		programs.add(setupProgram("GR05", LocalizedString.fromStringFormat("ca#Grau d'Enginyeria Informàtica"), null));
		
		return programs;
	}
	
	private static void setupPermissions() throws Exception
	{
		setupRolePermission("Program", "institution", UserRole.ADMIN, Arrays.asList(Permission.READ, Permission.EDIT));
		setupRolePermission("Program", "institution", UserRole.PROGRAM_MANAGER, Arrays.asList(Permission.READ));
		setupRolePermission("Program", "institution", UserRole.LECTURER, Arrays.asList(Permission.READ));
		setupRolePermission("Program", "institution", UserRole.INSTRUCTOR, Arrays.asList(Permission.READ));
		setupRolePermission("Program", "institution", UserRole.STUDENT, Arrays.asList(Permission.READ));
		
		setupRolePermission("Subject", "institution", UserRole.ADMIN, Arrays.asList(Permission.READ, Permission.EDIT));
		setupRolePermission("Subject", "institution", UserRole.PROGRAM_MANAGER, Arrays.asList(Permission.READ));
		setupRolePermission("Subject", "institution", UserRole.LECTURER, Arrays.asList(Permission.READ));
		setupRolePermission("Subject", "institution", UserRole.INSTRUCTOR, Arrays.asList(Permission.READ));
		setupRolePermission("Subject", "institution", UserRole.STUDENT, Arrays.asList(Permission.READ));
		
		setupRolePermission("Course", "institution", UserRole.ADMIN, Arrays.asList(Permission.READ, Permission.EDIT));
		setupRolePermission("Course", "institution", UserRole.PROGRAM_MANAGER, Arrays.asList(Permission.READ, Permission.EDIT));
		setupRolePermission("Course", "institution", UserRole.LECTURER, Arrays.asList(Permission.READ));
		setupRolePermission("Course", "institution", UserRole.INSTRUCTOR, Arrays.asList(Permission.READ));
		setupRolePermission("Course", "institution", UserRole.STUDENT, Arrays.asList(Permission.READ));
		
		setupRolePermission("Semester", "institution", UserRole.ADMIN, Arrays.asList(Permission.READ, Permission.EDIT));
		setupRolePermission("Semester", "institution", UserRole.PROGRAM_MANAGER, Arrays.asList(Permission.READ));
		setupRolePermission("Semester", "institution", UserRole.LECTURER, Arrays.asList(Permission.READ));
		setupRolePermission("Semester", "institution", UserRole.INSTRUCTOR, Arrays.asList(Permission.READ));
		setupRolePermission("Semester", "institution", UserRole.STUDENT, Arrays.asList(Permission.READ));
		
		setupRolePermission("CourseGroup", "institution", UserRole.ADMIN, Arrays.asList(Permission.READ, Permission.EDIT));
		setupRolePermission("CourseGroup", "institution", UserRole.PROGRAM_MANAGER, Arrays.asList(Permission.READ));
		setupRolePermission("CourseGroup", "institution", UserRole.LECTURER, Arrays.asList(Permission.READ, Permission.EDIT));
		setupRolePermission("CourseGroup", "institution", UserRole.INSTRUCTOR, Arrays.asList(Permission.READ));
		setupRolePermission("CourseGroup", "institution", UserRole.STUDENT, Arrays.asList(Permission.READ));
		
		setupRolePermission("Institution", "institution", UserRole.ADMIN, Arrays.asList(Permission.READ, Permission.EDIT));
		setupRolePermission("Institution", "institution", UserRole.PROGRAM_MANAGER, Arrays.asList(Permission.READ));
		setupRolePermission("Institution", "institution", UserRole.LECTURER, Arrays.asList(Permission.READ));
		setupRolePermission("Institution", "institution", UserRole.INSTRUCTOR, Arrays.asList(Permission.READ));
		
		setupRolePermission("VLE", "institution", UserRole.ADMIN, Arrays.asList(Permission.READ, Permission.EDIT));
		setupRolePermission("VLE", "institution", UserRole.PROGRAM_MANAGER, Arrays.asList(Permission.READ));
		setupRolePermission("VLE", "institution", UserRole.LECTURER, Arrays.asList(Permission.READ));
		setupRolePermission("VLE", "institution", UserRole.INSTRUCTOR, Arrays.asList(Permission.READ));
		
		setupRolePermission("Tool", "ela", UserRole.ADMIN, Arrays.asList(Permission.READ, Permission.EDIT));
		
		// Security
		
		//
		// Page permissions
		//
		
		Page p = setupPage("PrivateIndex", "Admin", "core", LocalizedString.fromStringFormat("en#Index"), "/private/index.xhtml", "", -1, false);
		
		setupRolePageAccess(p, UserRole.ADMIN);
		setupRolePageAccess(p, UserRole.PROGRAM_MANAGER);
		setupRolePageAccess(p, UserRole.LECTURER);
		setupRolePageAccess(p, UserRole.INSTRUCTOR);
		setupRolePageAccess(p, UserRole.STUDENT);
		
		p = setupPage("ProgramsList", "Admin", "institution",
				LocalizedString.fromStringFormat("en#Programs list;ca#Llista de programes;es#Lista de programas"), "/private/ProgramsList.xhtml",
				"fa fa-user-plus", 0, true);
		
		setupRolePageAccess(p, UserRole.ADMIN);
		setupRolePageAccess(p, UserRole.PROGRAM_MANAGER);
		
		p = setupPage("EditProgram", "Admin", "institution",
				LocalizedString.fromStringFormat("en#Edit program;ca#Editar programa;es#Editar programa"), "/private/EditProgram.xhtml",
				"", -1, false);
		
		setupRolePageAccess(p, UserRole.ADMIN);
		
		p = setupPage("SubjectsList", "Admin", "institution",
				LocalizedString.fromStringFormat("en#Subjects list;ca#Llista d'assignatures;es#Lista de asignaturas"), "/private/SubjectsList.xhtml",
				"fa fa-user-plus", 0, true);
		
		setupRolePageAccess(p, UserRole.ADMIN);
		setupRolePageAccess(p, UserRole.PROGRAM_MANAGER);
		setupRolePageAccess(p, UserRole.LECTURER);
		setupRolePageAccess(p, UserRole.INSTRUCTOR);
		
		p = setupPage("EditSubject", "Admin", "institution",
				LocalizedString.fromStringFormat("en#Edit subject;ca#Editar assignatura;es#Editar asignatura"), "/private/EditSubject.xhtml", "", -1,
				false);
				
		setupRolePageAccess(p, UserRole.ADMIN);
		
		p = setupPage("CoursesList", "Admin", "institution",
				LocalizedString.fromStringFormat("en#Courses list;ca#Llista de cursos;es#Lista de cursos"), "/private/CoursesList.xhtml",
				"fa fa-user-plus", 0, true);
				
		setupRolePageAccess(p, UserRole.ADMIN);
		setupRolePageAccess(p, UserRole.PROGRAM_MANAGER);
		setupRolePageAccess(p, UserRole.LECTURER);
		setupRolePageAccess(p, UserRole.INSTRUCTOR);
		
		p = setupPage("EditCourse", "Admin", "institution", LocalizedString.fromStringFormat("en#Edit course;ca#Editar curs;es#Editar curso"),
				"/private/EditCourse.xhtml", "", -1, false);
				
		setupRolePageAccess(p, UserRole.ADMIN);
		setupRolePageAccess(p, UserRole.PROGRAM_MANAGER);
		
		p = setupPage("CourseGroupsList", "Admin", "institution",
				LocalizedString.fromStringFormat("en#Groups list;ca#Llista de grups;es#Lista de grupos"), "/private/CourseGroupsList.xhtml",
				"fa fa-user-plus", 0, true);
				
		setupRolePageAccess(p, UserRole.ADMIN);
		setupRolePageAccess(p, UserRole.PROGRAM_MANAGER);
		setupRolePageAccess(p, UserRole.LECTURER);
		setupRolePageAccess(p, UserRole.INSTRUCTOR);
		
		p = setupPage("EditCourseGroup", "Admin", "institution", LocalizedString.fromStringFormat("en#Edit group;ca#Editar grup;es#Editar grupo"),
				"/private/EditGroup.xhtml", "", -1, false);
				
		setupRolePageAccess(p, UserRole.ADMIN);
		setupRolePageAccess(p, UserRole.LECTURER);
		
		p = setupPage("EditInstitution", "Admin", "institution",
				LocalizedString.fromStringFormat("en#Institution data;ca#Dades institució;es#Datos institución"), "/private/EditInstitution.xhtml",
				"fa fa-user-plus", 0, true);
				
		//		setupRolePageAccess(p, UserRole.ADMIN);
		
		p = setupPage("VLEsList", "Admin", "institution", LocalizedString.fromStringFormat("en#VLEs list;ca#Llista de VLEs;es#Lista de VLEs"),
				"/private/VLEsList.xhtml", "fa fa-user-plus", 0, true);
				
		setupRolePageAccess(p, UserRole.ADMIN);
		setupRolePageAccess(p, UserRole.PROGRAM_MANAGER);
		setupRolePageAccess(p, UserRole.LECTURER);
		setupRolePageAccess(p, UserRole.INSTRUCTOR);
		
		p = setupPage("EditVLE", "Admin", "institution", LocalizedString.fromStringFormat("en#Edit VLE;ca#Editar VLE;es#Editar VLE"),
				"/private/EditVLE.xhtml", "", -1, false);
				
		setupRolePageAccess(p, UserRole.ADMIN);
		
		p = setupPage("ToolsList", "Admin", "ela",
				LocalizedString.fromStringFormat("en#Tools list;ca#Llista d'eines;es#Lista de herramientas"),
				"/private/ToolsList.xhtml", "fa fa-user-plus", 0, true);
				
		setupRolePageAccess(p, UserRole.ADMIN);
		
		p = setupPage("EditTool", "Admin", "ela", LocalizedString.fromStringFormat("en#Edit Tool;ca#Editar eina;es#Editar herramienta"),
				"/private/EditTool.xhtml", "", -1, false);
				
		setupRolePageAccess(p, UserRole.ADMIN);
		
		p = setupPage("AssignmentAssessment", "Admin", "ela",
				LocalizedString.fromStringFormat("en#Assignment assessment;ca#Avaluació activitat;es#Evaluación actividad"),
				"/private/AssignmentAssessment.xhtml", "fa fa-user-plus", 0, true);
				
		setupRolePageAccess(p, UserRole.ADMIN);
		setupRolePageAccess(p, UserRole.INSTRUCTOR);
		
		p = setupPage("CoursesReport", "Reports", "ela",
				LocalizedString.fromStringFormat("en#Courses report;ca#Informe assignatures;es#Informe asignaturas"),
				"/private/CoursesReport.xhtml", "fa fa-bar-chart", 0, true);
				
		setupRolePageAccess(p, UserRole.ADMIN);
		setupRolePageAccess(p, UserRole.PROGRAM_MANAGER);
		setupRolePageAccess(p, UserRole.LECTURER);
		setupRolePageAccess(p, UserRole.INSTRUCTOR);
		
		p = setupPage("AssignmentsReport", "Reports", "ela",
				LocalizedString.fromStringFormat("en#Assignments report;ca#Informe activitats;es#Informe actividades"),
				"/private/AssignmentsReport.xhtml", "fa fa-bar-chart", 0, true);
		
		setupRolePageAccess(p, UserRole.ADMIN);
		setupRolePageAccess(p, UserRole.PROGRAM_MANAGER);
		setupRolePageAccess(p, UserRole.LECTURER);
		setupRolePageAccess(p, UserRole.INSTRUCTOR);
		setupRolePageAccess(p, UserRole.STUDENT);
		
		p = setupPage("ExercisesReport", "Reports", "ela",
				LocalizedString.fromStringFormat("en#Exercises report;ca#Informe exercicis;es#Informe ejercicios"), "/private/ExercisesReport.xhtml",
				"fa fa-bar-chart", 0, true);
				
		setupRolePageAccess(p, UserRole.ADMIN);
		setupRolePageAccess(p, UserRole.PROGRAM_MANAGER);
		setupRolePageAccess(p, UserRole.LECTURER);
		setupRolePageAccess(p, UserRole.INSTRUCTOR);
		
		p = setupPage("StudentsReport", "Reports", "ela",
				LocalizedString.fromStringFormat("en#Students report;ca#Informe estudiants;es#Informe estudiantes"), "/private/StudentsReport.xhtml",
				"fa fa-bar-chart", 0, true);
		
		setupRolePageAccess(p, UserRole.ADMIN);
		setupRolePageAccess(p, UserRole.PROGRAM_MANAGER);
		setupRolePageAccess(p, UserRole.LECTURER);
		setupRolePageAccess(p, UserRole.INSTRUCTOR);
		
		p = setupPage("EtlProcess", "Admin", "ela", LocalizedString.fromStringFormat("en#ETL process;ca#Procés ETL;es#Proceso ETL"),
				"/private/EtlProcess.xhtml", "fa fa-cog", 0, true);
	}	
}
