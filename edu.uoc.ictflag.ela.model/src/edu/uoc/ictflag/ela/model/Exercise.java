package edu.uoc.ictflag.ela.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import edu.uoc.ictflag.core.model.IIdentifiable;

@Entity
@Table(indexes = { @Index(name = "exercise_tool", columnList = "tool_id", unique = false),
		@Index(name = "exercise_name", columnList = "name", unique = false) })

public class Exercise implements Serializable, IIdentifiable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	
	private String name;
	
	private Tool tool;
	
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
	
	public Tool getTool()
	{
		return tool;
	}
	
	public void setTool(Tool tool)
	{
		this.tool = tool;
	}
	
	@Override
	public boolean equals(Object o)
	{
		return o instanceof Exercise && (((Exercise) o).getId() == this.getId() || ((Exercise) o).getId().equals(this.getId()));
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
