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
import edu.uoc.ictflag.institution.bll.IVLEManager;
import edu.uoc.ictflag.institution.model.VLE;

@Named(value = "vleController")
@ViewScoped
public class VLEController implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Valid
	private VLE vle;
	
	@Inject
	ValidationHelper validationHelper;
	
	@Inject
	URLHelper urlHelper;
	
	@Inject
	IVLEManager vleManager;
	
	public VLEController()
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
					vle = new VLE();
				}
				else
				{
					vle = vleManager.getVLE(SessionBean.getUsername(), l);
				}
			}
		}
		catch (Exception e)
		{
			LogHelper.error(e);
			
			NavigationHelper.redirectTo404();
		}
	}
	
	public VLE getVle()
	{
		return vle;
	}
	
	public void setVle(VLE vle)
	{
		this.vle = vle;
	}
	
	@ActivityLog
	public String save() throws Exception
	{
		if (validationHelper.validate(this))
		{
			vleManager.createOrUpdateVLE(SessionBean.getUsername(), vle);
			
			return InstitutionConstants.VLEsListPageUrl + URLHelper.facesRedirectTrue;
		}
		
		return "invalid";
	}
}
