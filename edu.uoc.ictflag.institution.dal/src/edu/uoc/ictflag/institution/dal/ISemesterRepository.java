package edu.uoc.ictflag.institution.dal;

import java.util.Date;
import java.util.List;

import edu.uoc.ictflag.institution.model.Semester;

public interface ISemesterRepository
{
	List<Semester> getSemestersList(String username) throws Exception;
	
	Semester getSemester(String username, long id) throws Exception;
	
	void createOrUpdateSemester(Semester semester) throws Exception;
	
	void deleteSemester(long id) throws Exception;
	
	Semester getSemester(int academicYear, int number) throws Exception;
	
	Semester getSemesterByDate(Date date) throws Exception;
	
	Semester getSemesterByCode(String semesterCode) throws Exception;
	
	List<Semester> getSemesters(String username) throws Exception;
}
