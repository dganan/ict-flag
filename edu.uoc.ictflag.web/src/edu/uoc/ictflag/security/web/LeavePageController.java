package edu.uoc.ictflag.security.web;

import java.util.Date;

import javax.ejb.LocalBean;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.com.ictflag.security.bll.IUserActivityManager;
import edu.uoc.ictflag.core.log.LogHelper;
import edu.uoc.ictflag.security.model.UserActivity;

@LocalBean
@Path("/leave-page")
public class LeavePageController
{
	@Inject
	IUserActivityManager userActivityManager;
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response processAssignmentData(@FormParam("url") String url, @Context HttpServletRequest req) throws Exception
	{
		try
		{
			String username = (String) req.getSession().getAttribute("username");
			
			userActivityManager.createUserActivity(new UserActivity(username, new Date(), "leave-page", url));

			return Response.ok().build();
		}
		catch (Exception ex)
		{
			LogHelper.error(ex);
			
			return Response.serverError().build();
		}
	}
	
	//	@GET
	//	@Produces(MediaType.APPLICATION_JSON)
	//	@Path("{id}")
	//	public String read(@PathParam("id") long id)
	//	{
	//		return "Hello World! " + id;
	//	}
}
