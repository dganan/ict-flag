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

import edu.uoc.ictflag.ela.bll.ExerciseDataManager;
import edu.uoc.ictflag.ela.dal.ExerciseDataRepository;
import edu.uoc.ictflag.ela.dal.IExerciseDataRepository;
import edu.uoc.ictflag.ela.dal.IToolRepository;
import edu.uoc.ictflag.ela.dal.ToolRepository;
import edu.uoc.ictflag.ela.model.ExerciseDataRaw;
import edu.uoc.ictflag.ela.model.Tool;

@RunWith(MockitoJUnitRunner.class)
public class ExerciseDataManagerTest
{
	IExerciseDataRepository exerciseDataRepository;
	IToolRepository toolRepository;
	
	ExerciseDataManager manager;
	
	private void setupMocks() throws Exception
	{
		exerciseDataRepository = mock(ExerciseDataRepository.class);
		toolRepository = mock(ToolRepository.class);
		
		manager = new ExerciseDataManager(exerciseDataRepository, toolRepository);
	}
	
	@Test
	public void processExerciseDataRawTest() throws Exception
	{
		// PREPARE TEST
		
		setupMocks();
		
		Tool tool = new Tool();
		tool.setUuid("UUID1");
		
		when(toolRepository.getToolByCode(null)).thenReturn(null);
		when(toolRepository.getToolByCode("NON EXISTING TOOL")).thenReturn(null);
		when(toolRepository.getToolByCode("EXISTING TOOL")).thenReturn(tool);
		
		ExerciseDataRaw exerciseDataRaw = new ExerciseDataRaw();
		
		// RUN TEST
		
		// Check tool = null && user == null && exercise == null && exerciseAction == null
		
		List<String> errors = manager.processExerciseDataRaw(exerciseDataRaw);
		
		assertEquals(4, errors.size());
		assertTrue(errors.get(0).equals("Tool 'null' does not exist."));
		assertTrue(errors.get(1).equals("User field is mandatory."));
		assertTrue(errors.get(2).equals("Exercise field is mandatory."));
		assertTrue(errors.get(3).equals("Incorrect exercise action: null"));
		
		// Check tool not exists && user empty && exercise empty && exerciseAction is wrong
		
		exerciseDataRaw.setTool("NON EXISTING TOOL");
		exerciseDataRaw.setUserId("");
		exerciseDataRaw.setExercise("");
		exerciseDataRaw.setAction("WRONG ACTION");
		
		errors = manager.processExerciseDataRaw(exerciseDataRaw);
		
		assertEquals(4, errors.size());
		assertTrue(errors.get(0).equals("Tool 'NON EXISTING TOOL' does not exist."));
		assertTrue(errors.get(1).equals("User field is mandatory."));
		assertTrue(errors.get(2).equals("Exercise field is mandatory."));
		assertTrue(errors.get(3).equals("Incorrect exercise action: WRONG ACTION"));

		// Check tool exists but UUID == null && grade null 
		
		exerciseDataRaw.setTool("EXISTING TOOL");
		exerciseDataRaw.setUserId("testStudent");
		exerciseDataRaw.setExercise("exercise1");
		exerciseDataRaw.setAction("EA");
		
		errors = manager.processExerciseDataRaw(exerciseDataRaw);
		
		assertEquals(2, errors.size());
		assertTrue(errors.get(0).equals("Tool key for tool 'EXISTING TOOL' is not valid."));
		assertTrue(errors.get(1).equals("Grade field is mandatory in assessment actions."));

		// Check tool exists but UUID is empty && grade empty 
		
		exerciseDataRaw.setToolUUID("");
		exerciseDataRaw.setGrade("");
		
		errors = manager.processExerciseDataRaw(exerciseDataRaw);
		
		assertEquals(2, errors.size());
		assertTrue(errors.get(0).equals("Tool key for tool 'EXISTING TOOL' is not valid."));
		assertTrue(errors.get(1).equals("Grade field is mandatory in assessment actions."));
		
		// Check grade is not a double 
		
		exerciseDataRaw.setToolUUID("UUID1");
		exerciseDataRaw.setGrade("A");
		
		errors = manager.processExerciseDataRaw(exerciseDataRaw);
		
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).equals("Incorrect grade: A"));
		
		// Check all ok 
		
		exerciseDataRaw.setGrade("8.2");
		
		errors = manager.processExerciseDataRaw(exerciseDataRaw);
		
		assertEquals(0, errors.size());
	}
}
