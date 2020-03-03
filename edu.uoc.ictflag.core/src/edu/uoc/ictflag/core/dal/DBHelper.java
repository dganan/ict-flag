package edu.uoc.ictflag.core.dal;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import edu.uoc.ictflag.core.log.LogHelper;
import edu.uoc.ictflag.core.model.IIdentifiable;
import edu.uoc.ictflag.core.model.IMappeable;
import edu.uoc.ictflag.core.model.IPreUpdatable;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

@Transactional(TxType.REQUIRED)
public class DBHelper
{
	private EntityManager em;
	
	public DBHelper(EntityManager em)
	{
		this.em = em;
	}
	
	public void addEntity(IIdentifiable o) throws Exception
	{
		try
		{
			em.persist(o);
		}
		catch (Exception e)
		{
			LogHelper.error(e);
			
			throw e;
		}
	}
	
	public void updateEntity(IIdentifiable o) throws Exception
	{
		try
		{
			Object t = em.find(o.getClass(), o.getId());
			
			if (o instanceof IMappeable)
			{
				((IMappeable) o).mapTo(t);
			}
			else
			{
				MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
				
				MapperFacade mapper = mapperFactory.getMapperFacade();
				
				mapper.map(o, t);
			}
			
			if (t instanceof IPreUpdatable)
			{
				((IPreUpdatable) t).preUpdate();
			}
		}
		catch (Exception e)
		{
			LogHelper.error(e);
			
			throw e;
		}
	}
	
	public void createOrUpdateEntity(IIdentifiable o) throws Exception
	{
		try
		{
			if (o.getId() == null || o.getId() <= 0)
			{
				addEntity(o);
			}
			else
			{
				updateEntity(o);
			}
		}
		catch (Exception e)
		{
			LogHelper.error(e);
			
			throw e;
		}
	}
	
	public <T> T getEntity(long id, Class<T> c) throws Exception
	{
		try
		{
			return getEntity(id, c, true);
		}
		catch (Exception e)
		{
			LogHelper.error(e);
			
			throw e;
		}
	}
	
	public <T> T getEntity(long id, Class<T> c, boolean detach) throws Exception
	{
		try
		{
			T t = em.find(c, id);
			
			if (detach && t != null)
			{
				em.detach(t);
			}
			
			return t;
		}
		catch (Exception e)
		{
			LogHelper.error(e);
			
			throw e;
		}
	}
	
	public <T> void deleteEntity(long id, Class<T> c)
	{
		try
		{
			T t = em.find(c, id);
			
			em.remove(t);
		}
		catch (Exception e)
		{
			LogHelper.error(e);
			
			throw e;
		}
	}
	
	public Object getScalarValue(String query) throws Exception
	{
		return getScalarValue(query, null);
	}
	
	@Transactional(TxType.NOT_SUPPORTED)
	public Object getScalarValue(String query, Map<String, Object> parameters) throws Exception
	{
		try
		{
			Query q = em.createQuery(query);
			
			if (parameters != null)
			{
				q = addQueryParameters(q, parameters);
			}
			
			return q.getSingleResult();
		}
		catch (Exception e)
		{
			LogHelper.error(e);
			
			throw e;
		}
	}
	
	@Transactional(TxType.NOT_SUPPORTED)
	public Object getScalarValueNative(String query, List<Object> parameters) throws Exception
	{
		try
		{
			Query q = em.createNativeQuery(query);
			
			if (parameters != null)
			{
				int index = 1;
				
				for (Object o : parameters)
				{
					if (o instanceof Date)
					{
						Date d = (Date) o;
						
						q = q.setParameter(index, d, TemporalType.DATE);
					}
					else
					{
						q = q.setParameter(index, o);
					}
					
					index++;
				}
			}
			
			return q.getSingleResult();
		}
		catch (Exception e)
		{
			LogHelper.error(e);
			
			throw e;
		}
	}
	
	public <T> T getFirst(String query, Class<T> c) throws Exception
	{
		return getFirst(query, c, null, null, true);
	}
	
	public <T> T getFirst(String query, Class<T> c, boolean detach) throws Exception
	{
		return getFirst(query, c, null, null, detach);
	}
	
	public <T> T getFirst(String query, Class<T> c, Map<String, Object> parameters) throws Exception
	{
		return getFirst(query, c, parameters, null, true);
	}
	
	public <T> T getFirst(String query, Class<T> c, Map<String, Object> parameters, boolean detach) throws Exception
	{
		return getFirst(query, c, parameters, null, detach);
	}
	
	public <T> T getFirst(String query, Class<T> c, Map<String, Object> parameters, Map<String, Object> viewParameters, boolean detach)
			throws Exception
	{
		try
		{
			T t = null;
			
			List<T> list = getQueryResult(query, c, parameters, viewParameters);
			
			if (list.size() > 0)
			{
				t = list.get(0);
				
				if (detach)
				{
					em.detach(t);
				}
			}
			
			return t;
		}
		catch (Exception e)
		{
			LogHelper.error(e);
			
			throw e;
		}
	}
	
