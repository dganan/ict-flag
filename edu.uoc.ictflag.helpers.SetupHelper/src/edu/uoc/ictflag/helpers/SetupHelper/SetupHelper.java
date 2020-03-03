package edu.uoc.ictflag.helpers.SetupHelper;

import static edu.uoc.ictflag.setup.core.DbSetupHelper.setupUser;

import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import edu.uoc.ictflag.core.localization.LocalizedString;
import edu.uoc.ictflag.institution.dal.CourseRepository;
import edu.uoc.ictflag.institution.dal.SemesterRepository;
import edu.uoc.ictflag.institution.dal.SubjectRepository;
import edu.uoc.ictflag.institution.model.Course;
import edu.uoc.ictflag.institution.model.Semester;
import edu.uoc.ictflag.institution.model.Subject;
import edu.uoc.ictflag.security.dal.PasswordHasher;
import edu.uoc.ictflag.security.dal.UserRepository;
import edu.uoc.ictflag.security.model.SecurePassword;
import edu.uoc.ictflag.security.model.User;
import edu.uoc.ictflag.security.model.UserRole;
import edu.uoc.ictflag.security.model.UserSecurePassword;
import edu.uoc.ictflag.setup.core.DbSetupHelper;

public class SetupHelper
{
	private static EntityManager em;
	
	public static void main(String[] args)
	{
		try
		{
			SetupDavidBaneresUser2();
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
		
		DbSetupHelper.commitTransaction();
	}
	
	private static void SetupDavidBaneresUser2() throws Exception
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("edu.uoc.ictflag.helpers.SetupHelper");
		
		em = emf.createEntityManager();
		
		DbSetupHelper.setEntityManager(em);
		
		setupUser("dbaneres2", "David Bañeres", "dbaneres16", "dbaneres@uoc.edu", Arrays.asList(UserRole.INSTRUCTOR, UserRole.STUDENT));
		
		DbSetupHelper.startTransaction();
	}
	
	private static void SetupDavidBaneresUser() throws Exception
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("edu.uoc.ictflag.helpers.SetupHelper");
			
		em = emf.createEntityManager();
			
		DbSetupHelper.setEntityManager(em);
		
		DbSetupHelper.startTransaction();
		
		UserRepository ur = new UserRepository();
		SemesterRepository semr = new SemesterRepository(ur);
		SubjectRepository sr = new SubjectRepository(ur);
		CourseRepository cr = new CourseRepository(ur);
		
		ur.setEntityManager(DbSetupHelper.getEntityManager());
		semr.setEntityManager(DbSetupHelper.getEntityManager());
		sr.setEntityManager(DbSetupHelper.getEntityManager());
		cr.setEntityManager(DbSetupHelper.getEntityManager());

		User dbaneres = ur.getUserByUsername("dbaneres");

		dbaneres.setName("David Bañeres");
		dbaneres.setEmail("dbaneres@uoc.edu");
		
		SecurePassword sp = PasswordHasher.getSecurePassword("dbaneres16");
		
		UserSecurePassword usp = DbSetupHelper.getDbHelper().getFirst("SELECT sp FROM UserSecurePassword sp WHERE sp.userId = " + dbaneres.getId(),
				UserSecurePassword.class);
		
		usp.setUserId(dbaneres.getId());
		usp.setPassword(sp);
		
		DbSetupHelper.getDbHelper().updateEntity(usp);

		dbaneres.getRoles().add(UserRole.LECTURER);
		
		Semester s20141 = semr.getSemesterByCode("2014-1");
		Semester s20142 = semr.getSemesterByCode("2014-2");
		
		Subject fc = sr.getSubjectByCode("05.562");
		
		fc.setName(LocalizedString.fromStringFormat("ca#Fonaments de computadors"));
		
		Course fc1 = cr.getCourse(fc, s20141);
		Course fc2 = cr.getCourse(fc, s20142);
		
		fc1.setLecturer(dbaneres);
		fc2.setLecturer(dbaneres);
		
		DbSetupHelper.commitTransaction();
	}
}
