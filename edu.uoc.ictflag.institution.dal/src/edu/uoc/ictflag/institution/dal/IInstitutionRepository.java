package edu.uoc.ictflag.institution.dal;

import edu.uoc.ictflag.institution.model.Institution;

public interface IInstitutionRepository
{
	Institution getInstitution() throws Exception;
	
	// Institution getInstitution(long id) throws Exception;
	
	void createOrUpdateInstitution(Institution institution) throws Exception;
}
