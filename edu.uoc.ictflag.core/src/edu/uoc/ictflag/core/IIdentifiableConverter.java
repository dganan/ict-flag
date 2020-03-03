package edu.uoc.ictflag.core;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import edu.uoc.ictflag.core.model.IIdentifiable;

public class IIdentifiableConverter<T extends IIdentifiable> implements Converter
{
	List<T> availableValues;
	
	public IIdentifiableConverter()
	{
	}
	
	public IIdentifiableConverter(List<T> list)
	{
		availableValues = list;
	}
	
	@Override
	public Object getAsObject(FacesContext ctx, UIComponent component, String value)
	{
		if (value == null || value.trim().length() == 0)
		{
			return null;
		}
		
		Long l = LongUtils.parseLong(value);
		
		if (l == null) return null;
		
		return ListUtils.findById(availableValues, Long.parseLong(value));
	}
	
	@Override
	public String getAsString(FacesContext fc, UIComponent uic, Object o)
	{
		if (o == null || o.toString().trim().length() == 0)
		{
			return null;
		}
		
		return ((IIdentifiable) o).getId().toString();
	}
}
