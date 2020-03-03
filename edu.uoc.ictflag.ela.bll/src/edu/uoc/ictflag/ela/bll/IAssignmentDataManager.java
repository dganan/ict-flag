package edu.uoc.ictflag.ela.bll;

import java.util.List;

import edu.uoc.ictflag.ela.model.AssignmentDataRaw;

public interface IAssignmentDataManager
{
	List<String> processAssignmentDataRaw(AssignmentDataRaw assignmentDataRaw) throws Exception;
	
	List<String> importAssignmentAssessmentData(String importText) throws Exception;
}
