package edu.uoc.ictflag.setup.core;

import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import edu.uoc.ictflag.core.dal.DBHelper;
import edu.uoc.ictflag.core.localization.LocalizedString;
import edu.uoc.ictflag.ela.model.Assignment;
import edu.uoc.ictflag.ela.model.AssignmentData;
import edu.uoc.ictflag.ela.model.Exercise;
import edu.uoc.ictflag.ela.model.ExerciseData;
import edu.uoc.ictflag.ela.model.Outcome;
import edu.uoc.ictflag.ela.model.Tool;
import edu.uoc.ictflag.institution.model.Course;
import edu.uoc.ictflag.institution.model.CourseGroup;
import edu.uoc.ictflag.institution.model.CourseGroupMember;
import edu.uoc.ictflag.institution.model.Program;
import edu.uoc.ictflag.institution.model.Semester;
import edu.uoc.ictflag.institution.model.Subject;
import edu.uoc.ictflag.security.dal.PasswordHasher;
import edu.uoc.ictflag.security.model.Page;
import edu.uoc.ictflag.security.model.Permission;
import edu.uoc.ictflag.security.model.SecurePassword;
import edu.uoc.ictflag.security.model.User;
import edu.uoc.ictflag.security.model.UserRole;
import edu.uoc.ictflag.security.model.UserRolePageAccess;
import edu.uoc.ictflag.security.model.UserRolePermissions;
import edu.uoc.ictflag.security.model.UserSecurePassword;

public class DbSetupHelper
{
	public static DBHelper getDbHelper()
	{
		return dbHelper;
	}
	
	private static DBHelper dbHelper;
	private static EntityManager em;
	
	public static void setEntityManager(EntityManager em)
	{
		DbSetupHelper.em = em;
		dbHelper = new DBHelper(em);
	}
	
	public static EntityManager getEntityManager()
	{
		return em;
	}
	
	//	public static EventData setupEventData(Tool tool, User person, Program program, Subject subject, Semester semester, Date timestamp,
	//			Action action, ElementType elementType, String element, ResultType resultType, String result) throws Exception
	//	{
	//		startTransaction();
	//		
	//		EventData ad = new EventData();
	//		
	//		ad.setTool(tool);
	//		ad.setPerson(person);
	//		ad.setProgram(program);
	//		ad.setSubject(subject);
	//		ad.setSemester(semester);
	//		ad.setAction(action);
	//		ad.setElement(element);
	//		ad.setElementType(elementType);
	//		
	//		ad.setResultType(resultType);
	//		ad.setResult(result);
	//		ad.setAssignmentDate(timestamp);
	//		
	//		dbHelper.addEntity(ad);
	//		
	//		commitTransaction();
	//		
	//		return ad;
	//	}
	
	public static void createDataRawTables()
	{
		Query q = DbSetupHelper.getEntityManager().createNativeQuery(
				"CREATE TABLE IF NOT EXISTS assignmentDataRaw(id serial NOT NULL, tool text, toolUUID text, userId text, timestamp text, programCode text, subjectCode text, semester text, action text, assignment text, grade text, handled boolean DEFAULT FALSE NOT NULL, CONSTRAINT assignmentDataRaw_pkey PRIMARY KEY (id));");
				
		q.executeUpdate();
		
		q = DbSetupHelper.getEntityManager().createNativeQuery(
				"CREATE TABLE IF NOT EXISTS exerciseDataRaw(id serial NOT NULL, tool text, toolUUID text, userId text, timestamp text, programCode text, subjectCode text, semester text, action text, assignment text, exercise text, outcome text, grade text, handled boolean DEFAULT FALSE NOT NULL, CONSTRAINT exercisetDataRaw_pkey PRIMARY KEY (id));");
		
		q.executeUpdate();
		
		q = DbSetupHelper.getEntityManager().createNativeQuery(
				"CREATE INDEX IF NOT EXISTS exercisedataraw_find_unhandled ON exercisedataraw USING btree (handled, \"timestamp\" COLLATE pg_catalog.\"default\");");
		
		q.executeUpdate();
		
		q = DbSetupHelper.getEntityManager().createNativeQuery(
				"CREATE INDEX IF NOT EXISTS assignmentdataraw_find_unhandled ON assignmentdataraw USING btree (handled, \"timestamp\" COLLATE pg_catalog.\"default\");");
				
		q.executeUpdate();
	}
	
	public static void emptyDataRawTables()
	{
		Query q = DbSetupHelper.getEntityManager().createNativeQuery("DELETE FROM assignmentDataRaw;");
		
		q.executeUpdate();
		
		q = DbSetupHelper.getEntityManager().createNativeQuery("DELETE FROM exerciseDataRaw;");
		
		q.executeUpdate();
	}
	
