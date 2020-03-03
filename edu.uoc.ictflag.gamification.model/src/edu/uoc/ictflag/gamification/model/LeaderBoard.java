package edu.uoc.ictflag.gamification.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

import edu.uoc.ictflag.institution.model.CourseGroup;
import edu.uoc.ictflag.security.model.User;

@Entity
public class LeaderBoard implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	
	CourseGroup coursegroup;
	
	@OneToMany
	@OrderColumn(name="position")
	List<User> leaders;
	
	public Long getId()
	{
		return id;
	}
	
	public void setId(Long id)
	{
		this.id = id;
	}

	public CourseGroup getCourseGroup()
	{
		return coursegroup;
	}

	public void setCourseGroup(CourseGroup coursegroup)
	{
		this.coursegroup = coursegroup;
	}

	public List<User> getLeaders()
	{
		return leaders;
	}
	
	public void setLeaders(List<User> leaders)
	{
		this.leaders = leaders;
	}
}