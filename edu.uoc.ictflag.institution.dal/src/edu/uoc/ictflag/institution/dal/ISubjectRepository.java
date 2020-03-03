package edu.uoc.ictflag.institution.dal;

import java.util.List;

import edu.uoc.ictflag.institution.model.Subject;

public interface ISubjectRepository
{
	List<Subject> getSubjectsList(String username) throws Exception;
	
	Subject getSubject(String username, long id) throws Exception;
	
	void createOrUpdateSubject(Subject Subject) throws Exception;
	
	void deleteSubject(long id) throws Exception;
	
	Subject getSubjectByCode(String subject) throws Exception;
}
