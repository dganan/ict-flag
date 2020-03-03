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
import edu.uoc.ictflag.institution.bll.SubjectManager;
import edu.uoc.ictflag.institution.dal.ISubjectRepository;
import edu.uoc.ictflag.institution.dal.SubjectRepository;
import edu.uoc.ictflag.institution.model.Subject;
import edu.uoc.ictflag.security.dal.IUserRepository;
import edu.uoc.ictflag.security.dal.UserRepository;
import edu.uoc.ictflag.security.model.Permission;
import edu.uoc.ictflag.security.model.User;
import edu.uoc.ictflag.security.model.UserRole;

@RunWith(MockitoJUnitRunner.class)
public class SubjectManagerTest
{
	ISubjectRepository subjectRepository;
	IAuthorizationManager authorizationManager;
	IUserRepository userRepository;
	
	SubjectManager manager;
	
	static List<Subject> subjects0 = new ArrayList<Subject>();
	
	static List<Subject> subjects1 = new ArrayList<Subject>();
	
	static List<Subject> subjects2 = new ArrayList<Subject>();
	
	static List<Subject> subjects3 = new ArrayList<Subject>();
	
	static List<Subject> subjects4 = new ArrayList<Subject>();
	
	static List<Subject> subjects5 = new ArrayList<Subject>();
	
	static List<Subject> subjects6 = new ArrayList<Subject>();
	
	static
	{
		subjects1.add(new Subject());
		
		subjects2.add(new Subject());
		subjects2.add(new Subject());
		
		subjects3.add(new Subject());
		subjects3.add(new Subject());
		subjects3.add(new Subject());
		
		subjects4.add(new Subject());
		subjects4.add(new Subject());
		subjects4.add(new Subject());
		subjects4.add(new Subject());
		
		subjects5.add(new Subject());
		subjects5.add(new Subject());
		subjects5.add(new Subject());
		subjects5.add(new Subject());
		subjects5.add(new Subject());
		
		subjects6.add(new Subject());
		subjects6.add(new Subject());
		subjects6.add(new Subject());
		subjects6.add(new Subject());
		subjects6.add(new Subject());
		subjects6.add(new Subject());
	}
	
	private void setupMocks() throws Exception
	{
		subjectRepository = mock(SubjectRepository.class);
		authorizationManager = mock(AuthorizationManager.class);
		userRepository = mock(UserRepository.class);
		
		manager = new SubjectManager(subjectRepository, authorizationManager, userRepository);
		
		setupPermissions(authorizationManager, userRepository);
	}
	
