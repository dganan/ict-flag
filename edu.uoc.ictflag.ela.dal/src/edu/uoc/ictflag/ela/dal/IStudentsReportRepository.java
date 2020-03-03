package edu.uoc.ictflag.ela.dal;

import java.util.List;

import edu.uoc.ictflag.ela.model.StudentsReportDataItem;

public interface IStudentsReportRepository extends IReportRepository<StudentsReportDataItem>
{
	List<String> getStudentsData() throws Exception;
}