	public List<Object[]> getNativeQueryResult(String query) throws Exception
	{
		return getQueryResult(query, null, -1, -1, null, null, false, true);
	}
	
	public <T> List<T> getNativeQueryResult(String query, Class<T> c) throws Exception
	{
		return getQueryResult(query, c, -1, -1, null, null, false, true);
	}
	
	public List<Object[]> getNativeQueryResult(String query, Map<String, Object> parameters) throws Exception
	{
		return getQueryResult(query, null, -1, -1, parameters, null, false, true);
	}
	
	public <T> List<T> getQueryResult(String query, Class<T> c) throws Exception
	{
		return getQueryResult(query, c, -1, -1, null);
	}
	
	public <T> List<T> getQueryResult(String query, Class<T> c, int page, int pageSize) throws Exception
	{
		return getQueryResult(query, c, page, pageSize, null);
	}
	
	public <T> List<T> getQueryResult(String query, Class<T> c, Map<String, Object> parameters) throws Exception
	{
		return getQueryResult(query, c, -1, -1, parameters);
	}
	
	public <T> List<T> getQueryResult(String query, Class<T> c, Map<String, Object> parameters, boolean detach) throws Exception
	{
		return getQueryResult(query, c, -1, -1, parameters, null, detach, false);
	}
	
	public <T> List<T> getQueryResult(String query, Class<T> c, Map<String, Object> parameters, Map<String, Object> viewParameters) throws Exception
	{
		return getQueryResult(query, c, -1, -1, parameters, viewParameters, false, false);
	}
	
	public <T> List<T> getQueryResult(String query, Class<T> c, int page, int pageSize, Map<String, Object> parameters) throws Exception
	{
		return getQueryResult(query, c, page, pageSize, parameters, null, false, false);
	}
	
	public <T> List<T> getQueryResult(String query, Class<T> c, int page, int pageSize, Map<String, Object> parameters,
			Map<String, Object> viewParameters, boolean detach, boolean nativeQuery) throws Exception
	{
		try
		{
			Query q = null;
			
			if (nativeQuery)
			{
				q = em.createNativeQuery(query);
			}
			else
			{
				q = c != null ? em.createQuery(query, c) : em.createQuery(query);
			}
			
			if (viewParameters != null)
			{
				addQueryViewParameters(viewParameters);
			}
			
			if (parameters != null)
			{
				q = addQueryParameters(q, parameters);
			}
			
			if (page > 0)
			{
				q.setFirstResult((page - 1) * pageSize);
				q.setMaxResults(pageSize);
			}
			
			@SuppressWarnings("unchecked")
			List<T> results = q.getResultList();
			
			if (detach)
			{
				for (T t : results)
				{
					em.detach(t);
				}
			}
			
			return results;
			
			//		List<T> results = new ArrayList<T>();
			//		
			//		for (T t : qr)
			//		{
			//			T t2 = DeepClone.clone(t);
			//			
			//			results.add(t2);
			//		}
		}
		catch (Exception e)
		{
			LogHelper.error(e);
			
			throw e;
		}
	}
	
	public <T> PartialSearchResultWithTotalCount<T> getQueryResult(String query, String countQuery, SearchParameters searchParameters, Class<T> c,
			Map<String, Object> parameters)
	{
		return getQueryResult(query, countQuery, searchParameters, c, parameters, null, false);
	}
	
	public <T> PartialSearchResultWithTotalCount<T> getQueryResult(String query, String countQuery, SearchParameters searchParameters, Class<T> c,
			Map<String, Object> parameters, boolean detach)
	{
		return getQueryResult(query, countQuery, searchParameters, c, parameters, null, detach);
	}
	
