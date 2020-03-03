package edu.uoc.ictflag.clientlib.java;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.client.urlconnection.HTTPSProperties;

import edu.uoc.ictflag.ela.model.AssignmentAction;
import edu.uoc.ictflag.ela.model.AssignmentDataRaw;
import edu.uoc.ictflag.ela.model.ExerciseAction;
import edu.uoc.ictflag.ela.model.ExerciseDataRaw;
import edu.uoc.ictflag.ela.model.Outcome;

public class IctFlagApiClient
{
	private static final String exercisePath = "exercise-data";
	private static final String assignmentPath = "assignment-data";
	
	public static void exerciseStart(Date timestamp, String host, String user, String tool, String toolUUID, String programCode, String subjectCode,
			String semester, String assignment, String exercise) throws Exception
	{
		ExerciseDataRaw ad = new ExerciseDataRaw(timestamp, tool, toolUUID, user, programCode, subjectCode, semester,
				ExerciseAction.EXERCISE_START.toString(), assignment, exercise, null, null);
				
		sendData(host, exercisePath, ad);
	}
	
	public static void exerciseStartSafe(Date timestamp, String host, String user, String tool, String toolUUID, String programCode,
			String subjectCode, String semester, String assignment, String exercise)
	{
		try
		{
			exerciseStart(timestamp, host, user, tool, toolUUID, programCode, subjectCode, semester, assignment, exercise);
		}
		catch (Exception e)
		{
		}
	}
	
	public static void exerciseSubmission(Date timestamp, String host, String user, String tool, String toolUUID, String programCode,
			String subjectCode, String semester, String assignment, String exercise, Outcome outcome) throws Exception
	{
		ExerciseDataRaw ad = new ExerciseDataRaw(timestamp, tool, toolUUID, user, programCode, subjectCode, semester,
				ExerciseAction.EXERCISE_SUBMISSION.toString(), assignment, exercise, outcome.toString(), null);
				
		sendData(host, exercisePath, ad);
	}
	
	public static void exerciseSubmissionSafe(Date timestamp, String host, String user, String tool, String toolUUID, String programCode,
			String subjectCode, String semester, String assignment, String exercise, Outcome outcome)
	{
		try
		{
			exerciseSubmission(timestamp, host, user, tool, toolUUID, programCode, subjectCode, semester, assignment, exercise, outcome);
		}
		catch (Exception e)
		{
		}
	}

	public static void exerciseAssessment(Date timestamp, String host, String user, String tool, String toolUUID, String programCode,
			String subjectCode, String semester, String assignment, String exercise, double grade) throws Exception
	{
		grade = Math.min(10, Math.max(0, grade)); // normalize to scale 0..10
		
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.CEILING);
		
		ExerciseDataRaw ad = new ExerciseDataRaw(timestamp, tool, toolUUID, user, programCode, subjectCode, semester,
				ExerciseAction.EXERCISE_ASSESSMENT.toString(), assignment, exercise, null, df.format(grade));
				
