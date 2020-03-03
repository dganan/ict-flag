package edu.uoc.ictflag.ela.bll;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;

import edu.uoc.ictflag.ela.dal.IAssignmentsReportRepository;
import edu.uoc.ictflag.ela.model.AssignmentsReportDataItem;

@Stateless
public class AssignmentsReportManager extends ReportManager<AssignmentsReportDataItem> implements IAssignmentsReportManager, Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Inject
	public AssignmentsReportManager(IAssignmentsReportRepository assignmentsReportRepository)
	{
		this.reportRepository = assignmentsReportRepository;
	}
}


