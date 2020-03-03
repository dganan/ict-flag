package edu.uoc.ictflag.security.web;

import java.io.IOException;
import java.util.Date;
import java.util.regex.Pattern;

import javax.faces.application.ResourceHandler;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.com.ictflag.security.bll.IAuthorizationManager;
import edu.com.ictflag.security.bll.IUserActivityManager;
import edu.uoc.ictflag.core.IctFlagPermissionDeniedException;
import edu.uoc.ictflag.core.log.ActivityLogInterceptor;
import edu.uoc.ictflag.core.log.LogHelper;
import edu.uoc.ictflag.core.web.SessionBean;
import edu.uoc.ictflag.core.web.URLHelper;
import edu.uoc.ictflag.core.web.URLHelper.Page;
import edu.uoc.ictflag.security.model.UserActivity;

@WebFilter(filterName = "AuthorizationFilter", urlPatterns = { "*.xhtml" })
public class AuthorizationFilter implements Filter
{
	public AuthorizationFilter()
	{
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
	
	}
	
	@Inject
	private IUserActivityManager userActivityManager;
	
	@Inject
	private IAuthorizationManager authorizationManager;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		// TODO: COMMENT THIS CODE FOR AUTHENTICATION TESTS AND DELETE IT ON PRODUCTION
		
		//		boolean develop = false;
		//		
		//		if (develop)
		//		{
		//			HttpServletRequest reqt = (HttpServletRequest) request;
		//			HttpSession ses = reqt.getSession(true);
		//			
		//			if (ses != null)
		//			{
		//				ses.setAttribute("username", "su");
		//				
		//				chain.doFilter(request, response);
		//			}
		//			
		//			return;
		//		}
		
		// END TODO
		
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpServletRequest reqt = (HttpServletRequest) request;
		HttpSession session = (HttpSession) reqt.getSession(true);
		
		String contextPath = reqt.getContextPath();
		String reqURI = reqt.getRequestURI();
		
		try
		{
			userActivityManager.createUserActivity(new UserActivity((String) session.getAttribute("username"), new Date(), "URL request", reqURI));
			
			//			if (reqt.getRemoteUser() != null && reqt.getScheme().equals("http"))
			//			{
			//				String url = "https://" + reqt.getServerName() + reqt.getContextPath() + reqt.getServletPath();
			//				
			//				if (reqt.getPathInfo() != null)
			//				{
			//					url += reqt.getPathInfo();
			//				}
			//				
			//				resp.sendRedirect(url);
			//			}
			//			else
			{
				ActivityLogInterceptor.logUserDataEntry(session, reqt);
				
				boolean authenticated = userIsAuthenticated(session);
				
				boolean validURI = isValidURI(reqURI, contextPath);
				boolean isPublicURL = isPublicURI(reqURI, contextPath);
				
				if (!reqURI.startsWith(contextPath + ResourceHandler.RESOURCE_IDENTIFIER)) // Skip JSF resources (CSS/JS/Images/etc)
				{
					resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
					resp.setHeader("Pragma", "no-cache"); // HTTP 1.0.
					resp.setDateHeader("Expires", 0); // Proxies.
				}
				
				if (authenticated)
				{
					if (validURI && (isPublicURL || userHasPermission(session, reqURI, contextPath)))
					{
						chain.doFilter(request, response);
					}
					else
					{
						LogHelper.info("ACCESS DENIAL user: " + SessionBean.getUsername() + ", page: " + reqURI);
						
						resp.sendRedirect(URLHelper.getRelativePath(contextPath, Page._404, true));
					}
				}
				else
				{
					if (validURI && isPublicURL)
					{
						chain.doFilter(request, response);
					}
					else
					{
						String url = URLHelper.getRelativePath(contextPath, Page.LOGIN, true);
						
						url = url + "&login-redirect=" + reqURI;
						
						resp.sendRedirect(url);
					}
				}
			}
		}
		catch (IctFlagPermissionDeniedException e)
		{
			LogHelper.error(e);
			LogHelper.info("ACCESS DENIAL user: " + SessionBean.getUsername() + ", page: " + reqURI);
			
			resp.sendRedirect(URLHelper.getRelativePath(contextPath, Page._404, true));
		}
		catch (Exception e)
		{
			LogHelper.error(e);
			
			resp.sendRedirect(URLHelper.getRelativePath(contextPath, Page.ERROR, true));
		}
	}
	
	private boolean userHasPermission(HttpSession session, String reqURI, String contextPath) throws Exception
	{
		if (reqURI == null || reqURI.length() == 0)
		{
			return false;
		}
		
		// Remove query string from URI
		String pageUrl = reqURI.split(Pattern.quote("?"))[0];
		
		// Remove context path
		pageUrl = pageUrl.substring(contextPath.length());
		
		return authorizationManager.userHasPageAccess((String) session.getAttribute("username"), pageUrl);
	}
	
	private boolean isPublicURI(String reqURI, String contextPath)
	{
		return reqURI.indexOf(URLHelper.privatePath + "/") < 0;
	}
	
	private boolean isValidURI(String reqURI, String contextPath)
	{
		return reqURI.contains("javax.faces.resource") // recurs
				// pagina xhtml (adreçada a traves de /pages/
				|| reqURI.indexOf(URLHelper.facesPath + "/") >= 0
				// home page
				|| reqURI.equals(contextPath + "/");
	}
	
	private boolean userIsAuthenticated(HttpSession session)
	{
		return (session != null && session.getAttribute("username") != null);
	}
	
	@Override
	public void destroy()
	{
	
	}
}
