package edu.uoc.ictflag.core.localization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Size;

//@Embeddable
public class LocalizedString implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Valid
	@Size(min = 1, message = "{At_least_one_translation_is_required}")
	private List<Translation> translations;
	
	public LocalizedString()
	{
		translations = new ArrayList<Translation>();
	}
	
	public void setTranslation(String code, String translation)
	{
		int i = indexOfTranslation(code);
		
		if (i >= 0)
		{
			translations.get(i).setTranslation(translation);
		}
		else
		{
			translations.add(new Translation(code, translation));
		}
	}
	
	private int indexOfTranslation(String code)
	{
		for (int i = 0; i < translations.size(); i++)
		{
			if (translations.get(i).getCode().equals(code))
			{
				return i;
			}
		}
		
		return -1;
	}
	
	public String getTranslation(String code)
	{
		int i = indexOfTranslation(code);
		
		if (i >= 0)
		{
			return translations.get(i).getTranslation();
		}
		
		return null;
	}
	
	public String getTranslation()
	{
		String t = null;
		
		if (translations.size() > 0)
		{
			String l = LocalizationController.getCurrentLanguageStatic();
			
			t = this.getTranslation(l);
			
			if (t == null)
			{
				l = LocalizationController.getDefaultLanguageStatic();
				
				t = this.getTranslation(l);
				
				if (t == null)
				{
					t = this.translations.get(0).getTranslation();
				}
			}
		}
		
		return t;
	}
	
	public List<Translation> getTranslations()
	{
		Collections.sort(translations);
		
		return translations;
	}
	
	public void setTranslations(List<Translation> translations)
	{
		this.translations = translations;
	}
	
	public String toStringFormat()
	{
		StringBuilder sb = new StringBuilder();
		
		boolean firstTime = true;
		
		for (Translation t : translations)
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
	
	public static LocalizedString fromStringFormat(String str)
	{
		LocalizedString ls = new LocalizedString();
		
		String[] parts = str.split(";");
		
		if (parts.length > 0)
		{
			for (String s : parts)
			{
				String[] tparts = s.split("#");
				
				ls.setTranslation(tparts[0], tparts[1]);
			}
		}
		
		return ls;
	}
	
	public void removeTranslation(String code)
	{
		int i = indexOfTranslation(code);
		
		if (i >= 0)
		{
			translations.remove(i);
		}
	}
	
	public void addTranslation(String code)
	{
		int i = indexOfTranslation(code);
		
		if (i < 0)
		{
			translations.add(new Translation(code, ""));
		}
	}
	
	public boolean hasTranslation(String code)
	{
		return indexOfTranslation(code) >= 0;
	}
	
	@Override
	public String toString()
	{
		return toStringFormat();
	}
}
