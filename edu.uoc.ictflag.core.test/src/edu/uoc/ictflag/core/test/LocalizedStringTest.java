package edu.uoc.ictflag.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.uoc.ictflag.core.localization.LocalizedString;

public class LocalizedStringTest
{
	@Test
	public void localizedStringTranslationTest()
	{
		LocalizedString ls = new LocalizedString();
		
		String caText = "Aixó es un text en Català";
		String esText = "Esto es un texto es Español";
		String enText = "This is a text in English";
		
		String encoded = "ca#Aixó es un text en Català;es#Esto es un texto es Español;en#This is a text in English";
		
		ls.setTranslation("ca", caText);
		ls.setTranslation("es", caText);
		ls.setTranslation("es", esText);
		ls.setTranslation("en", enText);
		
		String translation = ls.getTranslation("ca");
		
		assertEquals(caText, translation);
		
		translation = ls.getTranslation("es");
		
		assertEquals(esText, translation);
		
		translation = ls.getTranslation("en");
		
		assertEquals(enText, translation);
		
		translation = ls.getTranslation("ru");
		
		assertNull(translation);
		
		translation = ls.getTranslation(null);
		
		assertNull(translation);
		
		translation = ls.getTranslation();
		
		assertEquals(caText, translation);
		
		boolean b = ls.hasTranslation("en");
		
		assertTrue(null, b);
		
		ls.removeTranslation("en");
		
		b = ls.hasTranslation("en");
		
		assertFalse(null, b);
		
		ls.addTranslation("en");
		
		b = ls.hasTranslation("en");

		assertTrue(null, b);
		
		translation = ls.getTranslation("en");
		
		assertEquals("", translation);
		
		ls.setTranslation("en", enText);
		
		translation = ls.toString();
		
		assertEquals(encoded, translation);
		
		LocalizedString ls2 = LocalizedString.fromStringFormat(encoded);
		
		assertEquals(ls.getTranslation("ca"), ls2.getTranslation("ca"));
		assertEquals(ls.getTranslation("en"), ls2.getTranslation("en"));
		assertEquals(ls.getTranslation("es"), ls2.getTranslation("es"));
	}
}
