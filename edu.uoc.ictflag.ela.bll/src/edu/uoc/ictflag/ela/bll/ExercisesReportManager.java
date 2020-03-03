package edu.uoc.ictflag.ela.bll;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;

import edu.uoc.ictflag.ela.dal.IExercisesReportRepository;
import edu.uoc.ictflag.ela.model.ExercisesReportDataItem;

@Stateless
public class ExercisesReportManager extends ReportManager<ExercisesReportDataItem> implements IExercisesReportManager, Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Inject
	public ExercisesReportManager(IExercisesReportRepository exercisesReportRepository)
	{
		this.reportRepository = exercisesReportRepository;
	}
}