		sendData(host, exercisePath, ad);
	}
	
	public static void exerciseAssessmentSafe(Date timestamp, String host, String user, String tool, String toolUUID, String programCode,
			String subjectCode, String semester, String assignment, String exercise, double grade)
	{
		try
		{
			exerciseAssessment(timestamp, host, user, tool, toolUUID, programCode, subjectCode, semester, assignment, exercise, grade);
		}
		catch (Exception e)
		{
		}
	}
	
	public static void assignmentStart(Date timestamp, String host, String user, String tool, String toolUUID, String programCode, String subjectCode,
			String semester, String assignment) throws Exception
	{
		AssignmentDataRaw ad = new AssignmentDataRaw(timestamp, tool, toolUUID, user, programCode, subjectCode, semester,
				AssignmentAction.ASSIGNMENT_START.toString(), assignment, null);
				
		sendData(host, assignmentPath, ad);
	}
	
	public static void assignmentStartSafe(Date timestamp, String host, String user, String tool, String toolUUID, String programCode,
			String subjectCode, String semester, String assignment)
	{
		try
		{
			assignmentStart(timestamp, host, user, tool, toolUUID, programCode, subjectCode, semester, assignment);
		}
		catch (Exception e)
		{
		}
	}
	
	public static void assignmentEnd(Date timestamp, String host, String user, String tool, String toolUUID, String programCode, String subjectCode,
			String semester, String assignment) throws Exception
	{
		AssignmentDataRaw ad = new AssignmentDataRaw(timestamp, tool, toolUUID, user, programCode, subjectCode, semester,
				AssignmentAction.ASSIGNMENT_END.toString(), assignment, null);
		
		sendData(host, assignmentPath, ad);
	}
	
	public static void assignmentEndSafe(Date timestamp, String host, String user, String tool, String toolUUID, String programCode,
			String subjectCode, String semester, String assignment)
	{
		try
		{
			assignmentEnd(timestamp, host, user, tool, toolUUID, programCode, subjectCode, semester, assignment);
		}
		catch (Exception e)
		{
		}
	}
	
	public static void assignmentAssessment(Date timestamp, String host, String user, String tool, String toolUUID, String programCode,
			String subjectCode, String semester, String assignment, double grade) throws Exception
	{
		grade = Math.min(10, Math.max(0, grade)); // normalize to scale 0..10
		
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.CEILING);
		
		AssignmentDataRaw ad = new AssignmentDataRaw(timestamp, tool, toolUUID, user, programCode, subjectCode, semester,
				AssignmentAction.ASSIGNMENT_ASSESSMENT.toString(), assignment, df.format(grade));
		
		sendData(host, assignmentPath, ad);
	}
	
	public static void assignmentAssessmentSafe(Date timestamp, String host, String user, String tool, String toolUUID, String programCode,
			String subjectCode, String semester, String assignment, double grade)
	{
		try
		{
			assignmentAssessment(timestamp, host, user, tool, toolUUID, programCode, subjectCode, semester, assignment, grade);
		}
		catch (Exception e)
		{
		}
	}
	
	//	public static void assignmentAssessment(Date timestamp, String host, String user, String tool, String toolUUID, String programCode,
	//			String subjectCode,
	//			String semester,
	//			String assignment, double grade, double minGrade, double maxGrade)
	//	{
	//		grade = Math.min(maxGrade, Math.max(0, minGrade)); // normalize to scale minGrade..maxGrade
	//		
	//		// Convert from original grade range to 0..10 range
	//		
	//		double range = (maxGrade - minGrade);
	//		
	//		if (range <= 0)
	//		{
	//			grade = 0;
	//		}
	//		else
	//		{
	//			grade = (((grade - minGrade) * 10) / range);
	//		}
	//
	//		DecimalFormat df = new DecimalFormat("#.##");
	//		df.setRoundingMode(RoundingMode.CEILING);
	//		
	//		AssignmentDataRaw ad = new AssignmentDataRaw(timestamp, tool, toolUUID, user, programCode, subjectCode, semester,
	//				AssignmentAction.ASSIGNMENT_ASSESSMENT.toString(), null, null, ElementType.ASSIGNMENT.toString(), assignment,
	//				ResultType.GRADE.toString(),
	//				df.format(grade));
	//				
	//		sendData(host, assignmentPath, ad);
	//	}
	
	//	private static void sendDataSafe(String host, String resourcePath, Object dataRaw)
	//	{
	//		try
	//		{
	//			sendData(host, resourcePath, dataRaw);
	//		}
	//		catch (Exception e)
	//		{
	//			e.printStackTrace();
	//			
	//			Throwable t = e.getCause();
	//			
	//			while (t != null)
	//			{
	//				t.printStackTrace();
	//				
	//				t = t.getCause();
	//			}
	//		}
	//	}
	
	private static void sendData(String host, String resourcePath, Object dataRaw) throws Exception
	{
		ClientConfig clientConfig = new DefaultClientConfig();
		
		if (host.startsWith("https"))
		{
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager()
			{
				public java.security.cert.X509Certificate[] getAcceptedIssuers()
				{
					return new java.security.cert.X509Certificate[0];
				}
				
				public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
				{
				}
				
				public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
				{
				}
			} };

			SSLContext ctx = SSLContext.getInstance("SSL");
			
			ctx.init(null, trustAllCerts, new java.security.SecureRandom());
			
			HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());
			
			clientConfig.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(null, ctx));
		}
		
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		Client client = Client.create(clientConfig);
		
		WebResource resource = client.resource(host + "api/" + resourcePath);
		
		ClientResponse response = resource.type(MediaType.APPLICATION_JSON).accept(MediaType.TEXT_PLAIN).post(ClientResponse.class, dataRaw);
		
		// check response status code
		if (response.getStatus() != 200)
		{
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
	}
}
