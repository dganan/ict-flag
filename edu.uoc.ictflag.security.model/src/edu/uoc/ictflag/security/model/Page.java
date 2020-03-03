package edu.uoc.ictflag.security.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import edu.uoc.ictflag.core.localization.LocalizedString;
import edu.uoc.ictflag.core.model.ColumnTypes;
import edu.uoc.ictflag.core.model.IIdentifiable;

@Entity
@Table(name = "Pages")
public class Page implements Serializable, IIdentifiable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true)
	private String uniqueName;
	
	private String section;
	
	private String module;
	
	@Valid
	@NotNull
	@Transient
	private LocalizedString displayName;

	@Column(columnDefinition = ColumnTypes.TEXT_COLUMN, name = "displayName")
	private String displayNameStr;
	
	private String relativeUrl;
	
	private String iconClass;

	private int orderInMenu;
	
	private boolean inMenu;
	
	public Long getId()
	{
		return this.id;
	}
	
	public void setId(Long id)
	{
		this.id = id;
	}
	
	public String getUniqueName()
	{
		return uniqueName;
	}
	
	public void setUniqueName(String uniqueName)
	{
		this.uniqueName = uniqueName;
	}
	
	public String getSection()
	{
		return section;
	}
	
	public void setSection(String section)
	{
		this.section = section;
	}
	
	public String getModule()
	{
		return module;
	}
	
	public void setModule(String module)
	{
		this.module = module;
	}
	
	public LocalizedString getDisplayName()
	{
		return displayName;
	}
	
	public void setDisplayName(LocalizedString displayName)
	{
		this.displayName = displayName;
	}
	
	public String getRelativeUrl()
	{
		return relativeUrl;
	}
	
	public void setRelativeUrl(String relativeUrl)
	{
		this.relativeUrl = relativeUrl;
	}
	
	public String getIconClass()
	{
		return iconClass;
	}
	
	public void setIconClass(String iconClass)
	{
		this.iconClass = iconClass;
	}
	
	public int getOrderInMenu()
	{
		return orderInMenu;
	}
	
	public void setOrderInMenu(int order)
	{
		this.orderInMenu = order;
	}
	
	public boolean isInMenu()
	{
		return inMenu;
	}
	
	public void setInMenu(boolean inMenu)
	{
		this.inMenu = inMenu;
	}
	
	@PrePersist
	@PreUpdate
	public void preUpdate()
	{
		this.displayNameStr = displayName.toStringFormat();
	}
	
	@PostLoad
	public void postLoad()
	{
		this.displayName = LocalizedString.fromStringFormat(displayNameStr);
	}
}
