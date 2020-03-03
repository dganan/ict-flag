package edu.uoc.ictflag.core.log;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.uoc.ictflag.core.web.SessionBean;

@ActivityLog
@Interceptor
@Named
@Dependent
public class ActivityLogInterceptor implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public ActivityLogInterceptor()
	{
	}
	
	@AroundInvoke
	public Object logMethodEntry(InvocationContext invocationContext) throws Exception
	{
		Object o = null;
		
		String userData = getUserData();
		
		LogHelper.activity(
				userData + ";" + invocationContext.getMethod().getDeclaringClass().getName() + ";" + invocationContext.getMethod().getName());
		
		o =  invocationContext.proceed();
		
		return o;
	}
	
	public static void logUserDataEntry(HttpSession session, HttpServletRequest request) throws Exception
	{
		String userData = getUserData((String) session.getAttribute("username"), request);
		
		LogHelper.activity(userData);
	}
	
	private static String getUserData()
	{
		String username = SessionBean.getUsername();
		
		return getUserData(username, (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest());
	}
	
	private static String getUserData(String username, HttpServletRequest request)
	{
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		
		if (ipAddress == null)
		{
			ipAddress = request.getRemoteAddr();
		}
		
		String uri = request.getRequestURI();

		return (username != null ? username : "") + ";" + (ipAddress != null ? ipAddress : "") + ";" + uri;
	}
}
