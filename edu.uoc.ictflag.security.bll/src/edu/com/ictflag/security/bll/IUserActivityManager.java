package edu.com.ictflag.security.bll;

import edu.uoc.ictflag.security.model.UserActivity;

public interface IUserActivityManager
{
	void createUserActivity(UserActivity userActivity) throws Exception;
}
