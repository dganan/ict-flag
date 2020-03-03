package edu.uoc.ictflag.security.web;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import edu.uoc.ictflag.core.log.LogHelper;

@Stateless
@LocalBean
@Path("/uoc-login")
public class UOCLoginApi
{
	@Inject
	LoginController loginController;
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String loginByCAS(@FormParam("username") String username, @FormParam("idp") String idp) throws Exception
	{
		try
		{
			LogHelper.info("UOCLoginApi - username: " + username + ", idp: " + idp);
			
			if (loginController.validateUsernameIdp(username, idp))
			{
				return "true";
			}
			else
			{
				return "false";
			}
		}
		catch (Exception ex)
		{
			LogHelper.error(ex);
			
			return "false";
		}
	}
}
