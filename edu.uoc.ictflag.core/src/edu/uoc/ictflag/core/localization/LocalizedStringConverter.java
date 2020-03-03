package edu.uoc.ictflag.core.localization;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = false)
public class LocalizedStringConverter implements AttributeConverter<LocalizedString, String>
{
	@Override
	public String convertToDatabaseColumn(LocalizedString attribute)
	{
		return attribute.toStringFormat();
	}
	
	@Override
	public LocalizedString convertToEntityAttribute(String dbData)
	{
		return LocalizedString.fromStringFormat(dbData);
	}
}