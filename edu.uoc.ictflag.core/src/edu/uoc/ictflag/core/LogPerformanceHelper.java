package edu.uoc.ictflag.core;

import java.text.DecimalFormat;

import edu.uoc.ictflag.core.log.LogHelper;

public class LogPerformanceHelper
{
	static DecimalFormat timeFormat = new DecimalFormat("#.##");
	static DecimalFormat megaBytesFormat = new DecimalFormat("#.####");
	
	static boolean logTime;
	static boolean logMemory;
	
	static long lastTime;
	static double lastMemory;
	
	public static void startTimePerformanceLog()
	{
		startPerformanceLogInternal(true, false);
	}
	
	public static void startMemoryPerformanceLog ()
	{
		startPerformanceLogInternal(false, true);
	}
	
	public static void startTimeAndMemoryPerformanceLog()
	{
		startPerformanceLogInternal(true, true);
	}
	
	private static void startPerformanceLogInternal(boolean time, boolean memory)
	{
		logTime = time;
		
		logMemory = memory;
		
		if (logTime)
		{
			lastTime = System.currentTimeMillis();
		}
		
		if (logMemory)
		{
			lastMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576.0; // 1048576 = 1024 * 1024 ==> Mb
		}
	}
	
	public static void logPerformance(String method)
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(method);
		
		if (logTime)
		{
			long time = System.currentTimeMillis() - lastTime;
		
			sb.append(";" + timeFormat.format(time / 1000.0));
		
			lastTime = System.currentTimeMillis();
		}
		
		if (logMemory)
		{
			double occupiedMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576.0; // 1048576 = 1024 * 1024 ==> Mb
			
			sb.append(";" + megaBytesFormat.format(occupiedMemory) + ";" + megaBytesFormat.format(occupiedMemory - lastMemory));
			
			lastMemory = occupiedMemory;
		}
		
		LogHelper.performance(sb.toString());
	}
}
