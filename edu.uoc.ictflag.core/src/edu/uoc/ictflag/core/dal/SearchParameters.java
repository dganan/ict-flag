package edu.uoc.ictflag.core.dal;

import java.util.List;
import java.util.Map;

public class SearchParameters
{
	int page;
	int pageSize;
	
	Map<String, List<String>> filters;
	
	List<String> groupFields;
	
	List<SortArgument> sortArguments;
	
	public SearchParameters()
	{
		
	}
	
	public SearchParameters(Map<String, List<String>> filters, List<SortArgument> sortArguments)
	{
		this.page = -1;
		this.pageSize = -1;
		this.filters = filters;
		this.sortArguments = sortArguments;
	}
	
	public SearchParameters(int page, int pageSize, Map<String, List<String>> filters, List<SortArgument> sortArguments)
	{
		this.page = page;
		this.pageSize = pageSize;
		this.filters = filters;
		this.sortArguments = sortArguments;
	}
	
	public int getPage()
	{
		return page;
	}
	
	public void setPage(int page)
	{
		this.page = page;
	}
	
	public int getPageSize()
	{
		return pageSize;
	}
	
	public void setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
	}
	
	public Map<String, List<String>> getFilters()
	{
		return filters;
	}
	
	public void setFilters(Map<String, List<String>> filters)
	{
		this.filters = filters;
	}
	
	public List<String> getGroupFields()
	{
		return groupFields;
	}
	
	public void setGroupFields(List<String> groupFields)
	{
		this.groupFields = groupFields;
	}
	
	public List<SortArgument> getSortArguments()
	{
		return sortArguments;
	}
	
	public void setSortArguments(List<SortArgument> sortArguments)
	{
		this.sortArguments = sortArguments;
	}
}
