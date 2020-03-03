package edu.uoc.ictflag.institution.web;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;

import org.primefaces.model.LazyDataModel;

import edu.uoc.ictflag.core.IIdentifiableConverter;
import edu.uoc.ictflag.core.IMultipleSelectionListener;
import edu.uoc.ictflag.core.StringUtils;
import edu.uoc.ictflag.core.log.ActivityLog;
import edu.uoc.ictflag.core.log.LogHelper;
import edu.uoc.ictflag.core.web.NavigationHelper;
import edu.uoc.ictflag.core.web.SessionBean;
import edu.uoc.ictflag.core.web.URLHelper;
import edu.uoc.ictflag.core.web.ValidationHelper;
import edu.uoc.ictflag.institution.bll.ICourseGroupManager;
import edu.uoc.ictflag.institution.model.CourseGroup;
import edu.uoc.ictflag.institution.model.CourseGroupMember;
import edu.uoc.ictflag.security.model.User;

@Named
@ViewScoped
public class CourseGroupController implements Serializable, IMultipleSelectionListener
{
	private static final long serialVersionUID = 1L;
	
	@Valid
	private CourseGroup courseGroup;
	
	private List<User> instructors;
	private LazyUserDataModel availableMembers;
	private Set<User> selectedMembers;
	
	private IIdentifiableConverter<User> instructorConverter;
	//private IIdentifiableConverter<CourseGroupMember> courseGroupMemberConverter;
	
	@Inject
	ValidationHelper validationHelper;
	
	@Inject
	URLHelper urlHelper;
	
	@Inject
	ICourseGroupManager courseGroupManager;
	
	public CourseGroupController()
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
					courseGroup = new CourseGroup();
				}
				else
				{
					courseGroup = courseGroupManager.getCourseGroup(SessionBean.getUsername(), l);
				}
				
				instructors = courseGroupManager.getInstructors(SessionBean.getUsername());
				
				instructorConverter = new IIdentifiableConverter<User>(instructors);
				
				availableMembers = new LazyUserDataModel(courseGroupManager);
				selectedMembers = courseGroup.getMembers().stream().map(gm -> gm.getUser()).collect(Collectors.toSet());
				
				//courseGroupMemberConverter = new IIdentifiableConverter<CourseGroupMember>(availableMembers);
			}
		}
		catch (Exception e)
		{
			LogHelper.error(e);
			
			NavigationHelper.redirectTo404();
		}
	}
	
	public CourseGroup getCourseGroup()
	{
		return courseGroup;
	}
	
	public void setCourseGroup(CourseGroup courseGroup)
	{
		this.courseGroup = courseGroup;
	}
	
	public LazyDataModel<User> getAvailableMembers()
	{
		return availableMembers;
	}
	
	public Set<User> getSelectedMembers()
	{
		return selectedMembers;
	}
	
	public void setSelectedMembers(Set<User> selectedMembers)
	{
		this.selectedMembers = selectedMembers;
	}
	
	//	public IIdentifiableConverter<CourseGroupMember> getCourseGroupMemberConverter()
	//	{
	//		return courseGroupMemberConverter;
	//	}
	
	public List<User> getInstructors()
	{
		return instructors;
	}
	
	public IIdentifiableConverter<User> getInstructorConverter()
	{
		return instructorConverter;
	}
	
	public void selectItem(Long id)
	{
		Set<User> users = availableMembers.getDataSource().stream().filter(x -> x.getId().equals(id)).collect(Collectors.toSet());
		
		if (users != null && users.size() > 0)
		{
			selectedMembers.add((User) users.toArray()[0]);
		}
	}
	
	public void unselectItem(Long id)
	{
		selectedMembers = selectedMembers.stream().filter(x -> !x.getId().equals(id)).collect(Collectors.toSet());
	}
	
	public String itemToString(Object item)
	{
		User u = (User) item;
		
		if (u != null)
		{
			return u.getUsername() + " - " + u.getName();
		}
		
		return "";
	}
	
	@ActivityLog
	public String save() throws Exception
	{
		// Remove deselected elements
		Set<CourseGroupMember> members = courseGroup.getMembers().stream().filter(x->selectedMembers.contains(x.getUser())).collect(Collectors.toSet());

		// Add new elements
		for (User u : selectedMembers)
		{
			if (members.stream().noneMatch(x -> x.getUser().getId() == u.getId()))
			{
				members.add(new CourseGroupMember(u));
			}
		}
		
		courseGroup.setMembers(members);
		
		if (validationHelper.validate(this))
		{
			courseGroupManager.createOrUpdateCourseGroup(SessionBean.getUsername(), courseGroup);
			
			return InstitutionConstants.CourseGroupsListPageUrl + URLHelper.facesRedirectTrue;
		}
		
		return "invalid";
	}
}
