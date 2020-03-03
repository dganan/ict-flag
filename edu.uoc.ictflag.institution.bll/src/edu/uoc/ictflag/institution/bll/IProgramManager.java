package edu.uoc.ictflag.institution.bll;

import java.util.List;

import edu.uoc.ictflag.institution.model.Program;
import edu.uoc.ictflag.security.model.User;

public interface IProgramManager
{
	List<Program> getProgramsList(String username) throws Exception;
	
	void deleteProgram(String username, long id) throws Exception;
	
	Program getProgram(String username, long id) throws Exception;
	
	void createOrUpdateProgram(String username, Program program) throws Exception;
	
	List<User> getProgramManagers(String username) throws Exception;
}
