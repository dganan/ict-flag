package edu.uoc.ictflag.institution.dal;

import java.util.List;

import edu.uoc.ictflag.institution.model.VLE;

public interface IVLERepository
{
	List<VLE> getVLEsList() throws Exception;
	
	VLE getVLE(long id) throws Exception;
	
	void createOrUpdateVLE(VLE VLE) throws Exception;
	
	void deleteVLE(long id) throws Exception;
}
