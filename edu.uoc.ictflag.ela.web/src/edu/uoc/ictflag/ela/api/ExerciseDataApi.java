package edu.uoc.ictflag.ela.api;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.uoc.ictflag.core.log.LogHelper;
import edu.uoc.ictflag.ela.bll.IExerciseDataManager;
import edu.uoc.ictflag.ela.model.ExerciseDataRaw;

@Stateless
@LocalBean
@Path("/exercise-data")
public class ExerciseDataApi
{
	@Inject
	IExerciseDataManager exerciseDataManager;
	
	//	@POST
	//	@Consumes(MediaType.APPLICATION_JSON)
	//	public Response processEventData(String dataRaw) throws Exception
	//	{
	//		try
	//		{
	//			System.out.println(dataRaw);
	//			
	//			return Response.ok().build();
	//		}
	//		catch (Exception ex)
	//		{
	//			LogHelper.error(ex);
	//			
	//			return Response.serverError().build();
	//		}
	//	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response processExerciseData(ExerciseDataRaw exerciseDataRaw) throws Exception
	{
		try
		{
			List<String> errors = exerciseDataManager.processExerciseDataRaw(exerciseDataRaw);
			
			if (errors.size() > 0)
			{
				StringBuilder sb = new StringBuilder();
				
				sb.append("Exercise Data API errors. Exercise data:");
				sb.append(System.lineSeparator());
				sb.append(exerciseDataRaw.toString());
				sb.append(System.lineSeparator());
				sb.append("Errors:");
				sb.append(System.lineSeparator());
				
				for (String error : errors)
				{
					sb.append(error);
					sb.append(System.lineSeparator());
				}
				
				LogHelper.error(sb.toString());
			}
			
			return Response.ok().build();
		}
		catch (Exception ex)
		{
			LogHelper.error(ex);
			
			return Response.serverError().build();
		}
	}
	
	//	@GET
	//	@Produces(MediaType.APPLICATION_JSON)
	//	@Path("{id}")
	//	public String read(@PathParam("id") long id)
	//	{
	//		return "Hello World! " + id;
	//	}
}
