package edu.uoc.ictflag.institution.web;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import edu.uoc.ictflag.core.log.ActivityLog;
import edu.uoc.ictflag.core.web.SessionBean;
import edu.uoc.ictflag.core.web.URLHelper;
import edu.uoc.ictflag.institution.bll.IProgramManager;
import edu.uoc.ictflag.institution.model.Program;

@Named
@ViewScoped
public class ProgramsListController implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private List<Program> programsList;
	
	@Inject
	IProgramManager programManager;
	
	public ProgramsListController() throws IOException
	{
	}
	
	@ActivityLog
	public List<Program> getProgramsList() throws Exception
	{
		if (programsList == null)
		{
			refreshList();
		}
		
		return programsList;
	}
	
	@ActivityLog
	public String deleteProgram(long id) throws Exception
	{
		programManager.deleteProgram(SessionBean.getUsername(), id);
		
		refreshList();
		
		return InstitutionConstants.ProgramsListPageUrl + URLHelper.facesRedirectTrue;
	}
	
	private void refreshList() throws Exception
	{
		programsList = programManager.getProgramsList(SessionBean.getUsername());
	}
}
