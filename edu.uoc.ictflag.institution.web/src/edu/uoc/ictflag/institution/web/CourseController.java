package edu.uoc.ictflag.institution.web;

import java.io.Serializable;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;

import edu.uoc.ictflag.core.IIdentifiableConverter;
import edu.uoc.ictflag.core.StringUtils;
import edu.uoc.ictflag.core.localization.LocalizationController;
import edu.uoc.ictflag.core.log.ActivityLog;
import edu.uoc.ictflag.core.log.LogHelper;
import edu.uoc.ictflag.core.web.NavigationHelper;
import edu.uoc.ictflag.core.web.SessionBean;
import edu.uoc.ictflag.core.web.URLHelper;
import edu.uoc.ictflag.core.web.ValidationHelper;
import edu.uoc.ictflag.institution.bll.ICourseManager;
import edu.uoc.ictflag.institution.bll.ISubjectManager;
import edu.uoc.ictflag.institution.model.Course;
import edu.uoc.ictflag.institution.model.Semester;
import edu.uoc.ictflag.institution.model.Subject;
import edu.uoc.ictflag.security.model.User;

@Named
@ViewScoped
public class CourseController implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Valid
	private Course course;
	
	private List<Semester> semesters;
	private List<User> lecturers;
	private List<Subject> subjects;
	
	private IIdentifiableConverter<Semester> semesterConverter;
	private IIdentifiableConverter<User> lecturerConverter;
	private IIdentifiableConverter<Subject> subjectConverter;
	
	@Inject
	LocalizationController localizationController;
	
	@Inject
	ValidationHelper validationHelper;
	
	@Inject
	URLHelper urlHelper;
	
	@Inject
	ICourseManager courseManager;
	
	@Inject
	ISubjectManager subjectManager;
	
	public CourseController()
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
					course = new Course();
				}
				else
				{
					course = courseManager.getCourse(SessionBean.getUsername(), l);
				}
				
				semesters = courseManager.getSemesters(SessionBean.getUsername());
				
				lecturers = courseManager.getLecturers(SessionBean.getUsername());
				
				subjects = subjectManager.getSubjectsList(SessionBean.getUsername());
				
				semesterConverter = new IIdentifiableConverter<Semester>(semesters);
				lecturerConverter = new IIdentifiableConverter<User>(lecturers);
				subjectConverter = new IIdentifiableConverter<Subject>(subjects);
			}
		}
		catch (Exception e)
		{
			LogHelper.error(e);
			
			NavigationHelper.redirectTo404();
		}
	}
	
	public Course getCourse()
	{
		return course;
	}
	
	public void setCourse(Course course)
	{
		this.course = course;
	}
	
	public List<Semester> getSemesters()
	{
		return semesters;
	}
	
	public List<User> getLecturers()
	{
		return lecturers;
	}
	
	public List<Subject> getSubjects()
	{
		return subjects;
	}
	
	public IIdentifiableConverter<Semester> getSemesterConverter()
	{
		return semesterConverter;
	}
	
	public IIdentifiableConverter<User> getLecturerConverter()
	{
		return lecturerConverter;
	}
	
	public IIdentifiableConverter<Subject> getSubjectConverter()
	{
		return subjectConverter;
	}
	
	public List<String> getLanguages()
	{
		return localizationController.getSupportedLanguages();
	}
	
	@ActivityLog
	public String save() throws Exception
	{
		if (validationHelper.validate(this))
		{
			courseManager.createOrUpdateCourse(SessionBean.getUsername(), course);
			
			return InstitutionConstants.CoursesListPageUrl + URLHelper.facesRedirectTrue;
		}
		
		return "invalid";
	}
}
