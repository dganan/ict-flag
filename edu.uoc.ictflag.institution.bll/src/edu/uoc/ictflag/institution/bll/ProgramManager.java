package edu.uoc.ictflag.institution.bll;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import edu.com.ictflag.security.bll.IAuthorizationManager;
import edu.uoc.ictflag.core.IctFlagPermissionDeniedException;
import edu.uoc.ictflag.institution.dal.IProgramRepository;
import edu.uoc.ictflag.institution.model.Program;
import edu.uoc.ictflag.security.dal.IUserRepository;
import edu.uoc.ictflag.security.model.Permission;
import edu.uoc.ictflag.security.model.User;
import edu.uoc.ictflag.security.model.UserRole;

@Stateless
public class ProgramManager implements IProgramManager, Serializable
{
	private static final long serialVersionUID = 1L;
	
	IProgramRepository programRepository;
	IAuthorizationManager authorizationManager;
	IUserRepository userRepository;
	
	@Inject
	public ProgramManager(IProgramRepository programRepository, IAuthorizationManager authorizationManager, IUserRepository userRepository)
	{
		this.programRepository = programRepository;
		this.authorizationManager = authorizationManager;
		this.userRepository = userRepository;
	}
	
	public List<Program> getProgramsList(String username) throws Exception
	{
		List<Program> programsList = null;
		
		User ui = userRepository.getUserByUsername(username);
		
		if (authorizationManager.userHasPermission(ui, "institution", "Program", Permission.READ))
		{
			programsList = programRepository.getProgramsList(username);
		}
		
		return programsList;
	}
	
	@Override
	public Program getProgram(String username, long id) throws Exception
	{
		Program program = null;
		
		User ui = userRepository.getUserByUsername(username);
		
		if (authorizationManager.userHasPermission(ui, "institution", "Program", Permission.READ))
		{
			program = programRepository.getProgram(username, id);
		}
		
		if (program == null)
		{
			throw new IctFlagPermissionDeniedException(username);
		}
		
		return program;
	}
	
	@Override
	public void createOrUpdateProgram(String username, Program program) throws Exception
	{
		User ui = userRepository.getUserByUsername(username);
		
		if (authorizationManager.userHasPermission(ui, "institution", "Program", Permission.EDIT))
		{
			if (ui.getSelectedRole() == UserRole.PROGRAM_MANAGER)
			{
				if (program.getProgramManager().getUsername().equals(username))
				{
					programRepository.createOrUpdateProgram(program);
				}
			}
			else
			{
				programRepository.createOrUpdateProgram(program);
			}
		}
	}
	
	@Override
	public void deleteProgram(String username, long id) throws Exception
	{
		User ui = userRepository.getUserByUsername(username);
		
		if (authorizationManager.userHasPermission(ui, "institution", "Program", Permission.DELETE))
		{
			if (ui.getSelectedRole() == UserRole.PROGRAM_MANAGER)
			{
				Program p = programRepository.getProgram(username, id);
				
				if (p.getProgramManager().getUsername().equals(username))
				{
					programRepository.deleteProgram(id);
				}
			}
			else
			{
				programRepository.deleteProgram(id);
			}
		}
	}
	
	@Override
	public List<User> getProgramManagers(String username) throws Exception
	{
		User ui = userRepository.getUserByUsername(username);
		
		if (authorizationManager.userHasPermission(ui, "institution", "Program", Permission.EDIT))
		{
			if (ui.getSelectedRole() == UserRole.PROGRAM_MANAGER)
			{
				List<User> ul = new ArrayList<User>();
				
				ul.add(userRepository.getUserByUsername(username));
				
				return ul;
			}
			else
			{
				return userRepository.getUsersByRole(UserRole.PROGRAM_MANAGER);
			}
		}
		
		return null;
	}
}
