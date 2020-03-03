package edu.uoc.ictflag.ela.bll;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;

import edu.uoc.ictflag.ela.dal.IStudentReportRepository;
import edu.uoc.ictflag.ela.model.StudentReportDataItem;
import edu.uoc.ictflag.ela.model.StudentReportGlobalProgressItem;

@Stateless
public class StudentReportManager implements IStudentReportManager, Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Inject
	IStudentReportRepository studentReportRepository;
	
	@Override
	public StudentReportDataItem getReportData(String username) throws Exception
	{
		return studentReportRepository.getReportData(username);
	}
	
	@Override
	public StudentReportGlobalProgressItem getGlobalProgressData() throws Exception
	{
		return studentReportRepository.getGlobalProgressData();
	}
}


