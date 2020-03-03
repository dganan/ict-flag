package edu.uoc.ictflag.institution.bll.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import edu.com.ictflag.security.bll.AuthorizationManager;
import edu.com.ictflag.security.bll.IAuthorizationManager;
import edu.uoc.ictflag.core.IctFlagPermissionDeniedException;
import edu.uoc.ictflag.institution.bll.ProgramManager;
import edu.uoc.ictflag.institution.dal.IProgramRepository;
import edu.uoc.ictflag.institution.dal.ProgramRepository;
import edu.uoc.ictflag.institution.model.Program;
import edu.uoc.ictflag.security.dal.IUserRepository;
import edu.uoc.ictflag.security.dal.UserRepository;
import edu.uoc.ictflag.security.model.Permission;
import edu.uoc.ictflag.security.model.User;
import edu.uoc.ictflag.security.model.UserRole;

@RunWith(MockitoJUnitRunner.class)
public class ProgramManagerTest
{
	IProgramRepository programRepository;
	IAuthorizationManager authorizationManager;
	IUserRepository userRepository;
	
	ProgramManager manager;
	
	static List<Program> programs0 = new ArrayList<Program>();
	
	static List<Program> programs1 = new ArrayList<Program>();
	
	static List<Program> programs2 = new ArrayList<Program>();
	
	static List<Program> programs3 = new ArrayList<Program>();
	
	static
	{
		programs1.add(new Program());
		
		programs2.add(new Program());
		programs2.add(new Program());
		
		programs3.add(new Program());
		programs3.add(new Program());
		programs3.add(new Program());
	}
	
	private void setupMocks() throws Exception
	{
		programRepository = mock(ProgramRepository.class);
		authorizationManager = mock(AuthorizationManager.class);
		userRepository = mock(UserRepository.class);
		
		manager = new ProgramManager(programRepository, authorizationManager, userRepository);
		
		setupPermissions(authorizationManager, userRepository);
	}
	
	private void setupPermissions(IAuthorizationManager authorizationManager, IUserRepository userRepository) throws Exception
	{
		User ui1 = new User(0L, "su", "Super user", Arrays.asList(UserRole.SUPERUSER));
		User ui2 = new User(1L, "admin", "Admin", Arrays.asList(UserRole.ADMIN));
		User ui3 = new User(2L, "pm1", "Program manager 1", Arrays.asList(UserRole.PROGRAM_MANAGER));
		User ui4 = new User(3L, "pm2", "Program manager 2", Arrays.asList(UserRole.PROGRAM_MANAGER));
		User ui5 = new User(4L, "std", "Student", Arrays.asList(UserRole.STUDENT));
		
		when(authorizationManager.userHasPermission(ui1, "institution", "Program", Permission.READ)).thenReturn(true);
		when(authorizationManager.userHasPermission(ui1, "institution", "Program", Permission.EDIT)).thenReturn(true);
		when(authorizationManager.userHasPermission(ui1, "institution", "Program", Permission.DELETE)).thenReturn(true);
		when(authorizationManager.userHasPermission(ui2, "institution", "Program", Permission.READ)).thenReturn(true);
		when(authorizationManager.userHasPermission(ui2, "institution", "Program", Permission.EDIT)).thenReturn(true);
		when(authorizationManager.userHasPermission(ui2, "institution", "Program", Permission.DELETE)).thenReturn(true);
		when(authorizationManager.userHasPermission(ui3, "institution", "Program", Permission.READ)).thenReturn(true);
		when(authorizationManager.userHasPermission(ui3, "institution", "Program", Permission.EDIT)).thenReturn(false);
		when(authorizationManager.userHasPermission(ui3, "institution", "Program", Permission.DELETE)).thenReturn(false);
		when(authorizationManager.userHasPermission(ui4, "institution", "Program", Permission.READ)).thenReturn(true);
		when(authorizationManager.userHasPermission(ui4, "institution", "Program", Permission.EDIT)).thenReturn(false);
		when(authorizationManager.userHasPermission(ui4, "institution", "Program", Permission.DELETE)).thenReturn(false);
		when(authorizationManager.userHasPermission(ui5, "institution", "Program", Permission.READ)).thenReturn(false);
		when(authorizationManager.userHasPermission(ui5, "institution", "Program", Permission.EDIT)).thenReturn(false);
		when(authorizationManager.userHasPermission(ui5, "institution", "Program", Permission.DELETE)).thenReturn(false);
		
		when(userRepository.getUserByUsername("su")).thenReturn(ui1);
		when(userRepository.getUserByUsername("admin")).thenReturn(ui2);
		when(userRepository.getUserByUsername("pm1")).thenReturn(ui3);
		when(userRepository.getUserByUsername("pm2")).thenReturn(ui4);
		when(userRepository.getUserByUsername("std")).thenReturn(ui5);
	}
	
