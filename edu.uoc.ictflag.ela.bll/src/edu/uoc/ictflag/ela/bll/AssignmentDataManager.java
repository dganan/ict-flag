package edu.uoc.ictflag.ela.bll;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import edu.uoc.ictflag.ela.dal.IAssignmentDataRepository;
import edu.uoc.ictflag.ela.dal.IToolRepository;
import edu.uoc.ictflag.ela.model.AssignmentAction;
import edu.uoc.ictflag.ela.model.AssignmentDataRaw;
import edu.uoc.ictflag.ela.model.Tool;

@Stateless
public class AssignmentDataManager implements IAssignmentDataManager, Serializable
{
	private static final long serialVersionUID = 1L;
	
	IAssignmentDataRepository assignmentDataRepository;
	IToolRepository toolRepository;
	
	@Inject
	public AssignmentDataManager(IAssignmentDataRepository assignmentDataRepository, IToolRepository toolRepository)
	{
		this.assignmentDataRepository = assignmentDataRepository;
		this.toolRepository = toolRepository;
	}
	
	@Override
	public List<String> processAssignmentDataRaw(AssignmentDataRaw assignmentDataRaw) throws Exception
	{
		List<String> errors = checkAssignmentData(assignmentDataRaw);
		
		if (errors.size() == 0)
		{
			assignmentDataRepository.saveAssignmentDataRaw(assignmentDataRaw);
		}
		
		return errors;
	}
	
	@Override
	public List<String> importAssignmentAssessmentData(String importText) throws Exception
	{
		ArrayList<AssignmentDataRaw> listad = new ArrayList<AssignmentDataRaw>();
		
		List<String> errors = new ArrayList<String>();
		
		String[] lines = importText.split("\\r?\\n");
		
		int lineindex = 0;
		
		for (String line : lines)
		{
			lineindex++;
			
			String[] parts = line.split("[;,]");
			
			if (parts.length != 5 && parts.length != 8)
			{
				errors.add("Line " + lineindex + ": Incorrect number of columns.");
				
				continue;
			}
			
			AssignmentDataRaw assignmentDataRaw = new AssignmentDataRaw();
			
			assignmentDataRaw.setAction(AssignmentAction.ASSIGNMENT_ASSESSMENT.toString());
			
			assignmentDataRaw.setTool(parts[0]);
			assignmentDataRaw.setToolUUID(parts[1]);
			assignmentDataRaw.setUserId(parts[2]);
			assignmentDataRaw.setAssignment(parts[3]);
			assignmentDataRaw.setGrade(parts[4]);
			
			if (parts.length == 7)
			{
				assignmentDataRaw.setProgramCode(parts[5]);
				assignmentDataRaw.setSubjectCode(parts[6]);
				assignmentDataRaw.setSemester(parts[7]);
			}
			
			listad.add(assignmentDataRaw);
		
			List<String> lineErrors = checkAssignmentData(assignmentDataRaw);
			
			for (String error : lineErrors)
			{
				errors.add("Line " + lineindex + ":" + error);
			}
		}
		
		if (errors.size() == 0)
		{
			for (AssignmentDataRaw dr : listad)
			{
				assignmentDataRepository.saveAssignmentDataRaw(dr);
			}
		}
		else
		{
			errors.add(0, "No rows were inserted due to the following errors:");
		}
		
		return errors;
	}
	
	private List<String> checkAssignmentData(AssignmentDataRaw assignmentDataRaw) throws Exception
	{
		List<String> errors = new ArrayList<String>();
		
		// Check tool and UUID
		Tool t = toolRepository.getToolByCode(assignmentDataRaw.getTool());
		
		if (t == null)
		{
			errors.add("Tool '" + assignmentDataRaw.getTool() + "' does not exist.");
		}
		else
		{
			if (assignmentDataRaw.getToolUUID() == null || !assignmentDataRaw.getToolUUID().equals(t.getUuid()))
			{
				errors.add("Tool key for tool '" + assignmentDataRaw.getTool() + "' is not valid.");
			}
		}
		
		// Check user
		
		if (assignmentDataRaw.getUserId() == null || assignmentDataRaw.getUserId().trim().equals(""))
		{
			errors.add("User field is mandatory.");
		}
		
		// Check exercise
		
		if (assignmentDataRaw.getAssignment() == null || assignmentDataRaw.getAssignment().trim().equals(""))
		{
			errors.add("Assignment field is mandatory.");
		}
		
		// Check action
		
		try
		{
			AssignmentAction aa = AssignmentAction.fromString(assignmentDataRaw.getAction());
			
			// Check grade
			
			if (aa == AssignmentAction.ASSIGNMENT_ASSESSMENT)
			{
				if (assignmentDataRaw.getGrade() == null || assignmentDataRaw.getGrade().trim().equals(""))
				{
					errors.add("Grade field is mandatory in assessment actions.");
				}
				else
				{
					try
					{
						Double.parseDouble(assignmentDataRaw.getGrade());
					}
					catch (NumberFormatException nfe)
					{
						// try changing , with .
						
						String grade = assignmentDataRaw.getGrade().replace(',', '.');
						
						Double.parseDouble(grade);
						
						assignmentDataRaw.setGrade(grade);
					}
				}
			}
		}
		catch (NumberFormatException nfe)
		{
			errors.add("Incorrect grade: " + assignmentDataRaw.getGrade());
		}
		catch (IllegalArgumentException e)
		{
			errors.add("Incorrect assignment action: " + assignmentDataRaw.getAction());
		}
		
		return errors;
	}
}
