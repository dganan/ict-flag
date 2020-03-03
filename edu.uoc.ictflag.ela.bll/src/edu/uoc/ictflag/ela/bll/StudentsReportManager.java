package edu.uoc.ictflag.ela.bll;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import edu.uoc.ictflag.ela.dal.IStudentsReportRepository;
import edu.uoc.ictflag.ela.model.StudentsReportDataItem;

@Stateless
public class StudentsReportManager extends ReportManager<StudentsReportDataItem> implements IStudentsReportManager, Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Inject
	public StudentsReportManager(IStudentsReportRepository studentsReportRepository)
	{
		this.reportRepository = studentsReportRepository;
	}
	
	@Override
	public List<String> getStudentsData() throws Exception
	{
		return ((IStudentsReportRepository) reportRepository).getStudentsData();
	}
}


