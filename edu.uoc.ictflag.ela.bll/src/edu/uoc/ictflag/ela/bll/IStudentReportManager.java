package edu.uoc.ictflag.ela.bll;

import edu.uoc.ictflag.ela.model.StudentReportDataItem;
import edu.uoc.ictflag.ela.model.StudentReportGlobalProgressItem;

public interface IStudentReportManager
{
	StudentReportDataItem getReportData(String username) throws Exception;
	
	StudentReportGlobalProgressItem getGlobalProgressData() throws Exception;
}
