package edu.uoc.ictflag.security.web;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import org.apache.commons.codec.binary.Base64;
import org.imsglobal.aspect.Lti;
import org.imsglobal.lti.launch.LtiVerificationResult;

import edu.uoc.ictflag.core.log.LogHelper;

@Stateless
@LocalBean
@Path("/lti")
public class LTIProvider
{
	@Inject
	LoginController loginController;
	
	@POST
	public void loginByLTI(@Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception
	{
		try
		{
			//request.setCharacterEncoding("UTF-8");
			
			//			LtiVerifier ltiVerifier = new LtiOauthVerifier();
			String key = request.getParameter("oauth_consumer_key");
			//			String signature = request.getParameter("oauth_signature");
			//			
			//			LogHelper.info("LTI authentication signature: " + signature);
			//			
			//			String secret = "5N123FDchTmTYr6n";
			//			
			//			LtiVerificationResult ltiResult = ltiVerifier.verify(request, secret);
			
			if (!key.equals("gres"))
			{
				LogHelper.error("LTI authentication failed - key is not accepted: " + key);
				
				response.sendRedirect("/login.xhtml");
				
				return;
			}
			
			//			if (!ltiResult.getSuccess())
			//			{
			//				LogHelper.error("LTI authentication failed - lti verification failed: " + ltiResult.getMessage() + " - " + ltiResult.getError());
			//				
			//				response.sendRedirect("/login.xhtml");
			//				
			//				return;
			//			}
			
			// Get the custom parameters
			HashMap<String, Object> custom = getCustomParameters(request);
			
			// Standard LTI fields
			String userEmailConsumer = checkBase64(request.getParameter("lis_person_contact_email_primary"));
			
			LogHelper.info("LTIProvider - lis_person_contact_email_primary: " + userEmailConsumer);
			
			// Get or create the username
			
			String username = request.getParameter("lis_person_sourcedid");
						
			LogHelper.info("LTIProvider - lis_person_sourcedid: " + username);
			
			if (username == null || username.isEmpty())
			{
				username = userEmailConsumer.split("@")[0];
			}
			else
			{
				username = checkBase64(username);
				
				// UOC lti call provides this field as uoc.edu:<username>
				String[] parts = username.split(":");
				
				if (parts.length == 2)
				{
					username = parts[1];
				}
			}
			
			LogHelper.info("LTIProvider - username: " + username);
			
			// UOC fields provided when tool is launched from a classroom         
			String idp = (String) custom.get("usernumber");
			
			LogHelper.info("LTIProvider - idp: " + idp);
			
			if (loginController.validateUsernameLti(username))
			{
				request.getSession().setAttribute("username", username);
				
				response.sendRedirect("/private/index.xhtml");
				
				LogHelper.error("LTI authentication succeeded");
			}
			else
			{
				LogHelper.error("LTI authentication failed. User is not in the database");
				
				response.sendRedirect("/login.xhtml");
			}
		}
		catch (Exception ex)
		{
			LogHelper.error(ex);
			
			response.sendRedirect("/500.xhtml");
		}
	}
	
	private String checkBase64(String str)
	{
		String result = str;
		
		if (Base64.isArrayByteBase64(str.getBytes()))
		{
			byte[] decoded = Base64.decodeBase64(str.getBytes());
			
			result = new String(decoded);
		}
		
		return result;
	}
	
	//@POST
	@Lti
	public void loginByLTI_v2(@Context HttpServletRequest request, @Context HttpServletResponse response, LtiVerificationResult ltiResult)
			throws Exception
	{
		try
		{
			request.setCharacterEncoding("UTF-8");
			
			String key = request.getParameter("oauth_consumer_key");
			String signature = request.getParameter("oauth_signature");
			
			LogHelper.info("LTI authentication signature: " + signature);
			
			if (!key.equals("gres"))
			{
				LogHelper.error("LTI authentication failed - key is not accepted: " + key);
				
				response.sendRedirect("/login.xhtml");
				
				return;
			}
			
			if (!ltiResult.getSuccess())
			{
				LogHelper.error("LTI authentication failed - lti verification failed: " + ltiResult.getMessage() + " - " + ltiResult.getError());
				
				response.sendRedirect("/login.xhtml");
				
				return;
			}
			
			// Get the custom parameters
			HashMap<String, Object> custom = getCustomParameters(request);
			
			// Standard LTI fields
			String userEmailConsumer = request.getParameter("lis_person_contact_email_primary");
			
			// Get or create the username
			String username = request.getParameter("lis_person_sourcedid");
			
			if (username == null || username.isEmpty())
			{
				username = userEmailConsumer.split("@")[0];
			}
			else
			{
				// UOC lti call provides this field as uoc.edu:<username>
				String[] parts = username.split(":");
				
				if (parts.length == 2)
				{
					username = parts[1];
				}
			}
			
			// UOC fields provided when tool is launched from a classroom         
			String idp = (String) custom.get("usernumber");
			
			LogHelper.info("LTIProvider - username: " + username);
			
			if (loginController.validateUsernameLti(username))
			{
				request.getSession().setAttribute("username", username);
				
				response.sendRedirect("/private/index.xhtml");
				
				LogHelper.error("LTI authentication succeeded");
			}
			else
			{
				LogHelper.error("LTI authentication failed. User is not in the database");
				
				response.sendRedirect("/login.xhtml");
			}
		}
		catch (Exception ex)
		{
			LogHelper.error(ex);
			
			response.sendRedirect("/500.xhtml");
		}
	}
	
	private HashMap<String, Object> getCustomParameters(HttpServletRequest request)
	{
		HashMap<String, Object> pars = new HashMap<String, Object>();
		
		for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet())
		{
			String key = entry.getKey();
			String[] value = entry.getValue();
			
			if (key.startsWith("custom_"))
			{
				String newKey = key.substring("custom_".length());
				if (newKey.charAt(0) == '_')
				{
					newKey = newKey.substring(1);
				}
				if (newKey.charAt(0) >= '0' && newKey.charAt(0) <= '9')
				{
					// Is an array store and process later
				}
				else
				{
					if (value.length == 1)
					{
						String val = value[0];
						if (val.endsWith("\""))
						{
							val = val.substring(0, val.length() - 1);
						}
						pars.put(newKey, val);
					}
					else
					{
						pars.put(newKey, value);
					}
				}
			}
		}
		
		return pars;
	}
}
