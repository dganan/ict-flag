package edu.uoc.ictflag.institution.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;

import org.primefaces.model.DualListModel;

import edu.uoc.ictflag.core.IIdentifiableConverter;
import edu.uoc.ictflag.core.StringUtils;
import edu.uoc.ictflag.core.log.ActivityLog;
import edu.uoc.ictflag.core.log.LogHelper;
import edu.uoc.ictflag.core.web.NavigationHelper;
import edu.uoc.ictflag.core.web.SessionBean;
import edu.uoc.ictflag.core.web.URLHelper;
import edu.uoc.ictflag.core.web.ValidationHelper;
import edu.uoc.ictflag.institution.bll.IProgramManager;
import edu.uoc.ictflag.institution.bll.ISubjectManager;
import edu.uoc.ictflag.institution.model.Program;
import edu.uoc.ictflag.institution.model.Subject;
import edu.uoc.ictflag.security.model.User;

@Named
@ViewScoped
public class ProgramController implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Valid
	private Program program;
	
	private List<User> programManagers;
	private List<Subject> subjects;
	private List<Subject> availableSubjects;
	
	private DualListModel<Subject> subjectsDualModel;
	
	private IIdentifiableConverter<User> programManagerConverter;
	private IIdentifiableConverter<Subject> subjectConverter;
	
	@Inject
	ValidationHelper validationHelper;
	
	@Inject
	URLHelper urlHelper;
	
	@Inject
	IProgramManager programManager;
	
	@Inject
	ISubjectManager subjectManager;
	
	public ProgramController()
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
					program = new Program();
				}
				else
				{
					program = programManager.getProgram(SessionBean.getUsername(), l);
				}
				
				programManagers = programManager.getProgramManagers(SessionBean.getUsername());
				
				subjects = subjectManager.getSubjectsList(SessionBean.getUsername());
				
				availableSubjects = new ArrayList<Subject>(subjects);
				
				for (Subject s : program.getSubjects())
				{
					availableSubjects.remove(s);
				}
				
				programManagerConverter = new IIdentifiableConverter<User>(programManagers);
				subjectConverter = new IIdentifiableConverter<Subject>(subjects);
				
				subjectsDualModel = new DualListModel<Subject>();
				
				subjectsDualModel.setSource(availableSubjects);
				subjectsDualModel.setTarget(new ArrayList<Subject>(program.getSubjects()));
			}
		}
		catch (Exception e)
		{
			LogHelper.error(e);
			
			NavigationHelper.redirectTo404();
		}
	}
	
	public Program getProgram()
	{
		return program;
	}
	
	public void setProgram(Program program)
	{
		this.program = program;
	}
	
	public List<User> getProgramManagers()
	{
		return programManagers;
	}
	
	public DualListModel<Subject> getSubjectsDualModel()
	{
		return subjectsDualModel;
	}
	
	public void setSubjectsDualModel(DualListModel<Subject> dlm)
	{
		subjectsDualModel = dlm;
	}
	
	public IIdentifiableConverter<User> getProgramManagerConverter()
	{
		return programManagerConverter;
	}
	
	public IIdentifiableConverter<Subject> getSubjectConverter()
	{
		return subjectConverter;
	}
	
	@ActivityLog
	public String save() throws Exception
	{
		program.setSubjects(new HashSet<Subject>(subjectsDualModel.getTarget()));
		
		if (validationHelper.validate(this))
		{
			programManager.createOrUpdateProgram(SessionBean.getUsername(), program);
			
			return InstitutionConstants.ProgramsListPageUrl + URLHelper.facesRedirectTrue;
		}
		
		return "invalid";
	}
}
