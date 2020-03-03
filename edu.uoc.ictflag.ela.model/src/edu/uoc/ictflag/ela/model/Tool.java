package edu.uoc.ictflag.ela.model;

import java.io.Serializable;
import java.util.UUID;

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
import edu.uoc.ictflag.core.model.INameable;
import edu.uoc.ictflag.core.model.IPreUpdatable;

@Entity
public class Tool implements Serializable, INameable, IPreUpdatable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	
	@Valid
	@NotNull
	@Transient
	private LocalizedString name;
	
	@Column(columnDefinition = ColumnTypes.TEXT_COLUMN, name = "name")
	private String nameStr;
	
	@NotNull
	private String code;
	
	@Column(columnDefinition = ColumnTypes.TEXT_COLUMN, name = "uuid")
	private String uuid;
	
	public Tool()
	{
		name = new LocalizedString();
	}
	
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
	
	public String getCode()
	{
		return code;
	}
	
	public void setCode(String code)
	{
		this.code = code;
	}
	
	@Override
	public String toString()
	{
		return name.getTranslation();
	}
	
	@Override
	public boolean equals(Object o)
	{
		return o instanceof Tool && (((Tool) o).getId() == this.getId() || ((Tool) o).getId().equals(this.getId()));
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
		
		if (this.uuid == null) uuid = UUID.randomUUID().toString();
	}
	
	@PostLoad
	public void postLoad()
	{
		this.name = LocalizedString.fromStringFormat(nameStr);
	}

	public String getUuid()
	{
		return uuid;
	}
	
	public void setUuid(String uuid)
	{
		this.uuid = uuid;
	}
}
