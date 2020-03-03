package edu.uoc.ictflag.institution.web;

import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;

import edu.uoc.ictflag.core.StringUtils;
import edu.uoc.ictflag.core.log.ActivityLog;
import edu.uoc.ictflag.core.log.LogHelper;
import edu.uoc.ictflag.core.web.NavigationHelper;
import edu.uoc.ictflag.core.web.SessionBean;
import edu.uoc.ictflag.core.web.URLHelper;
import edu.uoc.ictflag.core.web.ValidationHelper;
import edu.uoc.ictflag.institution.bll.ISubjectManager;
import edu.uoc.ictflag.institution.model.Subject;

@Named
@ViewScoped
public class SubjectController implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Valid
	private Subject subject;
	
	@Inject
	ValidationHelper validationHelper;
	
	@Inject
	URLHelper urlHelper;
	
	@Inject
	ISubjectManager subjectManager;
	
	public SubjectController()
	{
	}
	
	@ActivityLog
	public void initialize() throws Exception
	{
		try
		{
			if (!FacesContext.getCurrentInstance().isPostback())
			{
				String id = urlHelper.getQueryAttribute("id");
				
				Long l = StringUtils.tryParse(id, null);
				
				if (l == null)
				{
					subject = new Subject();
				}
				else
				{
					subject = subjectManager.getSubject(SessionBean.getUsername(), l);
				}
			}
		}
		catch (Exception e)
		{
			LogHelper.error(e);
			
			NavigationHelper.redirectTo404();
		}
	}
	
	public Subject getSubject()
	{
		return subject;
	}
	
	public void setSubject(Subject subject)
	{
		this.subject = subject;
	}
	
	@ActivityLog
	public String save() throws Exception
	{
		if (validationHelper.validate(this))
		{
			subjectManager.createOrUpdateSubject(SessionBean.getUsername(), subject);
			
			return InstitutionConstants.SubjectsListPageUrl + URLHelper.facesRedirectTrue;
		}
		
		return "invalid";
	}
}
