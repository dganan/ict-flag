package edu.uoc.ictflag.ela.web;

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
import edu.uoc.ictflag.ela.bll.IToolManager;
import edu.uoc.ictflag.ela.model.Tool;

@Named
@ViewScoped
public class ToolController implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Valid
	private Tool tool;
	
	@Inject
	ValidationHelper validationHelper;
	
	@Inject
	IToolManager toolManager;
	
	@Inject
	URLHelper urlHelper;
	
	public ToolController()
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
					tool = new Tool();
				}
				else
				{
					tool = toolManager.getTool(SessionBean.getUsername(), l);
				}
			}
		}
		catch (Exception e)
		{
			LogHelper.error(e);
			
			NavigationHelper.redirectTo404();
		}
	}
	
	public Tool getTool()
	{
		return tool;
	}
	
	public void setTool(Tool tool)
	{
		this.tool = tool;
	}
	
	@ActivityLog
	public String save() throws Exception
	{
		if (validationHelper.validate(this))
		{
			toolManager.createOrUpdateTool(SessionBean.getUsername(), tool);
			
			return ELAConstants.ToolsListPageUrl + URLHelper.facesRedirectTrue;
		}
		
		return "invalid";
	}
}
