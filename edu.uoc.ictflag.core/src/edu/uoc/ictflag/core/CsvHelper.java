package edu.uoc.ictflag.core;

import java.util.ArrayList;
import java.util.List;

public class CsvHelper
{
	public static List<String[]> importCsv(String fileContent, int rows, boolean hasHeader)
	{
		ArrayList<String[]> results = new ArrayList<String[]>();
		
		String[] lines = fileContent.split("[\\r\\n]+");
		
		boolean first = true;
		
		for (String s : lines)
		{
			if (!hasHeader || !first)
			{
				String[] parts = s.split(";");
				
				String[] rowParts = new String[rows];
				
				for (int i = 0; i < rowParts.length; i++)
				{
					if (i < parts.length)
					{
						rowParts[i] = parts[i];
					}
				}
				
				results.add(rowParts);
			}
			
			first = false;
		}
		
		return results;
	}
	
	public static String exportCsv(List<String[]> content, String[] header)
	{
		StringBuilder sb = new StringBuilder();
		
		if (header != null)
		{
			sb.append(String.join(";", header));
		}
		
		for (String [] parts : content)
		{
			sb.append(String.join(";", parts));
		}
		
		return sb.toString();
	}
}
