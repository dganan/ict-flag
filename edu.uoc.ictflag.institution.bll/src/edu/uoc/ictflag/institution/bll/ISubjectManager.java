package edu.uoc.ictflag.institution.bll;

import java.util.List;

import edu.uoc.ictflag.institution.model.Subject;

public interface ISubjectManager
{
	List<Subject> getSubjectsList(String username) throws Exception;
	
	void deleteSubject(String username, long id) throws Exception;
	
	Subject getSubject(String username, long id) throws Exception;
	
	void createOrUpdateSubject(String username, Subject subject) throws Exception;
}
