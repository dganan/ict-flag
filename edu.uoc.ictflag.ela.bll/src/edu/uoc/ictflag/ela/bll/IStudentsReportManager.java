package edu.uoc.ictflag.ela.bll;

import java.util.List;

import edu.uoc.ictflag.ela.model.StudentsReportDataItem;

public interface IStudentsReportManager extends IReportManager<StudentsReportDataItem>
{
	List<String> getStudentsData() throws Exception;
}
