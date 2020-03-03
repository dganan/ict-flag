package edu.uoc.ictflag.institution.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import edu.uoc.ictflag.core.localization.LocalizedString;
import edu.uoc.ictflag.core.model.ColumnTypes;
import edu.uoc.ictflag.core.model.IIdentifiable;

@Entity
public class VLE implements Serializable, IIdentifiable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	
	@Valid
	@NotNull
	@Transient
	LocalizedString name;
	
	@Column(columnDefinition = ColumnTypes.TEXT_COLUMN, name = "name")
	private String nameStr;
	
	@NotNull
	@Column(columnDefinition = ColumnTypes.TEXT_COLUMN)
	String url;
	
	public Long getId()
	{
		return id;
	}
	
	public void setId(Long id)
	{
		this.id = id;
	}
	
	public LocalizedString getName()
	{
		return name;
	}

	public void setName(LocalizedString name)
	{
		this.name = name;
	}
	
	public String getUrl()
	{
		return url;
	}
	
	public void setUrl(String url)
	{
		this.url = url;
	}
	
	public VLE()
	{
		name = new LocalizedString();
	}
	
	@Override
	public boolean equals(Object o)
	{
		return o instanceof VLE && (((VLE) o).getId() == this.getId() || ((VLE) o).getId().equals(this.getId()));
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
