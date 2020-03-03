package edu.com.ictflag.security.bll;

import java.util.Date;

import edu.uoc.ictflag.security.model.User;
import edu.uoc.ictflag.security.model.UserRole;

public interface IUserManager
{
	User getUser(String username) throws Exception;
	
	void setUserSelectedLanguage(String username, String language) throws Exception;
	
	void setUserSelectedRole(String username, UserRole role) throws Exception;
	
	void setUserlastVisit(String username, Date lastVisit) throws Exception;
}
