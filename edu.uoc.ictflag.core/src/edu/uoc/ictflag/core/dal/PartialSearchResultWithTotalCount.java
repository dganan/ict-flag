package edu.uoc.ictflag.core.dal;

import java.util.List;

public class PartialSearchResultWithTotalCount<T>
{
	List<T> results;
	long totalCount;
	
	public PartialSearchResultWithTotalCount(List<T> results, long totalCount)
	{
		this.results = results;
		this.totalCount = totalCount;
	}
	
	public List<T> getResults()
	{
		return results;
	}
	
	public void setResults(List<T> results)
	{
		this.results = results;
	}
	
	public long getTotalCount()
	{
		return totalCount;
	}
	
	public void setTotalCount(long totalCount)
	{
		this.totalCount = totalCount;
	}
}
