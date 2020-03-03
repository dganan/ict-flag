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
import edu.uoc.ictflag.core.model.IPreUpdatable;

@Entity
public class Institution implements Serializable, IIdentifiable, IPreUpdatable
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
	@Column(columnDefinition = ColumnTypes.TEXT_COLUMN)
	private String mainSiteUrl;
	
	//	@OneToMany()
	//	Set<VLE> vles;
	
	public Institution()
	{
		//		vles = new HashSet<VLE>();
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
	
	public String getMainSiteUrl()
	{
		return mainSiteUrl;
	}

	public void setMainSiteUrl(String mainSiteUrl)
	{
		this.mainSiteUrl = mainSiteUrl;
	}
	
	//	public Set<VLE> getVLEs()
	//	{
	//		return vles;
	//	}
	//	
	//	public void setVLEs(Set<VLE> vles)
	//	{
	//		this.vles = vles;
	//	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o instanceof Institution)
		{
			Institution i = (Institution) o;
			
			if (i.getId() == this.getId() || i.getId().equals(this.getId()))
			{
				if (i.mainSiteUrl == this.mainSiteUrl || i.mainSiteUrl.equals(this.mainSiteUrl))
				{
					if (i.name == this.name || i.name.equals(this.name))
					{
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	@Override
	public int hashCode()
	{
		int hash = 17;
		
		if (id != null)
		{
			hash = hash * 31 + (id + mainSiteUrl + name).hashCode();
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
