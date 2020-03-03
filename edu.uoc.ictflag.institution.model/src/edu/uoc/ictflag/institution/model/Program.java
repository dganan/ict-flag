package edu.uoc.ictflag.institution.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import edu.uoc.ictflag.core.localization.LocalizedString;
import edu.uoc.ictflag.core.model.ColumnTypes;
import edu.uoc.ictflag.core.model.INameable;
import edu.uoc.ictflag.security.model.User;

@Entity
public class Program implements Serializable, INameable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	
	@NotNull
	@Column(columnDefinition = ColumnTypes.TEXT_COLUMN)
	private String code;
	
	@Valid
	@NotNull
	@Transient
	private LocalizedString name;
	
	@Column(columnDefinition = ColumnTypes.TEXT_COLUMN, name = "name")
	private String nameStr;
	
	@ManyToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinTable(name = "program_subject", joinColumns = @JoinColumn(name = "program_id", referencedColumnName = "id"), 
					inverseJoinColumns = @JoinColumn(name = "subject_id", referencedColumnName = "id"))
	private Set<Subject> subjects;
	
	//@NotNull
	//@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH })
	private User programManager;
	
	public Long getId()
	{
		return id;
	}
	
	public void setId(Long id)
	{
		this.id = id;
	}
	
	public String getCode()
	{
		return code;
	}
	
	public void setCode(String code)
	{
		this.code = code;
	}
	
	public LocalizedString getName()
	{
		return name;
	}
	
	public void setName(LocalizedString name)
	{
		this.name = name;
	}
	
	public Set<Subject> getSubjects()
	{
		return subjects;
	}
	
	public void setSubjects(Set<Subject> subjects)
	{
		this.subjects = subjects;
	}
	
	public Program()
	{
		name = new LocalizedString();
		subjects = new HashSet<Subject>();
	}
	
	public User getProgramManager()
	{
		return programManager;
	}
	
	public void setProgramManager(User programManager)
	{
		this.programManager = programManager;
	}
	
	@Override
	public boolean equals(Object o)
	{
		return o instanceof Program && (((Program) o).getId() == this.getId() || ((Program) o).getId().equals(this.getId()));
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
	
	@PrePersist
	@PreUpdate
	public void preUpdate()
	{
		this.nameStr = name.toStringFormat();
	}
	
	@PostLoad
	public void postLoad()
	{
		this.name = LocalizedString.fromStringFormat(nameStr);
	}
}
