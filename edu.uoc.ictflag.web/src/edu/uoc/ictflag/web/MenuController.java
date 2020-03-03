package edu.uoc.ictflag.web;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import edu.com.ictflag.security.bll.IAuthorizationManager;
import edu.uoc.ictflag.core.web.SessionBean;
import edu.uoc.ictflag.security.model.Page;
import edu.uoc.ictflag.security.web.LoginController;

@Named
@ViewScoped
public class MenuController implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private List<Page> pagesList;
	
	@Inject
	private IAuthorizationManager authorizationManager;
	
	@Inject
	private LoginController loginController;
	
	public List<Page> getUserMenuPages() throws Exception
	{
		if (!loginController.isUserLogged())
		{
			return null;
		}
		
		if (pagesList == null)
		{
			refreshList();
		}
		
		return pagesList;
	}
	
	private void refreshList() throws Exception
	{
		if (SessionBean.getUsername() != null)
		{
			pagesList = authorizationManager.getUserMenuPages(SessionBean.getUsername());
		}
	}
}
