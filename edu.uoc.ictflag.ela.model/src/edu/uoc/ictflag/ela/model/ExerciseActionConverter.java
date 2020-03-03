package edu.uoc.ictflag.ela.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ExerciseActionConverter implements AttributeConverter<ExerciseAction, String>
{
	@Override
	public String convertToDatabaseColumn(ExerciseAction attribute)
	{
		if (attribute != null)
		{
			return attribute.toString();
		}
		
		return null;
	}
	
	@Override
	public ExerciseAction convertToEntityAttribute(String dbData)
	{
		if (dbData != null)
		{
			return ExerciseAction.fromString(dbData);
		}
		
		return null;
	}
}