package edu.uoc.ictflag.web;

import java.net.URL;

import javax.faces.application.ResourceHandler;
import javax.faces.application.ViewResource;
import javax.faces.context.FacesContext;

import org.omnifaces.resourcehandler.UnmappedResourceHandler;

public class CustomResourceHandler extends UnmappedResourceHandler
{
	public static final String basePath = "/META-INF/resources/";
	
	public CustomResourceHandler(ResourceHandler wrapped)
	{
		super(wrapped);
	}
	
	@Override
	public ViewResource createViewResource(FacesContext context, String path)
	{
		ViewResource resource = super.createViewResource(context, path); // First try local.
		
		if (resource == null)
		{
			if (path.startsWith("/"))
			{
				path = path.substring(1);
			}
			
			URL u = Thread.currentThread().getContextClassLoader().getResource(basePath + path);
			
			resource = new ViewResource()
			{
				@Override
				public URL getURL()
				{
					return u;
				}
			};
		}
		
		return resource;
	}
}
