package edu.uoc.ictflag.core.localization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@SessionScoped
public class LocalizationController implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public List<String> getSupportedLanguages()
	{
		ArrayList<String> sl = new ArrayList<String>();
		
		Iterator<Locale> ls = FacesContext.getCurrentInstance().getApplication().getSupportedLocales();
		
		while (ls.hasNext())
		{
			sl.add(ls.next().getLanguage());
		}
		
		return sl;
	}
	
	public static String getDefaultLanguageStatic()
	{
		return FacesContext.getCurrentInstance().getApplication().getDefaultLocale().getLanguage();
	}
	
	public static String getCurrentLanguageStatic()
	{
		return FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage();
	}
	
	public String getDefaultLanguage()
	{
		return FacesContext.getCurrentInstance().getApplication().getDefaultLocale().getLanguage();
	}
	
	public String getCurrentLanguage()
	{
		return FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage();
	}
	
	public static String getLocalizedString(String resourceBundleKey) throws MissingResourceException
	{
		String s = resourceBundleKey;
		
		try
		{
			return getLocalizedString("msg", resourceBundleKey);
		}
		catch (Exception e)
		{
		}
		
		return s;
	}
	
	public static String getLocalizedString(String resourceBundleName, String resourceBundleKey) throws MissingResourceException
	{
		FacesContext facesContext = FacesContext.getCurrentInstance();
		
		ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, resourceBundleName);
		
		return bundle.getString(resourceBundleKey);
	}
	
	public String translate(Object o)
	{
		if (o instanceof Integer)
		{
			return ((Integer) o).toString();
		}
		else if (o instanceof LocalizedString)
		{
			LocalizedString ls = (LocalizedString) o;
			
			String l = getCurrentLanguage();
			
			String t = ls.getTranslation(l);
			
			if (t == null)
			{
				l = getDefaultLanguage();
				
				t = ls.getTranslation(l);
				
				if (t == null)
				{
					t = ls.getTranslation();
				}
			}
			
			return t;
		}
		
		return getLocalizedString(o.toString());
	}
	
	public static String getLocalizedErrorString(String errorkey)
	{
		FacesContext facesContext = FacesContext.getCurrentInstance();
		
		ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "localization.localization");
		
		return bundle.getString(errorkey);
	}
	
	public List<String> availableLanguages(LocalizedString ls)
	{
		List<String> langs = new ArrayList<String>();
		
		List<String> supported = getSupportedLanguages();
		
		for (String l : supported)
		{
			if (!ls.hasTranslation(l))
			{
				langs.add(l);
			}
		}
		
		Collections.sort(langs);
		
		return langs;
	}
}
