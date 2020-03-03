package edu.uoc.ictflag.core.web;

import java.io.IOException;

import javax.faces.context.FacesContext;

import edu.uoc.ictflag.core.web.URLHelper.Page;

public class NavigationHelper
{
	public static void redirectToUrl(String url) throws IOException
	{
		FacesContext.getCurrentInstance().getExternalContext().redirect(url);
	}
	
	public static void redirectTo404() throws IOException
	{
		redirectToUrl(URLHelper.getRelativePath(Page._404, true));
	}
}
