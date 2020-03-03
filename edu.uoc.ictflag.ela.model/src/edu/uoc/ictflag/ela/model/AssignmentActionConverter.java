package edu.uoc.ictflag.ela.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class AssignmentActionConverter implements AttributeConverter<AssignmentAction, String>
{
	@Override
	public String convertToDatabaseColumn(AssignmentAction attribute)
	{
		if (attribute != null)
		{
			return attribute.toString();
		}
		
		return null;
	}
	
	@Override
	public AssignmentAction convertToEntityAttribute(String dbData)
	{
		if (dbData != null)
		{
			return AssignmentAction.fromString(dbData);
		}
		
		return null;
	}
}