package edu.uoc.ictflag.institution.dal.test;

import static edu.uoc.ictflag.setup.core.DbSetupHelper.commitTransaction;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.getEntityManager;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.setupProgram;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.setupSubject;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.setupUser;
import static edu.uoc.ictflag.setup.core.DbSetupHelper.startTransaction;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.uoc.ictflag.core.dal.DBHelper;
import edu.uoc.ictflag.core.localization.LocalizedString;
import edu.uoc.ictflag.dal.test.DBTestHelper;
import edu.uoc.ictflag.institution.dal.ProgramRepository;
import edu.uoc.ictflag.institution.dal.SubjectRepository;
import edu.uoc.ictflag.institution.model.Program;
import edu.uoc.ictflag.institution.model.Subject;
import edu.uoc.ictflag.security.dal.UserRepository;
import edu.uoc.ictflag.security.model.User;
import edu.uoc.ictflag.security.model.UserRole;

public class ProgramRepositoryTest
{
	private static List<Program> programs;
	private static ProgramRepository pr;
	private static UserRepository ur;
	
	private static Subject s1;
	private static Subject s2;
	private static Subject s3;
	
	private static User pm1;
	private static User pm2;
	
	@BeforeClass
	public static void setUpProgramRepositoryTest() throws Exception
	{
		DBTestHelper.reset();
		
		ur = new UserRepository();
		ur.setEntityManager(getEntityManager());
		
		pr = new ProgramRepository(ur);
		pr.setEntityManager(getEntityManager());
		
		setupUser("su", "Super user", "su", "su@uoc.edu", Arrays.asList(UserRole.SUPERUSER));
		pm1 = setupUser("pm1", "Program manager 1", "pm1", "pm1@uoc.edu", Arrays.asList(UserRole.PROGRAM_MANAGER));
		pm2 = setupUser("pm2", "Program manager 2", "pm2", "pm2@uoc.edu", Arrays.asList(UserRole.PROGRAM_MANAGER));
		
		programs = new ArrayList<Program>();
		
		programs.add(setupProgram("P1", LocalizedString.fromStringFormat("ca#Programa 1;en#Program 1"), pm1));
		programs.add(setupProgram("P2", LocalizedString.fromStringFormat("ca#Programa 2;en#Program 2"), pm1));
		programs.add(setupProgram("P3", LocalizedString.fromStringFormat("ca#Programa 3;en#Program 3"), pm2));
		programs.add(setupProgram("P4", LocalizedString.fromStringFormat("ca#Programa 4;en#Program 4"), null));
		
		s1 = setupSubject(LocalizedString.fromStringFormat("ca#Assignatura 1;en#Subject 1"), "12.000");
		s2 = setupSubject(LocalizedString.fromStringFormat("ca#Assignatura 2;en#Subject 2"), "14.000");
		s3 = setupSubject(LocalizedString.fromStringFormat("ca#Assignatura 3;en#Subject 3"), "16.000");
		
		startTransaction();
		
		programs.get(0).getSubjects().add(s1);
		programs.get(0).getSubjects().add(s3);
		programs.get(1).getSubjects().add(s3);
		programs.get(2).getSubjects().add(s1);
		programs.get(2).getSubjects().add(s2);
		
		pr.createOrUpdateProgram(programs.get(0));
		pr.createOrUpdateProgram(programs.get(1));
		pr.createOrUpdateProgram(programs.get(2));
		
		commitTransaction();
	}
	
	@Test
	public void getProgramsListTest() throws Exception
	{
		List<Program> ps = pr.getProgramsList("su");
		
		assertEquals(4, ps.size());
		
		for (Program p : ps)
		{
			assertTrue(p.getId() == programs.get(0).getId() || p.getId() == programs.get(1).getId() || p.getId() == programs.get(2).getId()
					|| p.getId() == programs.get(3).getId());
		}
		
		ps = pr.getProgramsList("pm1");
		
		assertEquals(2, ps.size());
		
		for (Program p : ps)
		{
			assertTrue(p.getId() == programs.get(0).getId() || p.getId() == programs.get(1).getId());
		}
		
		// gets only the Programs tied to the programManager 2
		
		ps = pr.getProgramsList("pm2");
		
		assertEquals(1, ps.size());
		
		for (Program p : ps)
		{
			assertTrue(p.equals(programs.get(2)));
		}
	}
	
	@Test
	public void getProgram() throws Exception
	{
		Program p = pr.getProgram("su", programs.get(0).getId());
		
		assertEquals(pm1.getId(), p.getProgramManager().getId());
		
		assertEquals(2, p.getSubjects().size());
		
		for (Subject s : p.getSubjects())
		{
			assertTrue(s.getId() == s1.getId() || s.getId() == s3.getId());
		}
		
		p = pr.getProgram("pm1", programs.get(0).getId());
		
		assertNotNull(p);
		
		p = pr.getProgram("pm1", programs.get(3).getId());
		
		assertNull(p);
	}
	
	@Test
	public void deleteProgramTest() throws Exception
	{
		Program p = setupProgram("P4", LocalizedString.fromStringFormat("ca#Programa 4;en#Program 4"), null);
		
		startTransaction();
		
		p.getSubjects().add(s1);
		p.getSubjects().add(s3);
		
		pr.createOrUpdateProgram(p);
		
		commitTransaction();
		
		startTransaction();
		
		Long id = p.getId();
		
		pr.deleteProgram(id);
		
		commitTransaction();
		
		assertNull(pr.getProgram("su", id));
		
		DBHelper h = new DBHelper(getEntityManager());
		
		ArrayList<Object> parameters = new ArrayList<Object>();
		
		parameters.add(id);
		
		long count = (long) h.getScalarValueNative("SELECT count(*) FROM program_subject ps WHERE ps.program_id = ?", parameters);
		
		assertEquals(0, count);
		
		SubjectRepository sr = new SubjectRepository(ur);
		sr.setEntityManager(getEntityManager());
		
		assertNotNull(sr.getSubject("su", s1.getId()));
		assertNotNull(sr.getSubject("su", s3.getId()));
	}
	
	@Test
	public void updateProgramTest() throws Exception
	{
		startTransaction();
		
		Program p = programs.get(0);
		
		p.getSubjects().add(s2);
		pr.createOrUpdateProgram(p);
		
		commitTransaction();
		
		assertEquals(3, pr.getProgram("su", p.getId()).getSubjects().size());
		
		startTransaction();
		
		p.getSubjects().add(s2);
		pr.createOrUpdateProgram(p);
		
		commitTransaction();
		
		assertEquals(3, pr.getProgram("su", p.getId()).getSubjects().size());
		
		startTransaction();
		
		p.getSubjects().remove(s2);
		pr.createOrUpdateProgram(p);
		
		commitTransaction();
		
		assertEquals(2, pr.getProgram("su", programs.get(0).getId()).getSubjects().size());
	}
}
