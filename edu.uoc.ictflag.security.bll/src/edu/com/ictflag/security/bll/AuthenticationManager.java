package edu.com.ictflag.security.bll;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;

import edu.uoc.ictflag.security.dal.IUserRepository;

@Stateless
public class AuthenticationManager implements IAuthenticationManager, Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Inject
	IUserRepository userRepository;

	@Override
	public boolean checkUserLogin (String username, String password) throws Exception
	{
		return userRepository.verifyUserPassword(username, password);
	}
	
	@Override
	public boolean checkUserIDP(String username, String idp) throws Exception
	{
		return userRepository.verifyUserIdp(username, idp);
	}
}
