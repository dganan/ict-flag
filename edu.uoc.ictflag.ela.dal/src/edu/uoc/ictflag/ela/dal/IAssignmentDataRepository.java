package edu.uoc.ictflag.ela.dal;

import edu.uoc.ictflag.ela.model.AssignmentDataRaw;

public interface IAssignmentDataRepository
{
	void saveAssignmentDataRaw(AssignmentDataRaw assignmentDataRaw) throws Exception;
	
	void processAssignmentDataRaw(AssignmentDataRaw assignmentDataRaw) throws Exception;
}
