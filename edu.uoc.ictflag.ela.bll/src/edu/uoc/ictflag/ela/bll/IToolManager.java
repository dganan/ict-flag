package edu.uoc.ictflag.ela.bll;

import java.util.List;

import edu.uoc.ictflag.ela.model.Tool;

public interface IToolManager
{
	List<Tool> getToolsList(String username) throws Exception;
	
	void deleteTool(String username, long id) throws Exception;
	
	Tool getTool(String username, long id) throws Exception;
	
	void createOrUpdateTool(String username, Tool tool) throws Exception;
}
