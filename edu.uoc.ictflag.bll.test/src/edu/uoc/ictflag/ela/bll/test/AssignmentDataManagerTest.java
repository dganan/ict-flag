package edu.uoc.ictflag.ela.bll.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.assertNull;
//import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import edu.uoc.ictflag.ela.bll.AssignmentDataManager;
import edu.uoc.ictflag.ela.dal.AssignmentDataRepository;
import edu.uoc.ictflag.ela.dal.IAssignmentDataRepository;
import edu.uoc.ictflag.ela.dal.IToolRepository;
import edu.uoc.ictflag.ela.dal.ToolRepository;
import edu.uoc.ictflag.ela.model.AssignmentDataRaw;
import edu.uoc.ictflag.ela.model.Tool;

@RunWith(MockitoJUnitRunner.class)
public class AssignmentDataManagerTest
{
	IAssignmentDataRepository assignmentDataRepository;
	IToolRepository toolRepository;
	
	AssignmentDataManager manager;
	
	private void setupMocks() throws Exception
	{
		assignmentDataRepository = mock(AssignmentDataRepository.class);
		toolRepository = mock(ToolRepository.class);
		
		manager = new AssignmentDataManager(assignmentDataRepository, toolRepository);
	}
	
	@Test
	public void processAssignmentDataRawTest() throws Exception
	{
		// PREPARE TEST
		
		setupMocks();
		
		Tool tool = new Tool();
		tool.setUuid("UUID1");
		
		when(toolRepository.getToolByCode(null)).thenReturn(null);
		when(toolRepository.getToolByCode("NON EXISTING TOOL")).thenReturn(null);
		when(toolRepository.getToolByCode("EXISTING TOOL")).thenReturn(tool);
		
		AssignmentDataRaw assignmentDataRaw = new AssignmentDataRaw();
		
		// RUN TEST
		
		// Check tool = null && user == null && assignment == null && assignmentAction == null
		
		List<String> errors = manager.processAssignmentDataRaw(assignmentDataRaw);
		
		assertEquals(4, errors.size());
		assertTrue(errors.get(0).equals("Tool 'null' does not exist."));
		assertTrue(errors.get(1).equals("User field is mandatory."));
		assertTrue(errors.get(2).equals("Assignment field is mandatory."));
		assertTrue(errors.get(3).equals("Incorrect assignment action: null"));
		
		// Check tool not exists && user empty && assignment empty && assignmentAction is wrong
		
		assignmentDataRaw.setTool("NON EXISTING TOOL");
		assignmentDataRaw.setUserId("");
		assignmentDataRaw.setAssignment("");
		assignmentDataRaw.setAction("WRONG ACTION");
		
		errors = manager.processAssignmentDataRaw(assignmentDataRaw);
		
		assertEquals(4, errors.size());
		assertTrue(errors.get(0).equals("Tool 'NON EXISTING TOOL' does not exist."));
		assertTrue(errors.get(1).equals("User field is mandatory."));
		assertTrue(errors.get(2).equals("Assignment field is mandatory."));
		assertTrue(errors.get(3).equals("Incorrect assignment action: WRONG ACTION"));

		// Check tool exists but UUID == null && grade null 
		
		assignmentDataRaw.setTool("EXISTING TOOL");
		assignmentDataRaw.setUserId("testStudent");
		assignmentDataRaw.setAssignment("assignment1");
		assignmentDataRaw.setAction("AA");
		
		errors = manager.processAssignmentDataRaw(assignmentDataRaw);
		
		assertEquals(2, errors.size());
		assertTrue(errors.get(0).equals("Tool key for tool 'EXISTING TOOL' is not valid."));
		assertTrue(errors.get(1).equals("Grade field is mandatory in assessment actions."));

		// Check tool exists but UUID is empty && grade empty 
		
		assignmentDataRaw.setToolUUID("");
		assignmentDataRaw.setGrade("");
		
		errors = manager.processAssignmentDataRaw(assignmentDataRaw);
		
		assertEquals(2, errors.size());
		assertTrue(errors.get(0).equals("Tool key for tool 'EXISTING TOOL' is not valid."));
		assertTrue(errors.get(1).equals("Grade field is mandatory in assessment actions."));
		
		// Check grade is not a double 
		
		assignmentDataRaw.setToolUUID("UUID1");
		assignmentDataRaw.setGrade("A");
		
		errors = manager.processAssignmentDataRaw(assignmentDataRaw);
		
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).equals("Incorrect grade: A"));
		
		// Check all ok 
		
		assignmentDataRaw.setGrade("8.2");
		
		errors = manager.processAssignmentDataRaw(assignmentDataRaw);
		
		assertEquals(0, errors.size());
	}
}
