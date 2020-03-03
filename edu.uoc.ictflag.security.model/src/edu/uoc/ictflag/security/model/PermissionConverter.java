package edu.uoc.ictflag.security.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class PermissionConverter implements AttributeConverter<Permission, String>
{
	@Override
	public String convertToDatabaseColumn(Permission attribute)
	{
		return attribute.toString();
	}
	
	@Override
	public Permission convertToEntityAttribute(String dbData)
	{
		return Permission.fromString(dbData);
	}
}