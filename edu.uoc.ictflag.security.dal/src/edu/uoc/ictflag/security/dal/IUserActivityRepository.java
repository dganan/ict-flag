package edu.uoc.ictflag.security.dal;

import edu.uoc.ictflag.security.model.UserActivity;

public interface IUserActivityRepository
{
	void createUserActivity(UserActivity userActivity) throws Exception;
}