	@Test
	public void getProgramsListTest() throws Exception
	{
		// PREPARE TEST
		
		setupMocks();
		
		when(programRepository.getProgramsList("su")).thenReturn(programs3).thenReturn(programs3).thenReturn(programs0);
		when(programRepository.getProgramsList("admin")).thenReturn(programs3);
		when(programRepository.getProgramsList("pm1")).thenReturn(programs1);
		when(programRepository.getProgramsList("pm2")).thenReturn(programs2);
		
		// RUN TEST
		
		List<Program> lp = manager.getProgramsList("su");
		assertEquals(3, lp.size());
		
		lp = manager.getProgramsList("admin");
		assertEquals(3, lp.size());
		
		lp = manager.getProgramsList("su");
		assertEquals(3, lp.size());
		
		lp = manager.getProgramsList("su");
		assertEquals(0, lp.size());
		
		lp = manager.getProgramsList("pm1");
		
		assertEquals(1, lp.size());
		
		lp = manager.getProgramsList("pm2");
		
		assertEquals(2, lp.size());
		
		lp = manager.getProgramsList("std");
		
		assertNull(lp);
		
		lp = manager.getProgramsList(null);
		
		assertNull(lp);
		
		verify(programRepository, times(3)).getProgramsList("su");
		verify(programRepository, times(1)).getProgramsList("admin");
		verify(programRepository, times(1)).getProgramsList("pm1");
		verify(programRepository, times(1)).getProgramsList("pm2");
	}
	
	@Test
	public void deleteProgramTest() throws Exception
	{
		// PREPARE TEST
		
		setupMocks();
		
		// CREATE PROGRAM MANAGER USER WHICH CAN DELETE PROGRAM 3
		
		User pm1 = new User();
		pm1.setUsername("pm1");
		
		Program program = new Program();
		program.setProgramManager(pm1);
		
		when(programRepository.getProgram("su", 3)).thenReturn(program);
		
		// RUN TEST
		
		manager.deleteProgram("su", 3);
		verify(programRepository, times(1)).deleteProgram(3); // SU CAN DO ANYTHING
		
		manager.deleteProgram("admin", 3);
		verify(programRepository, times(2)).deleteProgram(3); // ADMINS CAN DO IT
		
		manager.deleteProgram("pm1", 3);
		verify(programRepository, times(2)).deleteProgram(3); // PM1 OWNS PROGRAM 3 BUT HAVE NO DELETE PERMISSION 
		
		manager.deleteProgram("pm2", 3);
		verify(programRepository, times(2)).deleteProgram(3); // PM2 DO NOT OWN PROGRAM 3 SO CAN'T DO IT
		
		manager.deleteProgram("std", 3);
		verify(programRepository, times(2)).deleteProgram(3); // STUDENS HAVE NO PERMISSION SO CAN'T DO IT 
	}
	
	@Test
	public void getProgramTest() throws Exception
	{
		// PREPARE TEST
		
		setupMocks();
		
		User pm1 = new User();
		pm1.setUsername("pm1");
		
		Program program = new Program();
		program.setProgramManager(pm1);
		
		when(programRepository.getProgram("su", 3)).thenReturn(program);
		when(programRepository.getProgram("admin", 3)).thenReturn(program);
		when(programRepository.getProgram("pm1", 3)).thenReturn(program);
		
		// RUN TEST
		
		Program p = manager.getProgram("su", 3);
		assertEquals(program, p); // SU CAN ACCESS
		
		p = manager.getProgram("admin", 3);
		assertEquals(program, p); // ADMIN CAN ACCESS
		
		p = manager.getProgram("pm1", 3);
		assertEquals(program, p); // PM1 OWNS PROGRAM SO CAN ACCESS
		
		try
		{
			p = manager.getProgram("pm2", 3);
			fail(); // THE OTHER PROGRAM MANAGER CAN'T ACCESS
		}
		catch (IctFlagPermissionDeniedException ex)
		{
		}
		
		try
		{
			p = manager.getProgram("std", 3);
			
			fail(); // STUDENTS DO NOT HAVE PERMISSION
		}
		catch (IctFlagPermissionDeniedException ex)
		{
		}
	}
	
	@Test
	public void createOrUpdateProgramTest() throws Exception
	{
		// PREPARE TEST
		
		setupMocks();
		
		User pm1 = new User();
		pm1.setUsername("pm1");
		
		Program program = new Program();
		program.setProgramManager(pm1);
		
		// RUN TEST
		
		manager.createOrUpdateProgram("su", program);
		verify(programRepository, times(1)).createOrUpdateProgram(program); // SU CAN DO IT 

		manager.createOrUpdateProgram("admin", program);
		verify(programRepository, times(2)).createOrUpdateProgram(program); // ADMIN CAN DO IT 
		
		manager.createOrUpdateProgram("pm1", program);
		verify(programRepository, times(2)).createOrUpdateProgram(program); // PM1 CAN DO IT 
		
		manager.createOrUpdateProgram("pm2", program);
		verify(programRepository, times(2)).createOrUpdateProgram(program); // PM2 CAN DO IT 
		
		manager.createOrUpdateProgram("std", program);
		verify(programRepository, times(2)).createOrUpdateProgram(program); // STD HAS NO PERMISSION 
	}
}
