package edu.com.ictflag.security.bll;

import java.io.Serializable;
import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;

import edu.uoc.ictflag.security.dal.IAuthorizationRepository;
import edu.uoc.ictflag.security.dal.IUserRepository;
import edu.uoc.ictflag.security.model.User;
import edu.uoc.ictflag.security.model.UserRole;

@Stateless
public class UserManager implements IUserManager, Serializable
{
	private static final long serialVersionUID = 1L;
	
	IAuthorizationRepository authorizationRepository;
	IUserRepository userRepository;
	
	@Inject
	public UserManager(IUserRepository userRepository, IAuthorizationRepository authorizationRepository)
	{
		this.userRepository = userRepository;
		this.authorizationRepository = authorizationRepository;
	}
	
	@Override
	public User getUser(String username) throws Exception
	{
		return userRepository.getUserByUsername(username);
	}
	
	@Override
	public void setUserSelectedLanguage(String username, String language) throws Exception
	{
		User u = userRepository.getUserByUsername(username);
		
		u.setSelectedLanguage(language);
		
		userRepository.createOrUpdateUser(u);
	}
	
	@Override
	public void setUserSelectedRole(String username, UserRole role) throws Exception
	{
		User u = userRepository.getUserByUsername(username);
		
		if (u.getRoles().contains(role))
		{
			u.setSelectedRole(role);
			userRepository.createOrUpdateUser(u);
		}
	}
	
	@Override
	public void setUserlastVisit(String username, Date lastVisit) throws Exception
	{
		User u = userRepository.getUserByUsername(username);
		
		u.setLastVisit(lastVisit);
		
		userRepository.createOrUpdateUser(u);
	}
}
