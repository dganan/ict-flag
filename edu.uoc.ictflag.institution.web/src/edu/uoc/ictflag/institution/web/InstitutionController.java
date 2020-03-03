package edu.uoc.ictflag.institution.web;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;

import edu.uoc.ictflag.core.log.ActivityLog;
import edu.uoc.ictflag.core.log.LogHelper;
import edu.uoc.ictflag.core.web.NavigationHelper;
import edu.uoc.ictflag.core.web.SessionBean;
import edu.uoc.ictflag.core.web.URLHelper;
import edu.uoc.ictflag.core.web.URLHelper.Page;
import edu.uoc.ictflag.core.web.ValidationHelper;
import edu.uoc.ictflag.institution.bll.IInstitutionManager;
import edu.uoc.ictflag.institution.model.Institution;

@Named
@ViewScoped
public class InstitutionController implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Valid
	private Institution institution;
	
	@Inject
	ValidationHelper validationHelper;
	
	@Inject
	URLHelper urlHelper;
	
	@Inject
	IInstitutionManager institutionManager;
	
	public InstitutionController()
	{
	}
	
	@ActivityLog
	public void initialize() throws Exception
	{
		try
		{
			institution = institutionManager.getInstitution(SessionBean.getUsername());
		}
		catch (Exception e)
		{
			LogHelper.error(e);
			
			NavigationHelper.redirectTo404();
		}
	}
	
	public Institution getInstitution()
	{
		return institution;
	}
	
	public void setInstitution(Institution institution)
	{
		this.institution = institution;
	}
	
	@ActivityLog
	public String save() throws Exception
	{
		if (validationHelper.validate(this))
		{
			institutionManager.createOrUpdateInstitution(SessionBean.getUsername(), institution);
			
			return URLHelper.getRelativePath("", Page.HOME_PRIVATE, true);
		}
		
		return "invalid";
	}
}
