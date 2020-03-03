package edu.uoc.ictflag.ela.web;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import edu.uoc.ictflag.core.log.ActivityLog;
import edu.uoc.ictflag.core.web.SessionBean;
import edu.uoc.ictflag.core.web.URLHelper;
import edu.uoc.ictflag.ela.bll.IToolManager;
import edu.uoc.ictflag.ela.model.Tool;

@Named
@ViewScoped
public class ToolsListController implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private List<Tool> toolsList;
	
	@Inject
	IToolManager toolManager;
	
	public ToolsListController() throws IOException
	{
	}
	
	@ActivityLog
	public List<Tool> getToolsList() throws Exception
	{
		if (toolsList == null)
		{
			refreshList();
		}
		
		return toolsList;
	}
	
	@ActivityLog
	public String deleteTool(long id) throws Exception
	{
		toolManager.deleteTool(SessionBean.getUsername(), id);
		
		refreshList();
		
		return ELAConstants.ToolsListPageUrl + URLHelper.facesRedirectTrue;
	}
	
	private void refreshList() throws Exception
	{
		toolsList = toolManager.getToolsList(SessionBean.getUsername());
	}
}
