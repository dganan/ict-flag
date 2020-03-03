package edu.uoc.ictflag.institution.bll;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import edu.com.ictflag.security.bll.IAuthorizationManager;
import edu.uoc.ictflag.core.IctFlagPermissionDeniedException;
import edu.uoc.ictflag.institution.dal.ISubjectRepository;
import edu.uoc.ictflag.institution.model.Subject;
import edu.uoc.ictflag.security.dal.IUserRepository;
import edu.uoc.ictflag.security.model.Permission;
import edu.uoc.ictflag.security.model.User;

@Stateless
public class SubjectManager implements ISubjectManager, Serializable
{
	private static final long serialVersionUID = 1L;
	
	ISubjectRepository programRepository;
	IAuthorizationManager authorizationManager;
	IUserRepository userRepository;
	
	@Inject
	public SubjectManager(ISubjectRepository programRepository, IAuthorizationManager authorizationManager, IUserRepository userRepository)
	{
		this.programRepository = programRepository;
		this.authorizationManager = authorizationManager;
		this.userRepository = userRepository;
	}
	
	@Override
	public Subject getSubject(String username, long id) throws Exception
	{
		Subject subject = null;
		
		User ui = userRepository.getUserByUsername(username);
		
		if (authorizationManager.userHasPermission(ui, "institution", "Subject", Permission.READ))
		{
			subject = programRepository.getSubject(username, id);
		}
		
		if (subject == null)
		{
			throw new IctFlagPermissionDeniedException(username);
		}
		
		return subject;
	}
	
	public List<Subject> getSubjectsList(String username) throws Exception
	{
		List<Subject> subjectsList = null;
		
		User ui = userRepository.getUserByUsername(username);
		
		if (authorizationManager.userHasPermission(ui, "institution", "Subject", Permission.READ))
		{
			subjectsList = programRepository.getSubjectsList(username);
		}
		
		return subjectsList;
	}
	
	@Override
	public void createOrUpdateSubject(String username, Subject subject) throws Exception
	{
		User ui = userRepository.getUserByUsername(username);
		
		if (authorizationManager.userHasPermission(ui, "institution", "Subject", Permission.EDIT))
		{
			//			if (ui.getRole() == UserRole.PROGRAM_MANAGER)
			//			{
			//				// Check user is PM of any program associated with the subject
			//				
			//				boolean canEdit = false;
			//				
			//				for (Program p : subject.getPrograms())
			//				{
			//					if (p.getProgramManager().getUsername().equals(username))
			//					{
			//						canEdit = true;
			//						break;
			//					}
			//				}
			//				
			//				if (canEdit)
			//				{
			//					programRepository.createOrUpdateSubject(subject);
			//				}
			//			}
			//			else
			{
				// BY NOW WE CONSIDER PROGRAM MANAGERS CAN EDIT ANY SUBJECT
				
				programRepository.createOrUpdateSubject(subject);
			}
		}
	}
	
	@Override
	public void deleteSubject(String username, long id) throws Exception
	{
		User ui = userRepository.getUserByUsername(username);
		
		if (authorizationManager.userHasPermission(ui, "institution", "Subject", Permission.DELETE))
		{
			programRepository.deleteSubject(id);
		}
	}
}
