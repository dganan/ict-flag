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
import edu.uoc.ictflag.institution.bll.ICourseGroupManager;
import edu.uoc.ictflag.institution.model.CourseGroup;

@Named
@ViewScoped
public class CourseGroupsListController implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private List<CourseGroup> courseGroupsList;
	
	@Inject
	ICourseGroupManager courseGroupManager;
	
	public CourseGroupsListController() throws IOException
	{
	}
	
	@ActivityLog
	public List<CourseGroup> getCourseGroupsList() throws Exception
	{
		if (courseGroupsList == null)
		{
			refreshList();
		}
		
		return courseGroupsList;
	}
	
	@ActivityLog
	public String deleteCourseGroup(long id) throws Exception
	{
		courseGroupManager.deleteCourseGroup(SessionBean.getUsername(), id);
		
		refreshList();
		
		return InstitutionConstants.CourseGroupsListPageUrl + URLHelper.facesRedirectTrue;
	}
	
	private void refreshList() throws Exception
	{
		courseGroupsList = courseGroupManager.getCourseGroupsList(SessionBean.getUsername());
	}
}