	public <T> PartialSearchResultWithTotalCount<T> getQueryResult(String query, String countQuery, SearchParameters searchParameters, Class<T> c,
			Map<String, Object> parameters, Map<String, Object> viewParameters, boolean detach)
	{
		try
		{
			StringBuilder sb = new StringBuilder(query);
			StringBuilder sb2 = new StringBuilder(countQuery);
			
			//			if (searchParameters != null)
			//			{
			//				if (searchParameters.getFilters() != null)
			//				{
			//					if (!query.toUpperCase().contains("WHERE"))
			//					{
			//						sb.append(" WHERE ");
			//					}
			//					else
			//					{
			//						sb.append(" AND ");
			//					}
			//					
			//					boolean first = true;
			//					
			//					for (String key : searchParameters.getFilters().keySet())
			//					{
			//						Object o = parameters.get(key);
			//						
			//						if (!first)
			//						{
			//							sb.append(" AND ");
			//						}
			//						
			//						sb.append(" :arg_" + key + " LIKE '%" + o + "%' ");
			//						
			//						first = false;
			//						
			//						// add new parameter to query parameters
			//						parameters.put("arg_" + key, o);
			//					}
			//				}
			//				
			//				if (searchParameters.getSortFields() != null && searchParameters.getSortFields().size() > 0)
			//				{
			//					sb.append(" ORDER BY ");
			//					
			//					boolean first = true;
			//					
			//					for (SortArgument sa : searchParameters.getSortFields())
			//					{
			//						if (!first)
			//						{
			//							sb.append(", ");
			//						}
			//						
			//						sb.append(sa.getField());
			//						
			//						sb.append(sa.isAscending() ? " ASC " : " DESC");
			//						
			//						first = false;
			//					}
			//				}
			//			}
			
			Query q = c != null ? em.createQuery(sb.toString(), c) : em.createQuery(sb.toString());
			
			// VIEW PARAMETERS
			if (viewParameters != null)
			{
				addQueryViewParameters(viewParameters);
			}
			
			// PARAMETERS
			if (parameters != null)
			{
				q = addQueryParameters(q, parameters);
			}
			
			// PAGINATION
			if (searchParameters != null && searchParameters.getPage() > 0)
			{
				q.setFirstResult(searchParameters.getPage() * searchParameters.getPageSize());
				q.setMaxResults(searchParameters.getPageSize());
			}
			
			@SuppressWarnings("unchecked")
			List<T> results = q.getResultList();
			
			if (detach)
			{
				for (T t : results)
				{
					em.detach(t);
				}
			}
			
			// QUERY COUNT
			
			Query cq = c != null ? em.createQuery(sb2.toString(), c) : em.createQuery(sb2.toString());
			
			// VIEW PARAMETERS
			if (viewParameters != null)
			{
				addQueryViewParameters(viewParameters);
			}
			
			// PARAMETERS
			if (parameters != null)
			{
				cq = addQueryParameters(cq, parameters);
			}
			
			long totalCount = (long) cq.getSingleResult();
			
			return new PartialSearchResultWithTotalCount<T>(results, totalCount);
			
			//		List<T> results = new ArrayList<T>();
			//		
			//		for (T t : qr)
			//		{
			//			T t2 = DeepClone.clone(t);
			//			
			//			results.add(t2);
			//		}
		}
		catch (Exception e)
		{
			LogHelper.error(e);
			
			throw e;
		}
	}
	
	private void addQueryViewParameters(Map<String, Object> viewParameters)
	{
		for (String key : viewParameters.keySet())
		{
			Object o = viewParameters.get(key);
			
			Query qp = em.createQuery("SET @" + key + " = " + o + ";");
			
			qp.executeUpdate();
		}
	}

	private Query addQueryParameters(Query q, Map<String, Object> parameters)
	{
		for (String key : parameters.keySet())
		{
			Object o = parameters.get(key);
			
			if (o instanceof Date)
			{
				Date d = (Date) o;
				
				q = q.setParameter(key, d, TemporalType.DATE);
			}
			else
			{
				q = q.setParameter(key, o);
			}
		}
		
		return q;
	}
	
	public int executeUpdate(String query) throws Exception
	{
		return executeUpdate(query, null);
	}
	
	public int executeUpdate(String query, Map<String, Object> parameters) throws Exception
	{
		try
		{
			Query q = em.createQuery(query);
			
			if (parameters != null)
			{
				for (String key : parameters.keySet())
				{
					Object o = parameters.get(key);
					
					if (o instanceof Date)
					{
						Date d = (Date) o;
						
						q = q.setParameter(key, d, TemporalType.DATE);
					}
					else
					{
						q = q.setParameter(key, o);
					}
				}
			}
			
			return q.executeUpdate();
		}
		catch (Exception e)
		{
			LogHelper.error(e);
			
			throw e;
		}
	}
	
	public void executeUpdateNative(String query, List<Object> parameters)
	{
		try
		{
			Query q = em.createNativeQuery(query);
			
			if (parameters != null)
			{
				int index = 1;
				
				for (Object o : parameters)
				{
					if (o instanceof Date)
					{
						Date d = (Date) o;
						
						q = q.setParameter(index, d, TemporalType.DATE);
					}
					else
					{
						q = q.setParameter(index, o);
					}
					
					index++;
				}
			}
			
			q.executeUpdate();
		}
		catch (Exception e)
		{
			LogHelper.error(e);
			
			throw e;
		}
	}
	
	public void clearContext()
	{
		em.flush();
		em.clear();
	}
	
	public void flush()
	{
		em.flush();
	}
	
	//	protected String mapArrayAsParameters(Long[] sensorsids, Map<String, Object> parameters)
	//	{
	//		String params = "";
	//		
	//		for (int i = 1; i <= sensorsids.length; i++)
	//		{
	//			parameters.put(i + "", sensorsids[i - 1]);
	//			
	//			params = params + ":" + i + ",";
	//		}
	//		
	//		if (sensorsids.length > 0)
	//		{
	//			params = params.substring(0, params.length() - 1);
	//		}
	//		
	//		return params;
	//	}
}
