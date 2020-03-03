package edu.uoc.ictflag.core.web;

import java.io.Serializable;
import java.util.Map;

import javax.faces.bean.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

@Named
@ApplicationScoped
public class URLHelper implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public enum Page
	{
		HOME, LOGIN, _404, ERROR, HOME_PRIVATE
	}
	
	public static String contextPath = null;
	
	public static final String facesPath = "";
	
	public static final String publicPath = "/public";
	
	public static final String privatePath = "/private";
	
	public static final String facesRedirectTrue = "?faces-redirect=true";
	
	public static String getRelativePath(Page page, boolean addFacesRedirectTrue)
	{
		return getRelativePath(getContextPath(), page, addFacesRedirectTrue);
	}
	
	public static String getRelativePath(String contextPath, Page page, boolean addFacesRedirectTrue)
	{
		String path = "";
		
		switch (page)
		{
			case HOME:
				path = "/index.xhtml";
				break;
				
			case LOGIN:
				path = "/login.xhtml";
				break;
				
			case _404:
				path = "/404.xhtml";
				break;
				
			case ERROR:
				path = "/error.xhtml";
				break;
				
			case HOME_PRIVATE:
				path = privatePath + "/index.xhtml";
				break;
		}
		
		return contextPath + facesPath + path + (addFacesRedirectTrue ? facesRedirectTrue : "");
	}
	
	public String getQueryAttribute(String param)
	{
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		
		return params.get(param);
	}
	
	private static String getContextPath()
	{
		if (contextPath == null)
		{
			contextPath = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getContextPath();
		}
		
		return contextPath;
	}
}
