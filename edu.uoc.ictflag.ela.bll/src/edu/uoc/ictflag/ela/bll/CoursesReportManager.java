package edu.uoc.ictflag.ela.bll;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;

import edu.uoc.ictflag.ela.dal.ICoursesReportRepository;
import edu.uoc.ictflag.ela.model.CoursesReportDataItem;

@Stateless
public class CoursesReportManager extends ReportManager<CoursesReportDataItem> implements ICoursesReportManager, Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Inject
	public CoursesReportManager(ICoursesReportRepository coursesReportRepository)
	{
		this.reportRepository = coursesReportRepository;
	}
}


