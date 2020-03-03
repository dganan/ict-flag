package edu.uoc.ictflag.institution.web;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import edu.uoc.ictflag.core.log.ActivityLog;
import edu.uoc.ictflag.core.web.SessionBean;
import edu.uoc.ictflag.core.web.URLHelper;
import edu.uoc.ictflag.institution.bll.IVLEManager;
import edu.uoc.ictflag.institution.model.VLE;

@Named(value = "vlesListController")
@ViewScoped
public class VLEsListController implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private List<VLE> vlesList;
	
	@Inject
	IVLEManager vleManager;
	
	public VLEsListController()
	{
	}
	
	@ActivityLog
	@Named(value = "VLEsList")
	public List<VLE> getVLEsList() throws Exception
	{
		if (vlesList == null)
		{
			refreshList();
		}
		
		return vlesList;
	}
	
	@ActivityLog
	@Named(value = "deleteVLE")
	public String deleteVLE(long id) throws Exception
	{
		vleManager.deleteVLE(SessionBean.getUsername(), id);
		
		refreshList();
		
		return InstitutionConstants.VLEsListPageUrl + URLHelper.facesRedirectTrue;
	}
	
	private void refreshList() throws Exception
	{
		vlesList = vleManager.getVLEsList(SessionBean.getUsername());
	}
}