	private void setupPermissions(IAuthorizationManager authorizationManager, IUserRepository userRepository) throws Exception
	{
		User ui1 = new User(0L, "su", "Super user", Arrays.asList(UserRole.SUPERUSER));
		User ui2 = new User(1L, "admin", "Admin", Arrays.asList(UserRole.ADMIN));
		User ui3 = new User(2L, "pm1", "Program manager 1", Arrays.asList(UserRole.PROGRAM_MANAGER));
		User ui4 = new User(3L, "pm2", "Program manager 2", Arrays.asList(UserRole.PROGRAM_MANAGER));
		User ui5 = new User(4L, "std", "Student", Arrays.asList(UserRole.STUDENT));
		User ui6 = new User(5L, "lect", "Lecturer", Arrays.asList(UserRole.LECTURER));
		User ui7 = new User(6L, "inst", "Instructor", Arrays.asList(UserRole.INSTRUCTOR));
		
		when(authorizationManager.userHasPermission(ui1, "institution", "Subject", Permission.READ)).thenReturn(true);
		when(authorizationManager.userHasPermission(ui1, "institution", "Subject", Permission.EDIT)).thenReturn(true);
		when(authorizationManager.userHasPermission(ui1, "institution", "Subject", Permission.DELETE)).thenReturn(true);
		when(authorizationManager.userHasPermission(ui2, "institution", "Subject", Permission.READ)).thenReturn(true);
		when(authorizationManager.userHasPermission(ui2, "institution", "Subject", Permission.EDIT)).thenReturn(true);
		when(authorizationManager.userHasPermission(ui2, "institution", "Subject", Permission.DELETE)).thenReturn(true);
		when(authorizationManager.userHasPermission(ui3, "institution", "Subject", Permission.READ)).thenReturn(true);
		when(authorizationManager.userHasPermission(ui3, "institution", "Subject", Permission.EDIT)).thenReturn(false);
		when(authorizationManager.userHasPermission(ui3, "institution", "Subject", Permission.DELETE)).thenReturn(false);
		when(authorizationManager.userHasPermission(ui4, "institution", "Subject", Permission.READ)).thenReturn(true);
		when(authorizationManager.userHasPermission(ui4, "institution", "Subject", Permission.EDIT)).thenReturn(false);
		when(authorizationManager.userHasPermission(ui4, "institution", "Subject", Permission.DELETE)).thenReturn(false);
		when(authorizationManager.userHasPermission(ui5, "institution", "Subject", Permission.READ)).thenReturn(false);
		when(authorizationManager.userHasPermission(ui5, "institution", "Subject", Permission.EDIT)).thenReturn(false);
		when(authorizationManager.userHasPermission(ui5, "institution", "Subject", Permission.DELETE)).thenReturn(false);
		
		when(authorizationManager.userHasPermission(ui6, "institution", "Subject", Permission.READ)).thenReturn(true);
		when(authorizationManager.userHasPermission(ui6, "institution", "Subject", Permission.EDIT)).thenReturn(false);
		when(authorizationManager.userHasPermission(ui6, "institution", "Subject", Permission.DELETE)).thenReturn(false);
		when(authorizationManager.userHasPermission(ui7, "institution", "Subject", Permission.READ)).thenReturn(true);
		when(authorizationManager.userHasPermission(ui7, "institution", "Subject", Permission.EDIT)).thenReturn(false);
		when(authorizationManager.userHasPermission(ui7, "institution", "Subject", Permission.DELETE)).thenReturn(false);
		
		when(userRepository.getUserByUsername("su")).thenReturn(ui1);
		when(userRepository.getUserByUsername("admin")).thenReturn(ui2);
		when(userRepository.getUserByUsername("pm1")).thenReturn(ui3);
		when(userRepository.getUserByUsername("pm2")).thenReturn(ui4);
		when(userRepository.getUserByUsername("std")).thenReturn(ui5);
		when(userRepository.getUserByUsername("lect")).thenReturn(ui6);
		when(userRepository.getUserByUsername("inst")).thenReturn(ui7);
	}
	
	@Test
	public void getSubjectsListTest() throws Exception
	{
		// PREPARE TEST
		
		setupMocks();
		
		when(subjectRepository.getSubjectsList("su")).thenReturn(subjects6).thenReturn(subjects5).thenReturn(subjects0);
		when(subjectRepository.getSubjectsList("admin")).thenReturn(subjects5);
		when(subjectRepository.getSubjectsList("pm1")).thenReturn(subjects4);
		when(subjectRepository.getSubjectsList("pm2")).thenReturn(subjects3);
		
		when(subjectRepository.getSubjectsList("lect")).thenReturn(subjects2);
		
		when(subjectRepository.getSubjectsList("inst")).thenReturn(subjects1);
		
		// RUN TEST
		
		List<Subject> lp = manager.getSubjectsList("su");
		assertEquals(6, lp.size());
		
		lp = manager.getSubjectsList("unknown");
		assertNull(lp);
		
		lp = manager.getSubjectsList("admin");
		assertEquals(5, lp.size());
		
		lp = manager.getSubjectsList("su");
		assertEquals(5, lp.size());
		
		lp = manager.getSubjectsList("su");
		assertEquals(0, lp.size());
		
		lp = manager.getSubjectsList("pm1");
		assertEquals(4, lp.size());
		
		lp = manager.getSubjectsList("pm2");
		assertEquals(3, lp.size());
		
		lp = manager.getSubjectsList("lect");
		assertEquals(2, lp.size());
		
		lp = manager.getSubjectsList("inst");
		assertEquals(1, lp.size());
		
		lp = manager.getSubjectsList("std");
		assertNull(lp);
		
		lp = manager.getSubjectsList(null);
		assertNull(lp);
		
		verify(subjectRepository, times(3)).getSubjectsList("su");
		verify(subjectRepository, times(1)).getSubjectsList("admin");
		verify(subjectRepository, times(1)).getSubjectsList("pm1");
		verify(subjectRepository, times(1)).getSubjectsList("pm2");
		verify(subjectRepository, times(1)).getSubjectsList("lect");
		verify(subjectRepository, times(1)).getSubjectsList("inst");
	}
	
