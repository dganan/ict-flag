package edu.uoc.ictflag.fakefat;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

import edu.uoc.ictflag.clientlib.java.IctFlagApiClient;
import edu.uoc.ictflag.ela.model.Outcome;

public class FakeFATMainFrame extends JFrame
{
	//public static final String host = "https://localhost:8585/";
	public static final String host = "https://gres.uoc.edu:8585/";
	
	public static final String uuid = "afe9ead0-8a55-4bf7-bce9-f78e81afdecb";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public FakeFATMainFrame()
	{
		initUI();
	}
	
	private void initUI()
	{
		JButton exerciseStartButton = new JButton("Exercise start");
		
		exerciseStartButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				try
				{
					IctFlagApiClient.exerciseStart(new Date(), host, "std1", "FAKEFAT", uuid, "P1", "A1", "2015-1", null, "1");
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		JButton exerciseSubmissionButton = new JButton("Exercise submission");
		
		exerciseSubmissionButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				try
				{
					IctFlagApiClient.exerciseSubmission(new Date(), host, "std1", "FAKEFAT", uuid, "P1", "A1", "2015-1", null, "1", Outcome.RIGHT);
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		JButton exerciseAssessmentButton = new JButton("Exercise assessment");
		
		exerciseAssessmentButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				try
				{
					IctFlagApiClient.exerciseAssessment(new Date(), host, "std1", "FAKEFAT", uuid, "P1", "A1", "2015-1", null, "1", 9.4);
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		JButton assignmentStartButton = new JButton("Assignment start");
		
		assignmentStartButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				try
				{
					IctFlagApiClient.assignmentStart(new Date(), host, "std1", "FAKEFAT", uuid, "P1", "A1", "2015asfsdf-1", "1");
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		JButton assignmentEndButton = new JButton("Assignment end");
		
		assignmentEndButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				try
				{
					IctFlagApiClient.assignmentEnd(new Date(), host, "std1", "FAKEFAT", uuid, "P1", "A1", "2015-1", "1");
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		JButton assignmentAssessmentButton = new JButton("Assignment assessment");
		
		assignmentAssessmentButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				try
				{
					IctFlagApiClient.assignmentAssessment(new Date(), host, "std1", "FAKEFAT", uuid, "P1", "A1", "2015-1", "1", 6);
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		createLayout(exerciseStartButton, exerciseSubmissionButton, exerciseAssessmentButton, assignmentStartButton, assignmentEndButton,
				assignmentAssessmentButton);
		
		setTitle("Fake FAT");
		setSize(300, 300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private void createLayout(JComponent... arg)
	{
		Container pane = getContentPane();
		BoxLayout gl = new BoxLayout(pane, BoxLayout.PAGE_AXIS);
		pane.setLayout(gl);
		
		//gl.setAutoCreateContainerGaps(true);
		
		for (JComponent jc : arg)
		{
			pane.add(jc);
		}
	}
	
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				FakeFATMainFrame ex = new FakeFATMainFrame();
				ex.setVisible(true);
			}
		});
	}
}
