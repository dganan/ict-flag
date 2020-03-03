package edu.com.ictflag.security.bll;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;

import edu.uoc.ictflag.security.dal.IUserActivityRepository;
import edu.uoc.ictflag.security.model.UserActivity;

@Stateless
public class UserActivityManager implements IUserActivityManager, Serializable
{
	private static final long serialVersionUID = 1L;
	
	IUserActivityRepository userActivityRepository;
	
	@Inject
	public UserActivityManager(IUserActivityRepository userActivityRepository)
	{
		this.userActivityRepository = userActivityRepository;
	}
	
	@Override
	public void createUserActivity(UserActivity userActivity) throws Exception
	{
		userActivityRepository.createUserActivity(userActivity);
	}
}
