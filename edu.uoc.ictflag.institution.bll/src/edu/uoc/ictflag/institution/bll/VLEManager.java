package edu.uoc.ictflag.institution.bll;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import edu.com.ictflag.security.bll.IAuthorizationManager;
import edu.uoc.ictflag.core.IctFlagPermissionDeniedException;
import edu.uoc.ictflag.institution.dal.IVLERepository;
import edu.uoc.ictflag.institution.model.VLE;
import edu.uoc.ictflag.security.dal.IUserRepository;
import edu.uoc.ictflag.security.model.Permission;
import edu.uoc.ictflag.security.model.User;

@Stateless
public class VLEManager implements IVLEManager, Serializable
{
	private static final long serialVersionUID = 1L;
	
	IVLERepository vleRepository;
	IAuthorizationManager authorizationManager;
	IUserRepository userRepository;
	
	@Inject
	public VLEManager(IVLERepository vleRepository, IAuthorizationManager authorizationManager, IUserRepository userRepository)
	{
		this.vleRepository = vleRepository;
		this.authorizationManager = authorizationManager;
		this.userRepository = userRepository;
	}
	
	public List<VLE> getVLEsList(String username) throws Exception
	{
		List<VLE> vlesList = null;
		
		User ui = userRepository.getUserByUsername(username);
		
		if (authorizationManager.userHasPermission(ui, "institution", "VLE", Permission.READ))
		{
			vlesList = vleRepository.getVLEsList();
		}
		
		return vlesList;
	}
	
	@Override
	public VLE getVLE(String username, long id) throws Exception
	{
		VLE vle = null;
		
		User ui = userRepository.getUserByUsername(username);
		
		if (authorizationManager.userHasPermission(ui, "institution", "VLE", Permission.READ))
		{
			vle = vleRepository.getVLE(id);
		}
		
		if (vle == null)
		{
			throw new IctFlagPermissionDeniedException(username);
		}
		
		return vle;
	}
	
	@Override
	public void createOrUpdateVLE(String username, VLE vle) throws Exception
	{
		User ui = userRepository.getUserByUsername(username);
		
		if (authorizationManager.userHasPermission(ui, "institution", "VLE", Permission.EDIT))
		{
			vleRepository.createOrUpdateVLE(vle);
		}
	}
	
	@Override
	public void deleteVLE(String username, long id) throws Exception
	{
		User ui = userRepository.getUserByUsername(username);
		
		if (authorizationManager.userHasPermission(ui, "institution", "VLE", Permission.DELETE))
		{
			vleRepository.deleteVLE(id);
		}
	}
}
