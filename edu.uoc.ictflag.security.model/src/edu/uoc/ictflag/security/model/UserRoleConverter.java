package edu.uoc.ictflag.security.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class UserRoleConverter implements AttributeConverter<UserRole, String>
{
	@Override
	public String convertToDatabaseColumn(UserRole attribute)
	{
		if (attribute != null)
		{
			return attribute.toString();
		}
		
		return null;
	}
	
	@Override
	public UserRole convertToEntityAttribute(String dbData)
	{
		if (dbData != null)
		{
			return UserRole.fromString(dbData);
		}
		
		return null;
	}
}