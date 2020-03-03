package edu.uoc.ictflag.institution.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import edu.uoc.ictflag.core.dal.PartialSearchResultWithTotalCount;
import edu.uoc.ictflag.core.dal.SearchParameters;
import edu.uoc.ictflag.core.dal.SortArgument;
import edu.uoc.ictflag.core.log.LogHelper;
import edu.uoc.ictflag.core.web.SessionBean;
import edu.uoc.ictflag.institution.bll.ICourseGroupManager;
import edu.uoc.ictflag.security.model.User;

public class LazyUserDataModel extends LazyDataModel<User>
{
	private static final long serialVersionUID = 1L;
	
	ICourseGroupManager courseGroupManager;
	
	private List<User> datasource;
	
	public List<User> getDataSource()
	{
		return datasource;
	}
	
	public LazyUserDataModel(ICourseGroupManager courseGroupManager)
	{
		this.courseGroupManager = courseGroupManager;
    }
	
	@Override
	public User getRowData(String rowKey)
	{
		for (User user : datasource)
		{
			if (user.getId().equals(rowKey)) return user;
		}
		
		return null;
	}
	
	@Override
	public Object getRowKey(User user)
	{
		return user.getId();
	}
	
	@Override
	public List<User> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters)
	{
		try
		{
			List<SortArgument> sortFields = new ArrayList<SortArgument>();
			sortFields.add(new SortArgument(sortField, sortOrder == SortOrder.DESCENDING));
			
			Map<String, List<String>> filters2 = filters.entrySet().stream()
					.collect(Collectors.toMap(e -> e.getKey(), e -> new ArrayList<String>(Arrays.asList(new String[] { e.getValue().toString() }))));
			
			SearchParameters searchParameters = new SearchParameters(first / pageSize, pageSize, filters2, sortFields);
			
			PartialSearchResultWithTotalCount<User> result = courseGroupManager.getAvailableMembersList(SessionBean.getUsername(), searchParameters);
			
			datasource = result.getResults();
			
			this.setRowCount((int) result.getTotalCount());
			
			return datasource;
		}
		catch (Exception e)
		{
			LogHelper.error(e);
		}
		
		return null;
		
		//		List<User> data = new ArrayList<User>();
		//		
		//		//filter
		//		for (User user : datasource)
		//		{
		//			boolean match = true;
		//			
		//			if (filters != null)
		//			{
		//				for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();)
		//				{
		//					try
		//					{
		//						String filterProperty = it.next();
		//						Object filterValue = filters.get(filterProperty);
		//						String fieldValue = String.valueOf(user.getClass().getField(filterProperty).get(user));
		//						
		//						if (filterValue == null || fieldValue.startsWith(filterValue.toString()))
		//						{
		//							match = true;
		//						}
		//						else
		//						{
		//							match = false;
		//							break;
		//						}
		//					}
		//					catch (Exception e)
		//					{
		//						match = false;
		//					}
		//				}
		//			}
		//			
		//			if (match)
		//			{
		//				data.add(user);
		//			}
		//		}
		//		
		//		//sort
		//		if (sortField != null)
		//		{
		//			Collections.sort(data, new LazyUserSorter(sortField, sortOrder));
		//		}
		//		
		//		//rowCount
		//		int dataSize = data.size();
		//		this.setRowCount(dataSize);
		//		
		//		//paginate
		//		if (dataSize > pageSize)
		//		{
		//			try
		//			{
		//				return data.subList(first, first + pageSize);
		//			}
		//			catch (IndexOutOfBoundsException e)
		//			{
		//				return data.subList(first, first + (dataSize % pageSize));
		//			}
		//		}
		//		else
		//		{
		//			return data;
		//		}
	}
}
