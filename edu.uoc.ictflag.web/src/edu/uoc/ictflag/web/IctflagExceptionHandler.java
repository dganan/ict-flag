package edu.uoc.ictflag.web;

import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import edu.uoc.ictflag.core.IctFlagPermissionDeniedException;
import edu.uoc.ictflag.core.log.LogHelper;

public class IctflagExceptionHandler extends ExceptionHandlerWrapper
{
	private ExceptionHandler wrapped;
	
	public IctflagExceptionHandler(ExceptionHandler wrapped)
	{
		this.wrapped = wrapped;
	}
	
	@Override
	public ExceptionHandler getWrapped()
	{
		return wrapped;
	}
	
	@Override
	public void handle() throws FacesException
	{
		Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator();
		
		while (i.hasNext())
		{
			ExceptionQueuedEvent event = (ExceptionQueuedEvent) i.next();
			ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
			
			// get the exception from context
			Throwable t = context.getException();
			FacesContext fc = FacesContext.getCurrentInstance();
			
			LogHelper.error(t);
			
			try
			{
				String pageToGo = "/error.xhtml";
				
				while (t != null)
				{
					if (t instanceof IctFlagPermissionDeniedException)
					{
						pageToGo = "/404.xhtml";
						break;
					}
					
					t = t.getCause();
				}

				/*
				 * Here you can use the Throwable object in order to verify the
				 * exceptions you want to handle in the application
				 */
				NavigationHandler navigationHandler = fc.getApplication().getNavigationHandler();
				navigationHandler.handleNavigation(fc, null, pageToGo); // + "?faces-redirect=true");
				fc.renderResponse();
			}
			finally
			{
				i.remove();
			}
		}
		
		// Call the parent exception handler’s handle() method
		getWrapped().handle();
	}
}
