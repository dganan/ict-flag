package edu.uoc.ictflag.institution.bll;

import java.util.List;

import edu.uoc.ictflag.institution.model.VLE;

public interface IVLEManager
{
	List<VLE> getVLEsList(String username) throws Exception;
	
	void deleteVLE(String username, long id) throws Exception;
	
	VLE getVLE(String username, long id) throws Exception;
	
	void createOrUpdateVLE(String username, VLE vle) throws Exception;
}
