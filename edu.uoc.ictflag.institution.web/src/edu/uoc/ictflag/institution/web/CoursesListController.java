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
import edu.uoc.ictflag.institution.bll.ICourseManager;
import edu.uoc.ictflag.institution.model.Course;

@Named
@ViewScoped
public class CoursesListController implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private List<Course> coursesList;
	
	@Inject
	ICourseManager courseManager;
	
	public CoursesListController() throws IOException
	{
	}
	
	@ActivityLog
	public List<Course> getCoursesList() throws Exception
	{
		if (coursesList == null)
		{
			refreshList();
		}
		
		return coursesList;
	}
	
	@ActivityLog
	public String deleteCourse(long id) throws Exception
	{
		courseManager.deleteCourse(SessionBean.getUsername(), id);
		
		refreshList();
		
		return InstitutionConstants.CoursesListPageUrl + URLHelper.facesRedirectTrue;
	}
	
	private void refreshList() throws Exception
	{
		coursesList = courseManager.getCoursesList(SessionBean.getUsername());
	}
}
