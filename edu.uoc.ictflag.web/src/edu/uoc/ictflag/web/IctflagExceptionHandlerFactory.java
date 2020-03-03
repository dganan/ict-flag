package edu.uoc.ictflag.web;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

public class IctflagExceptionHandlerFactory extends ExceptionHandlerFactory
{
	private ExceptionHandlerFactory parent;
	
	// this injection handles jsf
	public IctflagExceptionHandlerFactory(ExceptionHandlerFactory parent)
	{
        this.parent = parent;
    }
	
	@Override
	public ExceptionHandler getExceptionHandler()
	{
		ExceptionHandler result = new IctflagExceptionHandler(parent.getExceptionHandler());
		return result;
	}
}
