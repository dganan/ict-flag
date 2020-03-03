package edu.uoc.ictflag.ela.bll;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import edu.uoc.ictflag.ela.dal.IExerciseDataRepository;
import edu.uoc.ictflag.ela.dal.IToolRepository;
import edu.uoc.ictflag.ela.model.ExerciseAction;
import edu.uoc.ictflag.ela.model.ExerciseDataRaw;
import edu.uoc.ictflag.ela.model.Tool;

@Stateless
public class ExerciseDataManager implements IExerciseDataManager, Serializable
{
	private static final long serialVersionUID = 1L;
	
	IExerciseDataRepository exerciseDataRepository;
	IToolRepository toolRepository;
	
	@Inject
	public ExerciseDataManager(IExerciseDataRepository exerciseDataRepository, IToolRepository toolRepository)
	{
		this.exerciseDataRepository = exerciseDataRepository;
		this.toolRepository = toolRepository;
	}
	
	@Override
	public List<String> processExerciseDataRaw(ExerciseDataRaw exerciseDataRaw) throws Exception
	{
		List<String> errors = checkExerciseData(exerciseDataRaw);
		
		if (errors.size() == 0)
		{
			exerciseDataRepository.saveExerciseDataRaw(exerciseDataRaw);
		}
		
		return errors;
	}
	
	private List<String> checkExerciseData(ExerciseDataRaw exerciseDataRaw) throws Exception
	{
		List<String> errors = new ArrayList<String>();
		
		// Check tool and UUID
		Tool t = toolRepository.getToolByCode(exerciseDataRaw.getTool());
		
		if (t == null)
		{
			errors.add("Tool '" + exerciseDataRaw.getTool() + "' does not exist.");
		}
		else
		{
			if (exerciseDataRaw.getToolUUID() == null || !exerciseDataRaw.getToolUUID().equals(t.getUuid()))
			{
				errors.add("Tool key for tool '" + exerciseDataRaw.getTool() + "' is not valid.");
			}
		}
		
		// Check user
		
		if (exerciseDataRaw.getUserId() == null || exerciseDataRaw.getUserId().trim().equals(""))
		{
			errors.add("User field is mandatory.");
		}
		
		// Check exercise
		
		if (exerciseDataRaw.getExercise() == null || exerciseDataRaw.getExercise().trim().equals(""))
		{
			errors.add("Exercise field is mandatory.");
		}
		
		// Check action
		
		try
		{
			ExerciseAction ea = ExerciseAction.fromString(exerciseDataRaw.getAction());

			// Check grade
			
			if (ea == ExerciseAction.EXERCISE_ASSESSMENT)
			{
				if (exerciseDataRaw.getGrade() == null || exerciseDataRaw.getGrade().trim().equals(""))
				{
					errors.add("Grade field is mandatory in assessment actions.");
				}
				else
				{
					try
					{
						Double.parseDouble(exerciseDataRaw.getGrade());
					}
					catch (NumberFormatException nfe)
					{
						// try changing , with .
						
						String grade = exerciseDataRaw.getGrade().replace(',', '.');
						
						Double.parseDouble(grade);
						
						exerciseDataRaw.setGrade(grade);
					}
				}
			}
		}
		catch (NumberFormatException nfe)
		{
			errors.add("Incorrect grade: " + exerciseDataRaw.getGrade());
		}
		catch (IllegalArgumentException e)
		{
			errors.add("Incorrect exercise action: " + exerciseDataRaw.getAction());
		}
		
		return errors;
	}
}

