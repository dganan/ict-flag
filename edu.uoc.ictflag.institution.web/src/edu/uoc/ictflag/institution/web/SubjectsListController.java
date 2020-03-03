package edu.uoc.ictflag.institution.web;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import edu.uoc.ictflag.core.IctFlagValidationException;
import edu.uoc.ictflag.core.localization.LocalizationController;
import edu.uoc.ictflag.core.log.ActivityLog;
import edu.uoc.ictflag.core.web.SessionBean;
import edu.uoc.ictflag.core.web.ValidationHelper;
import edu.uoc.ictflag.institution.bll.ISubjectManager;
import edu.uoc.ictflag.institution.model.Subject;

@Named
@ViewScoped
public class SubjectsListController implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private List<Subject> subjectsList;
	
	@Inject
	ISubjectManager subjectManager;
	
	public SubjectsListController()
	{
	}
	
	@ActivityLog
	public List<Subject> getSubjectsList() throws Exception
	{
		if (subjectsList == null)
		{
			refreshList();
		}
		
		return subjectsList;
	}
	
	@ActivityLog
	public void deleteSubject(long id) throws Exception
	{
		try
		{
			subjectManager.deleteSubject(SessionBean.getUsername(), id);
			
			refreshList();
		}
		catch (IctFlagValidationException ve)
		{
			ValidationHelper.addValidationMessage(LocalizationController.getLocalizedString(ve.getErrorKey()));
		}
	}
	
	private void refreshList() throws Exception
	{
		subjectsList = subjectManager.getSubjectsList(SessionBean.getUsername());
	}
}
