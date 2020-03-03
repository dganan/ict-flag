package edu.uoc.ictflag.core.web;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

public class SessionBean
{
	private static HttpSession getSession()
	{
		if (FacesContext.getCurrentInstance() != null && FacesContext.getCurrentInstance().getExternalContext() != null)
		{
			return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		}
		
		return null;
	}
	
	//	private static HttpServletRequest getRequest()
	//	{
	//		if (FacesContext.getCurrentInstance() != null && FacesContext.getCurrentInstance().getExternalContext() != null)
	//		{
	//			return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	//		}
	//		
	//		return null;
	//	}
	
	public static String getUsername()
	{
		return (String) getAttribute("username");
	}
	
	public static void invalidateSession()
	{
		HttpSession session = getSession();
		
		if (session != null)
		{
			session.invalidate();
		}
	}
	
	public static void setAttribute(String attribute, Object value)
	{
		HttpSession session = SessionBean.getSession();
		
		if (session != null)
		{
			session.setAttribute(attribute, value);
		}
	}
	
	public static Object getAttribute(String attribute)
	{
		HttpSession session = getSession();
		
		if (session != null)
		{
			return session.getAttribute(attribute);
		}
		else
		{
			return null;
		}
	}
}
