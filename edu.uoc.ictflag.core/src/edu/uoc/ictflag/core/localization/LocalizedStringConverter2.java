package edu.uoc.ictflag.core.localization;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = false)
public class LocalizedStringConverter2 implements AttributeConverter<List<Translation>, String>
{
	@Override
	public String convertToDatabaseColumn(List<Translation> attribute)
	{
		StringBuilder sb = new StringBuilder();
		
		boolean firstTime = true;
		
		for (Translation t : attribute)
		{
			if (!firstTime)
			{
				sb.append(";");
			}
			
			sb.append(t.getCode());
			sb.append("#");
			sb.append(t.getTranslation());
			
			firstTime = false;
		}
		
		return sb.toString();
	}
	
	@Override
	public List<Translation> convertToEntityAttribute(String dbData)
	{
		List<Translation> lt = new ArrayList<Translation>();
		
		String[] parts = dbData.split(";");
		
		for (String s : parts)
		{
			String[] tparts = s.split("#");
			
			Translation t = new Translation();
			
			t.setCode(tparts[0]);
			t.setTranslation(tparts[1]);
			
			lt.add(t);
		}
		
		return lt;
	}
}