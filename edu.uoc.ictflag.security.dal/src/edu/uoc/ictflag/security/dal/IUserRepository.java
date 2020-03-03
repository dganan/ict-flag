package edu.uoc.ictflag.security.dal;

import java.util.List;

import edu.uoc.ictflag.core.dal.PartialSearchResultWithTotalCount;
import edu.uoc.ictflag.core.dal.SearchParameters;
import edu.uoc.ictflag.security.model.User;
import edu.uoc.ictflag.security.model.UserRole;

public interface IUserRepository
{
	User getUserByUsername(String username) throws Exception;
	
	User getUserByEmail(String email) throws Exception;
	
	void createOrUpdateUser(User u) throws Exception;
	
	List<User> getUsersByRole(UserRole role) throws Exception;
	
	PartialSearchResultWithTotalCount<User> getUsersByRole(UserRole role, SearchParameters searchParameters) throws Exception;
	
	boolean verifyUserPassword(String username, String password) throws Exception;
	
	boolean verifyUserIdp(String username, String idp) throws Exception;
}
