package edu.uoc.ictflag.ela.dal;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import edu.uoc.ictflag.core.log.LogHelper;
import edu.uoc.ictflag.ela.model.AssignmentDataRaw;
import edu.uoc.ictflag.ela.model.ExerciseDataRaw;

@Stateless
@Transactional(TxType.REQUIRES_NEW)
public class ReportsEtlRepository extends ELABaseRepository implements IReportsEtlRepository
{
	IExerciseDataRepository exerciseDataRepository;
	IAssignmentDataRepository assignmentDataRepository;
	
	@Inject
	public ReportsEtlRepository(IExerciseDataRepository exerciseDataRepository, IAssignmentDataRepository assignmentDataRepository)
	{
		this.exerciseDataRepository = exerciseDataRepository;
		this.assignmentDataRepository = assignmentDataRepository;
	}
	
	@Override
	public void startReportsEtlProcess() throws Exception
	{
		try
		{
			exerciseDataEtlProcess();
			
			assignmentDataEtlProcess();
		}
		catch (Exception e)
		{
			LogHelper.error(e);
			
			throw e;
		}
	}
	
	public final int limit = 2000;
	
	@Override
	public void exerciseDataEtlProcess() throws Exception
	{
		// Get all AssignmentDataRaw pending to process and process them one by one
		
		List<Object[]> data = dbHelper.getNativeQueryResult("SELECT * FROM ExerciseDataRaw WHERE handled = FALSE ORDER BY timestamp LIMIT " + limit);
		
		while (data.size() > 0)
		{
			for (Object[] raw : data)
			{
				try
				{
					ExerciseDataRaw edr = new ExerciseDataRaw(raw);
					
					exerciseDataRepository.processExerciseDataRaw(edr);
				}
				catch (Exception ex)
				{
					LogHelper.error(ex);
				}
			}
			
			dbHelper.clearContext();
			
			data = dbHelper.getNativeQueryResult("SELECT * FROM ExerciseDataRaw WHERE handled = FALSE ORDER BY timestamp LIMIT " + limit);
		}
	}
	
	@Override
	public void assignmentDataEtlProcess() throws Exception
	{
		// Get all AssignmentDataRaw pending to process and process them one by one
		
		List<Object[]> data = dbHelper
				.getNativeQueryResult("SELECT * FROM AssignmentDataRaw WHERE handled = FALSE ORDER BY timestamp LIMIT " + limit);
		
		while (data.size() > 0)
		{
			for (Object[] raw : data)
			{
				try
				{
					AssignmentDataRaw adr = new AssignmentDataRaw(raw);
					
					assignmentDataRepository.processAssignmentDataRaw(adr);
				}
				catch (Exception ex)
				{
					LogHelper.error(ex);
				}
			}
			
			dbHelper.clearContext();
			
			data = dbHelper.getNativeQueryResult("SELECT * FROM AssignmentDataRaw WHERE handled = FALSE ORDER BY timestamp LIMIT " + limit);
		}
	}
}