	@Test
	public void deleteSubjectTest() throws Exception
	{
		// PREPARE TEST
		
		setupMocks();
		
		// RUN TEST
		
		manager.deleteSubject("su", 3);
		verify(subjectRepository, times(1)).deleteSubject(3); // SU CAN DO ANYTHING
		
		manager.deleteSubject("admin", 3);
		verify(subjectRepository, times(2)).deleteSubject(3); // ADMINS CAN DO IT
		
		manager.deleteSubject("pm1", 3);
		verify(subjectRepository, times(2)).deleteSubject(3); // PM1 CAN'T DO IT 
		
		manager.deleteSubject("pm2", 3);
		verify(subjectRepository, times(2)).deleteSubject(3); // PM2 CAN'T DO IT
		
		manager.deleteSubject("lect", 3);
		verify(subjectRepository, times(2)).deleteSubject(3); // LECTURERS CAN'T DO IT 
		
		manager.deleteSubject("inst", 3);
		verify(subjectRepository, times(2)).deleteSubject(3); // INSTRUCTORS CAN'T DO IT 
		
		manager.deleteSubject("std", 3);
		verify(subjectRepository, times(2)).deleteSubject(3); // STUDENS CAN'T DO IT 
	}
	
	@Test
	public void getSubjectTest() throws Exception
	{
		// PREPARE TEST
		
		setupMocks();
		
		User pm1 = new User();
		pm1.setUsername("pm1");
		
		Subject subject = new Subject();
		
		when(subjectRepository.getSubject("su", 3)).thenReturn(subject);
		when(subjectRepository.getSubject("admin", 3)).thenReturn(subject);
		when(subjectRepository.getSubject("pm1", 3)).thenReturn(subject);
		when(subjectRepository.getSubject("pm2", 3)).thenReturn(subject);
		when(subjectRepository.getSubject("lect", 3)).thenReturn(subject);
		when(subjectRepository.getSubject("inst", 3)).thenReturn(subject);
		
		// RUN TEST
		
		Subject p = manager.getSubject("su", 3);
		assertEquals(subject, p); // SU CAN ACCESS
		
		p = manager.getSubject("admin", 3);
		assertEquals(subject, p); // ADMIN CAN ACCESS
		
		p = manager.getSubject("pm1", 3);
		assertEquals(subject, p); // PM CAN ACCESS
		
		p = manager.getSubject("pm2", 3);
		assertEquals(subject, p); // PM CAN ACCESS
		
		p = manager.getSubject("lect", 3);
		assertEquals(subject, p); // LECTURERS CAN ACCESS
		
		p = manager.getSubject("inst", 3);
		assertEquals(subject, p); // INSTRUCTORS CAN ACCESS
		
		try
		{
			p = manager.getSubject("std", 3);
			
			fail(); // STUDENTS DO NOT HAVE PERMISSION
		}
		catch (IctFlagPermissionDeniedException ex)
		{
		}
	}
	
	@Test
	public void createOrUpdateSubjectTest() throws Exception
	{
		// PREPARE TEST
		
		setupMocks();
		
		User pm1 = new User();
		pm1.setUsername("pm1");
		
		Subject subject = new Subject();
		
		// RUN TEST
		
		manager.createOrUpdateSubject("su", subject);
		verify(subjectRepository, times(1)).createOrUpdateSubject(subject); // SU CAN DO IT 

		manager.createOrUpdateSubject("admin", subject);
		verify(subjectRepository, times(2)).createOrUpdateSubject(subject); // ADMIN CAN DO IT 
		
		manager.createOrUpdateSubject("pm1", subject);
		verify(subjectRepository, times(2)).createOrUpdateSubject(subject); // PM1 CAN'T DO IT 
		
		manager.createOrUpdateSubject("pm2", subject);
		verify(subjectRepository, times(2)).createOrUpdateSubject(subject); // PM2 CAN'T DO IT 
		
		manager.createOrUpdateSubject("lect", subject);
		verify(subjectRepository, times(2)).createOrUpdateSubject(subject); // LECTURERS CAN'T DO IT 
		
		manager.createOrUpdateSubject("inst", subject);
		verify(subjectRepository, times(2)).createOrUpdateSubject(subject); // INSTRUCTORS CAN'T DO IT 
		
		manager.createOrUpdateSubject("std", subject);
		verify(subjectRepository, times(2)).createOrUpdateSubject(subject); // STD HAS NO PERMISSION 
	}
}
