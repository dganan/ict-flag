package edu.uoc.ictflag.ela.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import edu.uoc.ictflag.core.model.IIdentifiable;
import edu.uoc.ictflag.institution.model.Course;

@Entity
@Table(indexes = { @Index(name = "assignmment_course", columnList = "course_id", unique = false),
		@Index(name = "assignment_name", columnList = "name", unique = false) })

public class Assignment implements Serializable, IIdentifiable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	
	private String name;
	
	Course course;
	
	//@ManyToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH })
	@ManyToMany()
	@JoinTable(name = "assignment_exercises", joinColumns = @JoinColumn(name = "assignment_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "exercise_id", referencedColumnName = "id"))
	private Set<Exercise> exercises;
	
	public Long getId()
	{
		return id;
	}
	
	public void setId(Long id)
	{
		this.id = id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public Set<Exercise> getExercises()
	{
		return exercises;
	}
	
	public void setExercises(Set<Exercise> exercises)
	{
		this.exercises = exercises;
	}
	
	public Course getCourse()
	{
		return course;
	}
	
	public void setCourse(Course course)
	{
		this.course = course;
	}
	
	public Assignment()
	{
		exercises = new HashSet<Exercise>();
	}
	
	@Override
	public boolean equals(Object o)
	{
		return o instanceof Assignment && (((Assignment) o).getId() == this.getId() || ((Assignment) o).getId().equals(this.getId()));
	}
	
	@Override
	public int hashCode()
	{
		int hash = 17;
		
		if (id != null)
		{
			hash = hash * 31 + id.hashCode();
		}
		
		return hash;
	}
	
	@Override
	public String toString()
	{
		return name;
	}
}
