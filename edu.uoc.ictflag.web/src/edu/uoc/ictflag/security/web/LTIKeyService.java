package edu.uoc.ictflag.security.web;

import javax.ejb.Startup;

import org.imsglobal.aspect.LtiKeySecretService;

@Startup
public class LTIKeyService implements LtiKeySecretService
{
	@Override
	public String getSecretForKey(String s)
	{
		return "5N123FDchTmTYr6n";
	}
}
