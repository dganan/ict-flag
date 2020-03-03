package edu.uoc.ictflag.core.log;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.inject.Named;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@PerformanceLog
@Interceptor
@Named
@Dependent
public class PerformanceLogInterceptor implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public PerformanceLogInterceptor()
	{
	}
	
	@AroundInvoke
	public Object logMethodEntry(InvocationContext invocationContext) throws Exception
	{
		Object o = null;
			
		long start = System.currentTimeMillis();
		
		o =  invocationContext.proceed();
		
		long time = System.currentTimeMillis() - start;
		
		LogHelper.performance(invocationContext.getMethod().getDeclaringClass().getName() + " ; " + invocationContext.getMethod().getName() + " ; " + time + " ; " + time/1000.0);
		
		return o;
	}
}
