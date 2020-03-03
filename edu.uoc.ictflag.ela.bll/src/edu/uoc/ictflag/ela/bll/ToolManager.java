package edu.uoc.ictflag.ela.bll;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import edu.com.ictflag.security.bll.IAuthorizationManager;
import edu.uoc.ictflag.core.IctFlagPermissionDeniedException;
import edu.uoc.ictflag.ela.dal.IToolRepository;
import edu.uoc.ictflag.ela.model.Tool;
import edu.uoc.ictflag.security.dal.IUserRepository;
import edu.uoc.ictflag.security.model.Permission;
import edu.uoc.ictflag.security.model.User;

@Stateless
public class ToolManager implements IToolManager, Serializable
{
	private static final long serialVersionUID = 1L;
	
	IToolRepository toolRepository;
	IAuthorizationManager authorizationManager;
	IUserRepository userRepository;
	
	@Inject
	public ToolManager(IToolRepository toolRepository, IAuthorizationManager authorizationManager, IUserRepository userRepository)
	{
		this.toolRepository = toolRepository;
		this.authorizationManager = authorizationManager;
		this.userRepository = userRepository;
	}
	
	public List<Tool> getToolsList(String username) throws Exception
	{
		List<Tool> toolsList = null;
		
		User ui = userRepository.getUserByUsername(username);
		
		if (authorizationManager.userHasPermission(ui, "security", "Tool", Permission.READ))
		{
			toolsList = toolRepository.getToolsList(username);
		}
		
		return toolsList;
	}
	
	@Override
	public Tool getTool(String username, long id) throws Exception
	{
		Tool tool = null;
		
		User ui = userRepository.getUserByUsername(username);
		
		if (authorizationManager.userHasPermission(ui, "security", "Tool", Permission.READ))
		{
			tool = toolRepository.getTool(username, id);
		}
		
		if (tool == null)
		{
			throw new IctFlagPermissionDeniedException(username);
		}
		
		return tool;
	}
	
	@Override
	public void createOrUpdateTool(String username, Tool tool) throws Exception
	{
		User ui = userRepository.getUserByUsername(username);
		
		if (authorizationManager.userHasPermission(ui, "security", "Tool", Permission.EDIT))
		{
			toolRepository.createOrUpdateTool(tool);
		}
	}
	
	@Override
	public void deleteTool(String username, long id) throws Exception
	{
		User ui = userRepository.getUserByUsername(username);
		
		if (authorizationManager.userHasPermission(ui, "security", "Tool", Permission.DELETE))
		{
			toolRepository.deleteTool(id);
		}
	}
}