	public static Tool setupTool(LocalizedString name, String code, String uuid) throws Exception
	{
		startTransaction();
		
		Tool t = new Tool();
		
		t.setName(name);
		t.setCode(code);
		t.setUuid(uuid);
		
		dbHelper.addEntity(t);
		
		commitTransaction();
		
		return t;
	}
	
	public static User setupUser(String username, String name, String password, String email, List<UserRole> roles) throws Exception
	{
		return setupUser(username, name, password, email, roles, "en");
	}
	
	public static User setupUser(String username, String name, String password, String email, List<UserRole> roles, String language) throws Exception
	{
		startTransaction();
		
		User u = new User();
		
		u.setName(name);
		
		u.setUsername(username);
		
		u.setEmail(email);
		
		u.setRoles(roles);
		
		u.setSelectedRole(roles.get(0));
		
		u.setSelectedLanguage(language);
		
		dbHelper.addEntity(u);
		
		commitTransaction();
		
		startTransaction();
		
		SecurePassword sp = PasswordHasher.getSecurePassword(password);
		
		UserSecurePassword usp = new UserSecurePassword();
		usp.setUserId(u.getId());
		usp.setPassword(sp);
		
		dbHelper.addEntity(usp);
		
		commitTransaction();
		
		return u;
	}
	
	public static void changeUserPassword(String username, String newpassword) throws Exception
	{
		startTransaction();
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		parameters.put("username", username);
		
		User u = dbHelper.getFirst("SELECT u FROM User u WHERE u.username = :username", User.class, parameters);
		
		SecurePassword sp = PasswordHasher.getSecurePassword(newpassword);
		
		parameters = new HashMap<String, Object>();
		parameters.put("userid", u.getId());
		
		UserSecurePassword usp = dbHelper.getFirst("SELECT usp FROM UserSecurePassword usp WHERE usp.userId = :userid", UserSecurePassword.class,
				parameters);
		
		usp.setUserId(u.getId());
		usp.setPassword(sp);
		
		dbHelper.updateEntity(usp);
		
		commitTransaction();
	}
	
	public static Program setupProgram(String code, LocalizedString name, User programManager) throws Exception
	{
		startTransaction();
		
		Program p = new Program();
		p.setCode(code);
		p.setName(name);
		p.setProgramManager(programManager);
		dbHelper.addEntity(p);
		
		commitTransaction();
		
		return p;
	}
	
	public static Subject setupSubject(LocalizedString name, String code) throws Exception
	{
		startTransaction();
		
		Subject s = new Subject();
		s.setName(name);
		s.setCode(code);
		dbHelper.addEntity(s);
		
		commitTransaction();
		
		return s;
	}
	
	public static Course setupCourse(LocalizedString name, String code, Semester semester, String language, Subject subject,
			User lecturer)
			throws Exception
	{
		startTransaction();
		
		Course c = new Course();
		c.setCode(code);
		c.setName(name);
		c.setSemester(semester);
		c.setLanguage(language);
		c.setSubject(subject);
		c.setLecturer(lecturer);
		
		dbHelper.addEntity(c);
		
		commitTransaction();
		
		return c;
	}
	
	public static CourseGroup setupCourseGroup(LocalizedString name, Course course, User instructor) throws Exception
	{
		startTransaction();
		
		CourseGroup cg = new CourseGroup();
		cg.setName(name);
		cg.setCourse(course);
		cg.setInstructor(instructor);
		
		dbHelper.addEntity(cg);
		
		commitTransaction();
		
		return cg;
	}
	
	public static CourseGroupMember setupCourseGroupMember(User user) throws Exception
	{
		startTransaction();
		
		CourseGroupMember cgm = new CourseGroupMember();
		cgm.setUser(user);
		
		dbHelper.addEntity(cgm);
		
		commitTransaction();
		
		return cgm;
	}
	
	//	public static CourseGroupMember setupCourseGroupMember(Set<User> users) throws Exception
	//	{
	//		startTransaction();
	//		
	//		// ESTO SE TIENE QUE HACER CON Team
	//		
	//		CourseGroupMember cgm = new CourseGroupMember();
	//		cgm.setUser(users);
	//		
	//		dbHelper.addEntity(cgm);
	//		
	//		commitTransaction();
	//		
	//		return cgm;
	//	}
	
	public static void setupRolePermission(String entity, String module, UserRole role, List<Permission> permissions) throws Exception
	{
		startTransaction();
		
		UserRolePermissions up = new UserRolePermissions();
		
		up.setEntity(entity);
		up.setModule(module);
		up.setRole(role);
		up.setPermissions(permissions);
		dbHelper.addEntity(up);
		
		commitTransaction();
	}
	
