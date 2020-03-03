package edu.uoc.ictflag.ela.web;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Scanner;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import javax.validation.constraints.NotNull;

import edu.uoc.ictflag.core.IFileUploaderListener;
import edu.uoc.ictflag.core.log.ActivityLog;
import edu.uoc.ictflag.core.log.LogHelper;
import edu.uoc.ictflag.core.web.NavigationHelper;
import edu.uoc.ictflag.core.web.ValidationHelper;
import edu.uoc.ictflag.ela.bll.IAssignmentDataManager;

@Named
@ViewScoped
public class AssignmentAssessmentController implements Serializable, IFileUploaderListener
{
	private static final long serialVersionUID = 1L;
	
	@NotNull
	private Part file;
	
	private boolean success;
	
	@Inject
	IAssignmentDataManager assignmentDataManager;
	
	@ActivityLog
	public void initialize() throws Exception
	{
		try
		{
		}
		catch (Exception e)
		{
			LogHelper.error(e);
			
			NavigationHelper.redirectTo404();
		}
	}
	
	@Override
	public Part getFile()
	{
		return file;
	}
	
	@Override
	public void setFile(Part file)
	{
		this.file = file;
	}
	
	@Override
	public void upload() throws Exception
	{
		try
		{
			if (file == null)
			{
				ValidationHelper.addValidationMessage("A file must be uploaded.");
				
				return;
			}
			
			Scanner s = new Scanner(file.getInputStream());
			
			String fileContent = s.useDelimiter("\\A").next();
			
			s.close();
			
			List<String> errors = assignmentDataManager.importAssignmentAssessmentData(fileContent);
			
			if (errors.size() > 0)
			{
				StringBuilder sb = new StringBuilder ();
				
				sb.append("Assignment Assessment Import with errors. File Content: ");
				sb.append(System.lineSeparator());
				sb.append(fileContent);
				sb.append(System.lineSeparator());
				sb.append("Errors: ");
				sb.append(System.lineSeparator());
				
				for (String error : errors)
				{
					ValidationHelper.addValidationMessage(error);
					
					sb.append(error);
					sb.append(System.lineSeparator());
				}
				
				setSuccess(false);
				
				LogHelper.error(sb.toString());
			}
			else
			{
				setSuccess(true);
			}
		}
		catch (IOException e)
		{
			LogHelper.error(e);
		}
	}
	
	public boolean isSuccess()
	{
		return success;
	}
	
	public void setSuccess(boolean success)
	{
		this.success = success;
	}
}
