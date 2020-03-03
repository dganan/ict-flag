package edu.uoc.ictflag.ela.dal;

import java.util.List;

import edu.uoc.ictflag.ela.model.Tool;

public interface IToolRepository
{
	List<Tool> getToolsList(String username) throws Exception;
	
	Tool getTool(String username, long id) throws Exception;
	
	void createOrUpdateTool(Tool program) throws Exception;
	
	void deleteTool(long id) throws Exception;
	
	Tool getToolByCode(String tool) throws Exception;
	
	Tool getToolById(long id) throws Exception;
}

