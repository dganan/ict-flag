package edu.uoc.ictflag.institution.bll;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;

import edu.com.ictflag.security.bll.IAuthorizationManager;
import edu.uoc.ictflag.core.IctFlagPermissionDeniedException;
import edu.uoc.ictflag.institution.dal.IInstitutionRepository;
import edu.uoc.ictflag.institution.model.Institution;
import edu.uoc.ictflag.security.dal.IUserRepository;
import edu.uoc.ictflag.security.model.Permission;
import edu.uoc.ictflag.security.model.User;

@Stateless
public class InstitutionManager implements IInstitutionManager, Serializable
{
	private static final long serialVersionUID = 1L;
	
	IInstitutionRepository institutionRepository;
	IAuthorizationManager authorizationManager;
	IUserRepository userRepository;
	
	@Inject
	public InstitutionManager(IInstitutionRepository institutionRepository, IAuthorizationManager authorizationManager, IUserRepository userRepository)
	{
		this.institutionRepository = institutionRepository;
		this.authorizationManager = authorizationManager;
		this.userRepository = userRepository;
	}
	
	@Override
	public Institution getInstitution(String username) throws Exception
	{
		Institution institution = null;
		
		User ui = userRepository.getUserByUsername(username);
		
		if (authorizationManager.userHasPermission(ui, "institution", "Institution", Permission.READ))
		{
			institution = institutionRepository.getInstitution();
		}
		
		if (institution == null)
		{
			throw new IctFlagPermissionDeniedException(username);
		}
		
		return institution;
	}
	
	@Override
	public void createOrUpdateInstitution(String username, Institution institution) throws Exception
	{
		User ui = userRepository.getUserByUsername(username);
		
		if (authorizationManager.userHasPermission(ui, "institution", "Institution", Permission.EDIT))
		{
			institutionRepository.createOrUpdateInstitution(institution);
		}
	}
}
