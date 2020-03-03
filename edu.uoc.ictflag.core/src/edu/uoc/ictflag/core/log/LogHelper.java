package edu.uoc.ictflag.core.log;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogHelper implements Serializable
{
	private static final long serialVersionUID = 1L;

	private static Logger getLogger ()
	{
		return LogManager.getLogger();
	}
	
	public static void debug (String msg)
	{
		getLogger().debug(msg);
	}
	
	public static void error (String msg)
	{
		getLogger().error(msg);		
	}

	public static void error (Throwable err)
	{
		err.printStackTrace();
		
		getLogger().error("", err);		
	}

	public static void error (String msg, Throwable err)
	{
		getLogger().error(msg, err);		
	}

	public static void info (String msg)
	{
		getLogger().info(msg);		
	}

	public static void trace (String msg)
	{
		getLogger().trace(msg);		
	}

	public static void performance (String msg)
	{
		LogManager.getLogger("performance-log").trace(msg);
	}
	
	public static void activity(String msg)
	{
		LogManager.getLogger("activity-log").trace(msg);
	}
}
