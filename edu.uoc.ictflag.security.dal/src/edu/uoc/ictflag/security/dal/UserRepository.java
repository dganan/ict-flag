package edu.uoc.ictflag.security.dal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import edu.uoc.ictflag.core.dal.PartialSearchResultWithTotalCount;
import edu.uoc.ictflag.core.dal.SearchParameters;
import edu.uoc.ictflag.security.model.User;
import edu.uoc.ictflag.security.model.UserRole;
import edu.uoc.ictflag.security.model.UserSecurePassword;

@Stateless
@Transactional(TxType.REQUIRED)
public class UserRepository extends SecurityBaseRepository implements IUserRepository
{
	@Override
	public User getUserByUsername(String username) throws Exception
	{
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		parameters.put("username", username);
		
		User u = dbHelper.getFirst("SELECT u FROM User u WHERE u.username = :username", User.class, parameters);
		
		return u;
	}
	
	@Override
	public User getUserByEmail(String email) throws Exception
	{
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		parameters.put("email", email);
		
		User u = dbHelper.getFirst("SELECT u FROM User u WHERE u.email = :email", User.class, parameters);
		
		return u;
	}
	
	@Override
	public List<User> getUsersByRole(UserRole role) throws Exception
	{
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		parameters.put("role", "%" + role + "%");
		
		List<User> ul = dbHelper.getQueryResult("SELECT u FROM User u WHERE u.rolesStr LIKE :role", User.class, parameters);
		
		return ul;
	}
	
	@Override
	public PartialSearchResultWithTotalCount<User> getUsersByRole(UserRole role, SearchParameters searchParameters) throws Exception
	{
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		parameters.put("role", "%" + role + "%");
		
		return dbHelper.getQueryResult("SELECT u FROM User u WHERE u.rolesStr LIKE :role",
				"SELECT COUNT(u.id) FROM User u WHERE u.rolesStr LIKE :role", searchParameters, User.class, parameters);
	}
	
	@Override
	public void createOrUpdateUser(User u) throws Exception
	{
		dbHelper.createOrUpdateEntity(u);
	}
	
	@Override
	public boolean verifyUserPassword(String username, String password) throws Exception
	{
		User u = getUserByUsername(username);
		
		if (u != null)
		{
			Map<String,Object> parameters = new HashMap<String, Object>();
			
			parameters.put("userId", u.getId());
			
			UserSecurePassword usp = dbHelper.getFirst("SELECT u FROM UserSecurePassword u WHERE u.userId = :userId", UserSecurePassword.class,
					parameters);
			
			return usp != null && PasswordHasher.verifyPassword(password, usp.getPassword());
		}
		
		return false;
	}
	
	@Override
	public boolean verifyUserIdp(String username, String idp) throws Exception
	{
		User u = getUserByUsername(username);
		
		return (idp != null) ? idp.equals(u.getIdp()) : false;
	}
	
	// RELATIONS TO RESTRICT WHEN DELETE:
	// - PROGRAM MANAGER
	// - LECTURER
	// - INSTRUCTOR
	// - ...
}