	public static Page setupPage(String uniqueName, String section, String module, LocalizedString displayName, String relativeUrl, String icon,
			int order, boolean inMenu) throws Exception
	{
		startTransaction();
		
		Page p = new Page();
		
		p.setUniqueName(uniqueName);
		p.setSection(section);
		p.setModule(module);
		p.setDisplayName(displayName);
		p.setRelativeUrl(relativeUrl);
		p.setIconClass(icon);
		p.setOrderInMenu(order);
		p.setInMenu(inMenu);
		
		dbHelper.addEntity(p);
		
		commitTransaction();
		
		return p;
	}
	
	public static void setupRolePageAccess(Page p, UserRole role) throws Exception
	{
		startTransaction();
		
		UserRolePageAccess up = new UserRolePageAccess();
		
		up.setPage(p);
		up.setRole(role);
		
		dbHelper.addEntity(up);
		
		commitTransaction();
	}
	
	public static void startTransaction()
	{
		if (!em.getTransaction().isActive())
		{
			em.getTransaction().begin();
		}
	}
	
	public static void commitTransaction()
	{
		if (em.getTransaction().isActive())
		{
			em.getTransaction().commit();
		}
	}
	
	public static void clearContext()
	{
		em.clear();
	}
	
	public static Semester setupSemester(int academicYear, int number) throws Exception
	{
		startTransaction();
		
		Semester s = new Semester();
		s.setAcademicYear(academicYear);
		s.setNumber(number);
		
		dbHelper.addEntity(s);
		
		commitTransaction();
		
		return s;
	}
	
	public static Semester setupSemester(int academicYear, int number, int startYear, int startMonth, int startDay, int endYear, int endMonth,
			int endDay) throws Exception
	{
		Semester s = new Semester();
		s.setAcademicYear(academicYear);
		s.setNumber(number);
		s.setStartDate(new GregorianCalendar(startYear, startMonth - 1, startDay).getTime());
		s.setEndDate(new GregorianCalendar(endYear, endMonth - 1, endDay).getTime());
		
		dbHelper.addEntity(s);
		
		return s;
	}
	
	public static void setupExerciseData(Tool tool, User student, Program program, Subject subject, Course course, CourseGroup courseGroup,
			Assignment assignment, Exercise exercise, Outcome outcome, double grade, Date startDate, Date endDate, Date assessmentDate,
			long duration_sec, long timeFromLastAttempt_sec, int attemptNumber) throws Exception
	{
		startTransaction();
		
		ExerciseData ed = new ExerciseData();
		
		ed.setTool(tool);
		ed.setStudent(student);
		ed.setProgram(program);
		ed.setSubject(subject);
		ed.setCourse(course);
		ed.setSemester(course.getSemester());
		ed.setCourseGroup(courseGroup);
		ed.setAssignment(assignment);
		ed.setExercise(exercise);
		ed.setOutcome(outcome);
		ed.setGrade(grade);
		ed.setStartDate(startDate);
		ed.setEndDate(endDate);
		ed.setAssessmentDate(assessmentDate);
		ed.setDuration_sec(duration_sec);
		ed.setTimeFromLastAttempt_sec(timeFromLastAttempt_sec);
		ed.setAttemptNumber(attemptNumber);
		
		dbHelper.addEntity(ed);
		
		commitTransaction();
	}
	
	public static void setupAssignmentData(Tool tool, User student, Program program, Subject subject, Course course, CourseGroup courseGroup,
			Assignment assignment, double grade, Date startDate, Date endDate, Date assessmentDate, long duration_sec) throws Exception
	{
		startTransaction();
		
		AssignmentData ad = new AssignmentData();
		
		ad.setTool(tool);
		ad.setStudent(student);
		ad.setProgram(program);
		ad.setSubject(subject);
		ad.setCourse(course);
		ad.setSemester(course.getSemester());
		ad.setCourseGroup(courseGroup);
		ad.setAssignment(assignment);
		ad.setGrade(grade);
		ad.setStartDate(startDate);
		ad.setEndDate(endDate);
		ad.setAssessmentDate(assessmentDate);
		ad.setDuration_sec(duration_sec);
		
		dbHelper.addEntity(ad);
		
		commitTransaction();
	}
	
	public static Exercise setupExercise(Tool tool, String name) throws Exception
	{
		startTransaction();
		
		Exercise ex = new Exercise();
		ex.setName(name);
		ex.setTool(tool);
		
		dbHelper.addEntity(ex);
		
		commitTransaction();
		
		return ex;
	}
	
	public static Assignment setupAssignment(Course course, String name, Exercise... exercises) throws Exception
	{
		startTransaction();
		
		Assignment a = new Assignment();
		a.setName(name);
		a.setCourse(course);
		a.getExercises().addAll(Arrays.asList(exercises));
		
		dbHelper.addEntity(a);
		
		commitTransaction();
		
		return a;
	}
}
