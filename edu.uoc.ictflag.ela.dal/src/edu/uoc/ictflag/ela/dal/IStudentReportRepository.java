package edu.uoc.ictflag.ela.dal;

import edu.uoc.ictflag.ela.model.StudentReportDataItem;
import edu.uoc.ictflag.ela.model.StudentReportGlobalProgressItem;

public interface IStudentReportRepository
{
	StudentReportDataItem getReportData(String username) throws Exception;
	
	StudentReportDataItem getReportData(String username, boolean fillGlobalData) throws Exception;
	
	StudentReportGlobalProgressItem getGlobalProgressData() throws Exception;
}
