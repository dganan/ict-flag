package edu.uoc.ictflag.ela.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class OutcomeConverter implements AttributeConverter<Outcome, String>
{
	@Override
	public String convertToDatabaseColumn(Outcome attribute)
	{
		if (attribute != null)
		{
			return attribute.toString();
		}
		
		return null;
	}
	
	@Override
	public Outcome convertToEntityAttribute(String dbData)
	{
		if (dbData != null)
		{
			return Outcome.fromString(dbData);
		}
		
		return null;
	}
}