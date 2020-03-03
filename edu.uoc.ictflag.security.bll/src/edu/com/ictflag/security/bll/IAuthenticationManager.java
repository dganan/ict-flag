package edu.com.ictflag.security.bll;

public interface IAuthenticationManager
{
	public boolean checkUserLogin (String username, String password) throws Exception;
	
	public boolean checkUserIDP(String username, String idp) throws Exception;
}
