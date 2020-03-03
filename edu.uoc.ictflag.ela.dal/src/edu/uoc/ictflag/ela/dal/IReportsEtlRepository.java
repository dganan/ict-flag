package edu.uoc.ictflag.ela.dal;

public interface IReportsEtlRepository
{
	void startReportsEtlProcess() throws Exception;
	
	void exerciseDataEtlProcess() throws Exception;
	
	void assignmentDataEtlProcess() throws Exception;
}
