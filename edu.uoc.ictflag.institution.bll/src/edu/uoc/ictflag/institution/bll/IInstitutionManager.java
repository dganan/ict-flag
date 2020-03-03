package edu.uoc.ictflag.institution.bll;

import edu.uoc.ictflag.institution.model.Institution;

public interface IInstitutionManager
{
	Institution getInstitution(String username) throws Exception;
	
	void createOrUpdateInstitution(String username, Institution institution) throws Exception;
}
